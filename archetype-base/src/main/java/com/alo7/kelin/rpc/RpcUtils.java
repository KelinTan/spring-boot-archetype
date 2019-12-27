// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.rpc;

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
    private static final Pattern PATH_VARIAABLE_PATTERN = Pattern.compile("\\{([^}])*}");

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

    static String concatPath(String base, String relative) {
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

    static String replacePathVariable(String path, Map<String, Object> pathParameterMap) {
        Matcher matcher = PATH_VARIAABLE_PATTERN.matcher(path);
        while (matcher.find()) {
            String group = matcher.group();
            String pathParameter = group.substring(group.indexOf("{") + 1, group.indexOf("}"));
            if (!pathParameterMap.containsKey(pathParameter)) {
                throw new RuntimeException(String.format("%s 缺少对应的path参数%s", path, pathParameter));
            }

            path = path.replace(group, pathParameterMap.get(pathParameter).toString());
        }

        return path;
    }

    static String getProperty(String property) {
        Matcher matcher = PATH_VARIAABLE_PATTERN.matcher(property);
        if (matcher.find()) {
            String group = matcher.group();
            return group.substring(group.indexOf("{") + 1, group.indexOf("}"));
        }

        return property;
    }

    static String appendParams(String path, Map<String, Object> parameterMap) {
        if (MapUtils.isEmpty(parameterMap)) {
            return path;
        }
        if (path.contains("?")) {
            return path + buildQueryParams(parameterMap);
        } else {
            return path + "?" + buildQueryParams(parameterMap);
        }
    }

    private static String buildQueryParams(Map<String, Object> parameterMap) {
        if (MapUtils.isEmpty(parameterMap)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        parameterMap.forEach((s, o) -> stringBuilder.append(String.format("%s=%s", s, o.toString())));
        return stringBuilder.toString();
    }
}
