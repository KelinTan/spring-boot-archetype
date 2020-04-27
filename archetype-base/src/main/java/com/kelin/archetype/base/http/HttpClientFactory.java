// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author Kelin Tan
 */
public class HttpClientFactory {
    private static CloseableHttpClient defaultHttpClient;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(32);
        connectionManager.setMaxTotal(1024);

        defaultHttpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .disableCookieManagement()
                .disableAuthCaching()
                .disableRedirectHandling()
                .disableConnectionState()
                .build();
    }

    public static CloseableHttpClient getDefaultHttpClient() {
        return defaultHttpClient;
    }
}
