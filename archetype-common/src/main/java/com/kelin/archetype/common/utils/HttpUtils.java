// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.utils;

import com.kelin.archetype.common.exception.RestExceptionFactory;
import com.kelin.archetype.common.http.HttpClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Preconditions;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Param;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Kelin Tan
 */
@Slf4j
public class HttpUtils {
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{([^}])*}");

    private static final String PATH_SEPARATOR = "/";

    public static final int DEFAULT_TIMEOUT_SECONDS = 5;

    public static boolean isOk(int status) {
        return status / 100 == 2;
    }

    public static boolean isBadRequest(int status) {
        return status / 100 == 4;
    }

    public static boolean isError(int status) {
        return status / 100 == 5;
    }

    public static String concatPath(String base, String relative) {
        if (base.endsWith(PATH_SEPARATOR)) {
            if (relative.startsWith(PATH_SEPARATOR)) {
                return base + relative.substring(1);
            } else {
                return base + relative;
            }
        } else {
            if (relative.startsWith(PATH_SEPARATOR)) {
                return base + relative;
            } else {
                return base + PATH_SEPARATOR + relative;
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

    public static URIBuilder safeURIBuilder(String host) {
        Preconditions.checkNotNullOrEmpty(host);

        try {
            return new URIBuilder(host);
        } catch (URISyntaxException e) {
            log.error("Invalid Host: {} " + host, e);
            throw RestExceptionFactory.toSystemException();
        }
    }

    public static URI safeURI(URIBuilder builder) {
        Preconditions.checkNotNull(builder);

        try {
            return builder.build();
        } catch (URISyntaxException e) {
            log.error("Invalid builder: {} " + builder.toString(), e);
            throw RestExceptionFactory.toSystemException();
        }
    }

    public static CloseableHttpResponse safeExecute(HttpRequestBase request) {
        Preconditions.checkNotNull(request);

        try {
            return HttpClientFactory.getDefaultHttpClient().execute(request);
        } catch (IOException e) {
            log.error("Invalid request: {} " + request.toString(), e);
            throw RestExceptionFactory.toSystemException();
        }
    }

    public static Response asyncExecuteResponse(Request request) {
        return safeAsyncResponse(HttpClientFactory.getAsyncHttpClient().executeRequest(request));
    }

    public static String safeEntityToString(HttpEntity httpEntity) {
        if (httpEntity == null) {
            return null;
        }
        try {
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            log.error("Io exception: ", e);
            return null;
        }
    }

    public static Response safeAsyncResponse(ListenableFuture<Response> future) {
        try {
            return future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Async response get exception: ", e);
        }

        return null;
    }

    public static String safeAsyncResponseBody(ListenableFuture<Response> future) {
        try {
            return future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS).getResponseBody(StandardCharsets.UTF_8);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Async response get exception: ", e);
        }

        return null;
    }

    public static List<NameValuePair> buildNameValuePairs(Map<String, Object> params) {
        if (MapUtils.isEmpty(params)) {
            return Collections.emptyList();
        }
        return params.entrySet().stream().map(
                (Function<Entry<String, Object>, NameValuePair>) param -> new BasicNameValuePair(param.getKey(),
                        param.getValue().toString())).collect(Collectors.toList());
    }

    public static List<Param> buildAsyncParams(Map<String, Object> params) {
        if (MapUtils.isEmpty(params)) {
            return Collections.emptyList();
        }
        return params.entrySet().stream().map(entry -> new Param(entry.getKey(), String.valueOf(entry.getValue())))
                .collect(Collectors.toList());
    }
}
