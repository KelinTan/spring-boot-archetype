// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing;

import com.kelin.archetype.core.tracing.asynchttpclient.TracingAsyncHttpClient;
import com.kelin.archetype.core.tracing.httpclient.TracingHttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;

/**
 * @author Kelin Tan
 */
public class TracingHttpClientFactory {
    private static final CloseableHttpClient TRACING_HTTP_CLIENT;

    private static final AsyncHttpClient TRACING_ASYNC_HTTP_CLIENT;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(32);
        connectionManager.setMaxTotal(1024);

        TRACING_HTTP_CLIENT = new TracingHttpClientBuilder()
                //use disableInjection instead of disableRedirectHandling()
                // .disableInjection()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .disableCookieManagement()
                .disableAuthCaching()
                .disableConnectionState()
                .build();

        TRACING_ASYNC_HTTP_CLIENT = new TracingAsyncHttpClient(Dsl.config()
                .setMaxConnections(1024)
                .setMaxConnectionsPerHost(128).build());
    }

    public static CloseableHttpClient getTracingHttpClient() {
        return TRACING_HTTP_CLIENT;
    }

    public static AsyncHttpClient getTracingAsyncHttpClient() {
        return TRACING_ASYNC_HTTP_CLIENT;
    }
}
