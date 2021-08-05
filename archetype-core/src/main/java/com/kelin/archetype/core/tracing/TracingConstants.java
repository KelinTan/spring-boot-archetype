// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing;

import com.kelin.archetype.core.tracing.http.httpclient.TracingHttpClientBuilder;

/**
 * @author Kelin Tan
 */
public class TracingConstants {
    public static final String ASYNC_COMPONENT_NAME = "async-httpclient";
    public static final String APACHE_COMPONENT_NAME = "apache-httpclient";
    public static final String PARENT_CONTEXT = TracingHttpClientBuilder.class.getName() + ".parentSpanContext";

    public static final String MDC_TRACING = "X-TRACE";
}
