// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.constants;

/**
 * @author Kelin Tan
 */
public class TracingConstants {
    public static final String ASYNC_COMPONENT_NAME = "async-httpclient";
    public static final String APACHE_COMPONENT_NAME = "apache-httpclient";
    public static final String PARENT_CONTEXT = "TracingHttpClientBuilder.parentSpanContext";

    public static final String MDC_TRACING = "X-TRACE";
    public static final String ERROR_META_TRACE_ID = "traceId";
    public static final String CUSTOM_TRACING_NAME_HEADER = "X-CUSTOM-TRACE-NAME";
}
