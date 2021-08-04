// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing.http.httpclient;

import com.kelin.archetype.common.utils.HttpUtils;
import com.kelin.archetype.core.rpc.RpcConstants;
import com.kelin.archetype.core.tracing.http.HttpClientSpanDecorator;
import com.kelin.archetype.core.tracing.http.HttpRequestTracing;
import com.kelin.archetype.core.tracing.http.HttpResponseTracing;
import io.opentracing.References;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.execchain.ClientExecChain;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracing is added before {@link org.apache.http.impl.execchain.ProtocolExec} which is invoked as the next to last.
 * Note that {@link org.apache.http.impl.execchain.RedirectExec} is invoked before so this exec has to deal with
 * redirects.
 *
 * @author Pavol Loffay
 * @author Kelin Tan
 */
public class TracingClientExec implements ClientExecChain {
    public static final String COMPONENT_NAME = "apache-httpclient";
    public static final String PARENT_CONTEXT = TracingHttpClientBuilder.class.getName() + ".parentSpanContext";

    /**
     * Id of {@link HttpClientContext#setAttribute(String, Object)} representing span associated with the current client
     * processing. Referenced span is local span not a span representing HTTP communication.
     */
    protected static final String SPAN_PROP = TracingHttpClientBuilder.class.getName() + ".currentSpan";
    /**
     * Tracing {@link ClientExecChain} is executed after redirect exec, so on redirects it is called multiple times.
     * This is used as an id for {@link HttpClientContext#setAttribute(String, Object)} to store number of redirects.
     */
    protected static final String REDIRECT_COUNT = TracingHttpClientBuilder.class.getName() + ".redirectCount";

    private final RedirectStrategy redirectStrategy;
    private final ClientExecChain requestExecutor;
    private final boolean redirectHandlingDisabled;
    private final boolean injectDisabled;

    private final Tracer tracer;
    private final List<HttpClientSpanDecorator> spanDecorators;

    public TracingClientExec(
            ClientExecChain clientExecChain,
            RedirectStrategy redirectStrategy,
            boolean redirectHandlingDisabled,
            boolean injectDisabled,
            Tracer tracer,
            List<HttpClientSpanDecorator> spanDecorators) {
        this.requestExecutor = clientExecChain;
        this.redirectStrategy = redirectStrategy;
        this.redirectHandlingDisabled = redirectHandlingDisabled;
        this.injectDisabled = injectDisabled;
        this.tracer = tracer;
        this.spanDecorators = new ArrayList<>(spanDecorators);
    }

    @Override
    public CloseableHttpResponse execute(
            HttpRoute route,
            HttpRequestWrapper request,
            HttpClientContext clientContext,
            HttpExecutionAware execAware) throws IOException, HttpException {
        Span localSpan = handleLocalSpan(request, clientContext);
        CloseableHttpResponse response = null;
        try {
            return (response = handleNetworkProcessing(localSpan, route, request, clientContext, execAware));
        } catch (Exception e) {
            localSpan.finish();
            throw e;
        } finally {
            if (response != null) {
                /*
                  This exec runs after {@link org.apache.http.impl.execchain.RedirectExec} which loops
                  until there is no redirect or reaches max redirect count.
                  {@link RedirectStrategy} is used to decide whether localSpan should be finished or not.
                  If there is a redirect localSpan is not finished and redirect is logged.
                 */
                Integer redirectCount = clientContext.getAttribute(REDIRECT_COUNT, Integer.class);
                if (!redirectHandlingDisabled
                        && clientContext.getRequestConfig().isRedirectsEnabled()
                        && redirectStrategy.isRedirected(request, response, clientContext)
                        && ++redirectCount < clientContext.getRequestConfig().getMaxRedirects()) {
                    clientContext.setAttribute(REDIRECT_COUNT, redirectCount);
                } else {
                    localSpan.finish();
                }
            }
        }
    }

    protected Span handleLocalSpan(HttpRequest httpRequest, HttpClientContext clientContext) {
        Tracer.SpanBuilder spanBuilder = tracer
                .buildSpan(getLocalSpanName(httpRequest))
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT)
                .withTag(Tags.COMPONENT.getKey(), COMPONENT_NAME);

        if (clientContext.getAttribute(PARENT_CONTEXT, SpanContext.class) != null) {
            spanBuilder.ignoreActiveSpan().asChildOf(clientContext.getAttribute(PARENT_CONTEXT, SpanContext.class));
        }

        Span previousLocalSpan = clientContext.getAttribute(SPAN_PROP, Span.class);
        spanBuilder.addReference(References.FOLLOWS_FROM,
                previousLocalSpan == null ? null : previousLocalSpan.context());

        Span localSpan = spanBuilder.start();
        clientContext.setAttribute(SPAN_PROP, localSpan);
        clientContext.setAttribute(REDIRECT_COUNT, 0);
        return localSpan;
    }

    protected CloseableHttpResponse handleNetworkProcessing(
            Span parentSpan,
            HttpRoute route,
            HttpRequestWrapper request,
            HttpClientContext clientContext,
            HttpExecutionAware execAware) throws IOException, HttpException {
        Span redirectSpan = tracer.buildSpan(request.getMethod())
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT)
                .asChildOf(parentSpan)
                .start();
        if (!injectDisabled) {
            tracer.inject(redirectSpan.context(), Format.Builtin.HTTP_HEADERS, new HttpHeadersInjectAdapter(request));
        }

        try (Scope redirectScope = tracer.activateSpan(redirectSpan)) {
            spanDecorators.forEach(decorator -> decorator.onRequest(buildRequestTracing(request), redirectSpan));
            CloseableHttpResponse response = requestExecutor.execute(route, request, clientContext, execAware);
            spanDecorators.forEach(decorator -> decorator
                    .onResponse(new HttpResponseTracing(response.getStatusLine().getStatusCode()), redirectSpan));
            return response;
        } catch (IOException | HttpException | RuntimeException e) {
            spanDecorators.forEach(decorator -> decorator.onError(e, redirectSpan));
            throw e;
        } finally {
            redirectSpan.finish();
        }
    }

    /**
     * use rpc-header name when {@see RpcConstants.RPC_NAME_HEADER}
     */
    private String getLocalSpanName(HttpRequest httpRequest) {
        Header rpcHeader = httpRequest.getFirstHeader(RpcConstants.RPC_NAME_HEADER);
        if (rpcHeader != null) {
            return rpcHeader.getValue();
        } else {
            return httpRequest.getRequestLine().getMethod();
        }
    }

    private HttpRequestTracing buildRequestTracing(HttpRequestWrapper request) {
        HttpRequestTracing tracing = new HttpRequestTracing();
        tracing.setMethod(request.getMethod());

        URI uri = request.getURI();
        HttpHost target = request.getTarget();

        if (uri != null && uri.isAbsolute()) {
            tracing.setUrl(uri.toString());
            tracing.setHostName(uri.getHost());
            tracing.setPort(HttpUtils.extractUriPort(uri));
        } else if (target != null) {
            tracing.setUrl(request.getTarget() + request.getRequestLine().getUri());
            tracing.setHostName(target.getHostName());
            tracing.setPort(HttpUtils.extractHostPort(target));
        }
        return tracing;
    }
}
