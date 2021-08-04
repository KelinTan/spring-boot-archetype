// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing.asynchttpclient;

import io.netty.handler.codec.http.HttpHeaders;
import io.opentracing.propagation.TextMap;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map.Entry;

public class HttpHeadersInjectAdapter implements TextMap {
    private final HttpHeaders headers;

    public HttpHeadersInjectAdapter(HttpHeaders headers) {
        this.headers = headers;
    }

    @NotNull
    @Override
    public Iterator<Entry<String, String>> iterator() {
        throw new UnsupportedOperationException("iterator not supported with Tracer.inject()");
    }

    @Override
    public void put(final String key, final String value) {
        headers.add(key, value);
    }
}
