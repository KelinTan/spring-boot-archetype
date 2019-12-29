// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rpc;

import org.apache.commons.collections4.MapUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kelin Tan
 */
class RpcUtils {
    private static CloseableHttpClient defaultHttpClient;
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{([^}])*}");

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

    static CloseableHttpClient getDefaultHttpClient() {
        return defaultHttpClient;
    }

    public static String concatPath(String base, String relative) {
        if (base.endsWith("/")) {
            if (relative.startsWith("/")) {
                return base + relative.substring(1);
            } else {
                return base + relative;
            }
        } else {
            if (relative.startsWith("/")) {
                return base + relative;
            } else {
                return base + "/" + relative;
            }
        }
    }

    public static String formatUrlWithPathParams(String path, Map<String, Object> pathParameterMap) {
        Matcher matcher = PATH_VARIABLE_PATTERN.matcher(path);
        while (matcher.find()) {
            String group = matcher.group();
            String pathParameter = group.substring(group.indexOf("{") + 1, group.indexOf("}"));
            if (pathParameterMap.containsKey(pathParameter)) {
                path = path.replace(group, pathParameterMap.get(pathParameter).toString());
            }
        }
        return path;
    }

    public static String formatUrlWithParams(String path, Map<String, Object> parameterMap) {
        if (MapUtils.isEmpty(parameterMap)) {
            return path;
        }
        if (path.contains("?")) {
            return path + "&" + buildQueryParams(parameterMap);
        } else {
            return path + "?" + buildQueryParams(parameterMap);
        }
    }

    private static String buildQueryParams(Map<String, Object> parameterMap) {
        StringBuilder stringBuilder = new StringBuilder();
        parameterMap.forEach((s, o) -> stringBuilder.append(String.format("%s=%s&", s, o.toString())));
        return stringBuilder.substring(0, stringBuilder.lastIndexOf("&"));
    }
}
