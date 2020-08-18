// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.http;

import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Preconditions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Kelin Tan
 */
public class HttpUtils {
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{([^}])*}");

    public static boolean isHttpOk(int status) {
        return status / 100 == 2;
    }

    public static boolean isHttpBadRequest(int status) {
        return status / 100 == 4;
    }

    public static boolean isHttpErrorRequest(int status) {
        return status / 100 == 5;
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

    static String formatUrlWithParams(String path, Map<String, Object> parameterMap) {
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

    static URIBuilder safeURIBuilder(String host) {
        Preconditions.checkNotNullOrEmpty(host);

        try {
            return new URIBuilder(host);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static URI safeURI(URIBuilder builder) {
        Preconditions.checkNotNull(builder);

        try {
            return builder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static CloseableHttpResponse safeExecute(HttpRequestBase request) {
        Preconditions.checkNotNull(request);

        try {
            return HttpClientFactory.getDefaultHttpClient().execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String safeEntityToString(HttpEntity httpEntity) {
        if (httpEntity == null) {
            return null;
        }
        try {
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            return null;
        }
    }

    static List<NameValuePair> buildNameValuePairs(Map<String, Object> params) {
        if (MapUtils.isEmpty(params)) {
            return Collections.emptyList();
        }
        return params.entrySet().stream().map(
                (Function<Entry<String, Object>, NameValuePair>) param -> new BasicNameValuePair(param.getKey(),
                        param.getValue().toString())).collect(Collectors.toList());
    }
}
