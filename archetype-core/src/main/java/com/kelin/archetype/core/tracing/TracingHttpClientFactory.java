// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.tracing;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author Kelin Tan
 */
public class TracingHttpClientFactory {
    private static final CloseableHttpClient TRACING_HTTP_CLIENT;

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
    }

    public static CloseableHttpClient getTracingHttpClient() {
        return TRACING_HTTP_CLIENT;
    }
}
