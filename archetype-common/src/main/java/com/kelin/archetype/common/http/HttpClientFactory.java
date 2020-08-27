// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author Kelin Tan
 */
public class HttpClientFactory {
    private static final CloseableHttpClient DEFAULT_HTTP_CLIENT;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(32);
        connectionManager.setMaxTotal(1024);

        DEFAULT_HTTP_CLIENT = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .disableCookieManagement()
                .disableAuthCaching()
                .disableRedirectHandling()
                .disableConnectionState()
                .build();
    }

    public static CloseableHttpClient getDefaultHttpClient() {
        return DEFAULT_HTTP_CLIENT;
    }
}
