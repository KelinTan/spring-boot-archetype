// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing.httpclient;

import com.kelin.archetype.common.utils.HttpUtils;
import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.HttpContext;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Decorate span at different stages of request processing. Do not finish span in decorator.
 *
 * @author Pavol Loffay
 * @author Kelin Tan
 */
public interface ApacheClientSpanDecorator {
    /**
     * Decorate span before request is fired.
     *
     * @param request request
     * @param httpContext context
     * @param span span to decorate
     */
    void onRequest(HttpRequestWrapper request, HttpContext httpContext, Span span);

    /**
     * Decorate span after response is received.
     *
     * @param response response
     * @param httpContext context
     * @param span span to decorate
     */
    void onResponse(HttpResponse response, HttpContext httpContext, Span span);

    /**
     * Decorate span span on error e.g. {@link java.net.UnknownHostException}
     *
     * @param request request
     * @param httpContext context
     * @param ex exception
     * @param span span to decorate
     */
    void onError(HttpRequest request, HttpContext httpContext, Exception ex, Span span);

    /**
     * Decorator which adds standard set of tags and logs.
     */
    @Slf4j
    class StandardTags implements ApacheClientSpanDecorator {
        @Override
        public void onRequest(HttpRequestWrapper request, HttpContext httpContext, Span span) {
            URI uri = request.getURI();
            HttpHost target = request.getTarget();

            Tags.HTTP_METHOD.set(span, request.getRequestLine().getMethod());

            if (uri != null && uri.isAbsolute()) {
                Tags.HTTP_URL.set(span, uri.toString());
                Tags.PEER_HOSTNAME.set(span, uri.getHost());
                Tags.PEER_PORT.set(span, HttpUtils.extractUriPort(uri));
            } else if (target != null) {
                Tags.HTTP_URL.set(span, request.getTarget() + request.getRequestLine().getUri());
                Tags.PEER_HOSTNAME.set(span, target.getHostName());
                Tags.PEER_PORT.set(span, HttpUtils.extractHostPort(target));
            }
        }

        @Override
        public void onResponse(HttpResponse response, HttpContext httpContext, Span span) {
            Tags.HTTP_STATUS.set(span, response.getStatusLine().getStatusCode());
        }

        @Override
        public void onError(HttpRequest request, HttpContext httpContext, Exception ex, Span span) {
            Tags.ERROR.set(span, Boolean.TRUE);

            Map<String, Object> errorLogs = new HashMap<>(3);
            errorLogs.put(Fields.EVENT, Tags.ERROR.getKey());
            errorLogs.put(Fields.ERROR_KIND, "Exception");
            errorLogs.put(Fields.ERROR_OBJECT, ex);
            span.log(errorLogs);
        }
    }
}
