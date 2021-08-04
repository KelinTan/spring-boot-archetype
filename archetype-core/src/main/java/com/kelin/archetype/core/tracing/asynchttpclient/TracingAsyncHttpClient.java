// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing.asynchttpclient;

import com.kelin.archetype.core.rpc.RpcConstants;
import io.netty.handler.codec.http.HttpHeaders;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An {@link org.asynchttpclient.AsyncHttpClient} that traces HTTP calls using the OpenTracing API.
 */
public class TracingAsyncHttpClient extends DefaultAsyncHttpClient {
    private final Tracer tracer;
    private final List<AsyncHttpClientSpanDecorator> decorators;
    private final boolean traceWithActiveSpanOnly;

    public TracingAsyncHttpClient() {
        this(GlobalTracer.get(), Collections.singletonList(AsyncHttpClientSpanDecorator.DEFAULT), false);
    }

    public TracingAsyncHttpClient(Tracer tracer) {
        this(tracer, Collections.singletonList(AsyncHttpClientSpanDecorator.DEFAULT), false);
    }

    public TracingAsyncHttpClient(Tracer tracer, boolean traceWithActiveSpanOnly) {
        this(tracer, Collections.singletonList(AsyncHttpClientSpanDecorator.DEFAULT), traceWithActiveSpanOnly);
    }

    public TracingAsyncHttpClient(Tracer tracer, List<AsyncHttpClientSpanDecorator> decorators) {
        this(tracer, decorators, false);
    }

    public TracingAsyncHttpClient(Tracer tracer, List<AsyncHttpClientSpanDecorator> decorators,
            boolean traceWithActiveSpanOnly) {
        this.tracer = tracer;
        this.decorators = new ArrayList<>(decorators);
        this.traceWithActiveSpanOnly = traceWithActiveSpanOnly;
    }

    public TracingAsyncHttpClient(AsyncHttpClientConfig config) {
        this(config, GlobalTracer.get(), Collections.singletonList(AsyncHttpClientSpanDecorator.DEFAULT), false);
    }

    public TracingAsyncHttpClient(AsyncHttpClientConfig config, Tracer tracer,
            boolean traceWithActiveSpanOnly) {
        this(config, tracer, Collections.singletonList(AsyncHttpClientSpanDecorator.DEFAULT), traceWithActiveSpanOnly);
    }

    public TracingAsyncHttpClient(AsyncHttpClientConfig config, Tracer tracer,
            List<AsyncHttpClientSpanDecorator> decorators) {
        this(config, tracer, decorators, false);
    }

    public TracingAsyncHttpClient(AsyncHttpClientConfig config, Tracer tracer,
            List<AsyncHttpClientSpanDecorator> decorators, boolean traceWithActiveSpanOnly) {
        super(config);
        this.tracer = tracer;
        this.decorators = new ArrayList<>(decorators);
        this.traceWithActiveSpanOnly = traceWithActiveSpanOnly;
    }

    @Override
    public <T> ListenableFuture<T> executeRequest(Request request, AsyncHandler<T> handler) {
        if (traceWithActiveSpanOnly && tracer.activeSpan() == null) {
            return super.executeRequest(request, handler);
        }

        final Span span = tracer
                .buildSpan(getSpanName(request))
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT).start();

        decorators.forEach(decorator -> decorator.onRequest(request, span));
        tracer.inject(span.context(), Format.Builtin.HTTP_HEADERS, new HttpHeadersInjectAdapter(request.getHeaders()));

        return super.executeRequest(request, new TracingAsyncHandler<>(tracer, handler, span, decorators));
    }

    /**
     * use rpc-header name when {@see RpcConstants.RPC_NAME_HEADER}
     */
    private String getSpanName(Request request) {
        HttpHeaders headers = request.getHeaders();
        if (headers.contains(RpcConstants.RPC_NAME_HEADER)) {
            return headers.get(RpcConstants.RPC_NAME_HEADER);
        } else {
            return request.getMethod();
        }
    }
}
