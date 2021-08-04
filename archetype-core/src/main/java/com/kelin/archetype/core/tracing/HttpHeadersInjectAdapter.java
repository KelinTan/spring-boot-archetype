// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing;

import io.opentracing.propagation.TextMap;
import org.apache.http.HttpRequest;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Pavol Loffay
 * @author Kelin Tan
 */
public class HttpHeadersInjectAdapter implements TextMap {
    private final HttpRequest httpRequest;

    public HttpHeadersInjectAdapter(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public void put(String key, String value) {
        httpRequest.setHeader(key, value);
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        throw new UnsupportedOperationException("This class should be used only with tracer#inject()");
    }
}
