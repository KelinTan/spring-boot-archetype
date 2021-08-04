// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing;

/**
 * @author Kelin Tan
 */
public final class TracingConstants {
    /**
     * SpanContext which will be used as a parent for created client span.
     */
    public static final String PARENT_CONTEXT = TracingHttpClientBuilder.class.getName() + ".parentSpanContext";

    public static final String COMPONENT_NAME = "apache-httpclient";
}
