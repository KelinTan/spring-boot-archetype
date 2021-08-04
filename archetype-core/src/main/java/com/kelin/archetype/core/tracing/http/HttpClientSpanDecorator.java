// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing.http;

import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kelin Tan
 */
public interface HttpClientSpanDecorator {
    /**
     * Decorate span before execute request
     *
     * @param requestTracing requestTracing
     * @param span span
     */
    void onRequest(HttpRequestTracing requestTracing, Span span);

    /**
     * Decorate span after response
     *
     * @param responseTracing responseTracing
     * @param span span
     */
    void onResponse(HttpResponseTracing responseTracing, Span span);

    /**
     * Decorate span on an error
     *
     * @param throwable exception
     * @param span span
     */
    void onError(Throwable throwable, Span span);

    class StandardTags implements HttpClientSpanDecorator {
        @Override
        public void onRequest(HttpRequestTracing requestTracing, Span span) {
            Tags.HTTP_METHOD.set(span, requestTracing.getMethod());
            Tags.HTTP_URL.set(span, requestTracing.getUrl());
            Tags.PEER_HOSTNAME.set(span, requestTracing.getHostName());
            Tags.PEER_PORT.set(span, requestTracing.getPort());
        }

        @Override
        public void onResponse(HttpResponseTracing responseTracing, Span span) {
            Tags.HTTP_STATUS.set(span, responseTracing.getStatus());
        }

        @Override
        public void onError(Throwable throwable, Span span) {
            Tags.ERROR.set(span, Boolean.TRUE);

            Map<String, Object> errorLogs = new HashMap<>(3);
            errorLogs.put(Fields.EVENT, Tags.ERROR.getKey());
            errorLogs.put(Fields.ERROR_KIND, "Exception");
            errorLogs.put(Fields.ERROR_OBJECT, throwable);
            span.log(errorLogs);
        }
    }
}
