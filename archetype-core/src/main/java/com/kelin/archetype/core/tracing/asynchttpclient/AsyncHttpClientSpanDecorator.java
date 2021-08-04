// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing.asynchttpclient;

import io.opentracing.Span;
import io.opentracing.tag.Tags;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Span decorator to add tags, logs and operation name.
 */
public interface AsyncHttpClientSpanDecorator {
    String COMPONENT_NAME = "async-httpclient";

    /**
     * Decorate span before a request is made.
     *
     * @param request request
     * @param span span
     */
    void onRequest(Request request, Span span);

    /**
     * Decorate span on a response status
     *
     * @param responseStatus response status
     * @param span span
     */
    void onStatus(HttpResponseStatus responseStatus, Span span);

    /**
     * Decorate span on an error
     *
     * @param throwable exception
     * @param span span
     */
    void onError(Throwable throwable, Span span);

    AsyncHttpClientSpanDecorator DEFAULT = new AsyncHttpClientSpanDecorator() {
        @Override
        public void onRequest(Request request, Span span) {
            Tags.COMPONENT.set(span, COMPONENT_NAME);
            Tags.HTTP_METHOD.set(span, request.getMethod());
            Tags.HTTP_URL.set(span, request.getUrl());
        }

        @Override
        public void onStatus(HttpResponseStatus responseStatus, Span span) {
            span.setTag(Tags.HTTP_STATUS.getKey(), responseStatus.getStatusCode());
        }

        @Override
        public void onError(Throwable throwable, Span span) {
            Tags.ERROR.set(span, Boolean.TRUE);
            if (throwable != null) {
                span.log(errorLogs(throwable));
            }
        }

        private Map<String, Object> errorLogs(final Throwable throwable) {
            final Map<String, Object> errorLogs = new HashMap<>(2);
            errorLogs.put("event", Tags.ERROR.getKey());
            errorLogs.put("error.object", throwable);
            return errorLogs;
        }
    };
}