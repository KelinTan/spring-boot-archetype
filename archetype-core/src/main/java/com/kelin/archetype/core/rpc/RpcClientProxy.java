// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

import com.kelin.archetype.common.http.AsyncHttpRequest;
import com.kelin.archetype.common.http.HttpConfig;
import com.kelin.archetype.common.http.HttpRequest;
import com.kelin.archetype.common.http.HttpUtils;
import com.kelin.archetype.common.json.JsonConverter;
import com.kelin.archetype.common.log.LogMessageBuilder;
import com.kelin.archetype.common.rest.exception.RestExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.asynchttpclient.Response;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kelin Tan
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private Class<?> clazz;
    private String endpoint;
    private RpcErrorHandler rpcErrorHandler;
    private final Map<Method, String> requestUrlOnMethods = new HashMap<>();
    private final Map<Method, Annotation[][]> methodParameterAnnotations = new HashMap<>();
    private final Map<Method, RequestMethod> requestMethodOnMethods = new HashMap<>();
    private final Map<Method, HttpConfig> requestHttpConfigOnMethods = new HashMap<>();
    private final Map<Method, HttpRequest> methodHttpRequestMap = new ConcurrentHashMap<>();
    private final Map<Method, AsyncHttpRequest> methodAsyncHttpRequestMap = new ConcurrentHashMap<>();

    RpcClientProxy(Class<?> clazz, String endpoint, RpcErrorHandler rpcErrorHandler) {
        super();

        init(clazz, endpoint, rpcErrorHandler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        HttpConfig httpConfig = requestHttpConfigOnMethods.get(method);
        if (httpConfig.isAsync()) {
            return invokeAsyncRequest(method, httpConfig, args);
        } else {
            return invokeRequest(method, httpConfig, args);
        }
    }

    private Object invokeAsyncRequest(Method method, HttpConfig httpConfig, Object[] args) {
        AsyncHttpRequest request = getAsyncHttpRequest(method, httpConfig, args);
        for (int i = 0; i < httpConfig.getRetryTimes(); i++) {
            Response response = HttpUtils.safeAsyncResponse(request.execute().response());
            if (response == null) {
                log.warn(LogMessageBuilder.builder()
                        .message("Rpc failed with empty response: " + method.getName())
                        .parameter("retryTimes", i)
                        .build());
                throw RestExceptionFactory.toRpcException();
            }
            int status = response.getStatusCode();
            String body = response.getResponseBody();
            if (HttpUtils.isOk(status)) {
                if (!StringUtils.equalsIgnoreCase(method.getGenericReturnType().getTypeName(), "void")) {
                    return JsonConverter.deserialize(body, method.getGenericReturnType());
                }
                break;
            } else if (HttpUtils.isBadRequest(status)) {
                //4xx error we do not retry
                rpcErrorHandler.handle(status, body);
                break;
            }
            log.warn(LogMessageBuilder.builder()
                    .message("Rpc failed: " + method.getName())
                    .parameter("retryTimes", i)
                    .parameter("errorResponse", body)
                    .build());
            if (i == httpConfig.getRetryTimes() - 1) {
                rpcErrorHandler.handle(status, body);
            }
        }

        //should not reach here
        return null;
    }

    private Object invokeRequest(Method method, HttpConfig httpConfig, Object[] args) {
        HttpRequest request = getHttpRequest(method, httpConfig, args);
        for (int i = 0; i < httpConfig.getRetryTimes(); i++) {
            CloseableHttpResponse response = request.execute().response();
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (HttpUtils.isOk(status)) {
                if (!StringUtils.equalsIgnoreCase(method.getGenericReturnType().getTypeName(), "void")) {
                    return JsonConverter.deserialize(HttpUtils.safeEntityToString(entity),
                            method.getGenericReturnType());
                } else {
                    EntityUtils.consumeQuietly(entity);
                }
                break;
            } else if (HttpUtils.isBadRequest(status)) {
                //4xx error we do not retry
                rpcErrorHandler.handle(status, HttpUtils.safeEntityToString(entity));
                break;
            }
            log.warn(LogMessageBuilder.builder()
                    .message("Rpc failed: " + method.getName())
                    .parameter("retryTimes", i)
                    .parameter("errorResponse", HttpUtils.safeEntityToString(entity))
                    .build());
            if (i == httpConfig.getRetryTimes() - 1) {
                rpcErrorHandler.handle(status, HttpUtils.safeEntityToString(entity));
            }
        }

        //should not reach here
        return null;
    }

    private HttpRequest getHttpRequest(Method method, HttpConfig config, Object[] args) {
        if (methodHttpRequestMap.containsKey(method)) {
            return methodHttpRequestMap.get(method);
        }
        RequestMethod requestMethod = requestMethodOnMethods.get(method);
        Map<String, Object> parameterMap = buildParameterMap(method, args);
        Map<String, Object> pathParameterMap = buildPathParameterMap(method, args);
        Map<String, Object> headerMap = buildRequestHeaderMap(method, args);
        Object requestBody = buildRequestBody(method, args);

        String url = buildUrl(endpoint, requestUrlOnMethods.get(method), pathParameterMap);
        HttpRequest request = createRequest(url, requestMethod, config, parameterMap,
                headerMap, requestBody);
        methodHttpRequestMap.putIfAbsent(method, request);
        return request;
    }

    private AsyncHttpRequest getAsyncHttpRequest(Method method, HttpConfig config, Object[] args) {
        if (methodAsyncHttpRequestMap.containsKey(method)) {
            return methodAsyncHttpRequestMap.get(method);
        }
        RequestMethod requestMethod = requestMethodOnMethods.get(method);
        Map<String, Object> parameterMap = buildParameterMap(method, args);
        Map<String, Object> pathParameterMap = buildPathParameterMap(method, args);
        Map<String, Object> headerMap = buildRequestHeaderMap(method, args);
        Object requestBody = buildRequestBody(method, args);

        String url = buildUrl(endpoint, requestUrlOnMethods.get(method), pathParameterMap);
        AsyncHttpRequest request = createAsyncRequest(url, requestMethod, config, parameterMap,
                headerMap, requestBody);
        methodAsyncHttpRequestMap.putIfAbsent(method, request);
        return request;
    }

    private void init(Class<?> clazz, String endpoint, RpcErrorHandler rpcErrorHandler) {
        this.clazz = clazz;
        this.endpoint = endpoint;
        this.rpcErrorHandler = rpcErrorHandler;
        Method[] methods = clazz.getMethods();
        Arrays.stream(methods).forEach(method -> {
            HttpMethod httpMethod = getHttpMethod(method);
            this.requestUrlOnMethods.put(method, getRequestPathOnMethod(httpMethod));

            this.methodParameterAnnotations.put(method, method.getParameterAnnotations());
            this.requestMethodOnMethods.put(method, httpMethod.method());
            this.requestHttpConfigOnMethods.put(method, HttpConfig.builder()
                    .connectionTimeout(httpMethod.connectionTimeout())
                    .readTimeout(httpMethod.readTimeout())
                    .retryTimes(httpMethod.retryTimes())
                    .async(httpMethod.async())
                    .build());
        });
    }

    private String getRequestPathOnMethod(HttpMethod httpMethod) {
        if (StringUtils.isEmpty(httpMethod.value()) && StringUtils.isEmpty(httpMethod.path())) {
            throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder()
                    .message("@HttpMethod need value or path")
                    .parameter("clazz", clazz.getName())
                    .parameter("method", httpMethod)
                    .build());
        }
        if (StringUtils.isNotBlank(httpMethod.value())) {
            return httpMethod.value();
        }
        return httpMethod.path();
    }

    private HttpRequest createRequest(String uri, RequestMethod method, HttpConfig config,
            Map<String, Object> paramsMap,
            Map<String, Object> headerMap,
            Object requestBody) {
        HttpRequest request = HttpRequest
                .host(uri)
                .withConfig(config)
                .withParams(paramsMap)
                .withHeaders(headerMap);
        if (requestBody != null) {
            request.withContent(JsonConverter.serialize(requestBody));
        }
        switch (method) {
            case GET:
                return request.get();
            case DELETE:
                return request.delete();
            case POST:
                return request.post();
            case PUT:
                return request.put();
            default:
                throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder()
                        .message("UnSupported http method")
                        .parameter("uri", uri)
                        .parameter("method", method)
                        .build());
        }
    }

    private AsyncHttpRequest createAsyncRequest(String uri, RequestMethod method, HttpConfig config,
            Map<String, Object> paramsMap,
            Map<String, Object> headerMap,
            Object requestBody) {
        AsyncHttpRequest request = AsyncHttpRequest
                .host(uri)
                .withConfig(config)
                .withParams(paramsMap)
                .withHeaders(headerMap);
        if (requestBody != null) {
            request.withContent(JsonConverter.serialize(requestBody));
        }
        switch (method) {
            case GET:
                return request.get();
            case DELETE:
                return request.delete();
            case POST:
                return request.post();
            case PUT:
                return request.put();
            default:
                throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder()
                        .message("UnSupported http method")
                        .parameter("uri", uri)
                        .parameter("method", method)
                        .build());
        }
    }

    private String buildUrl(String endpoint, String requestUrlOnMethod, Map<String, Object> pathParameterMap) {
        String url = HttpUtils.concatPath(endpoint, requestUrlOnMethod);
        return HttpUtils.formatUrlWithPathParams(url, pathParameterMap);
    }

    private HttpMethod getHttpMethod(Method method) {
        HttpMethod annotation = AnnotationUtils.findAnnotation(method, HttpMethod.class);
        if (annotation == null) {
            throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder()
                    .message("RpcClient method need @HttpMethod ")
                    .parameter("clazz", clazz.getName())
                    .parameter("method", method.getName())
                    .build());
        }
        return annotation;
    }

    private Map<String, Object> buildParameterMap(Method method, Object[] args) {
        Map<String, Object> map = new HashMap<>();

        Annotation[][] parameterAnnotations = methodParameterAnnotations.get(method);
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestParam) {
                    map.put(((RequestParam) annotation).value(), args[i]);
                }
            }
        }
        return map;
    }

    private Map<String, Object> buildPathParameterMap(Method method, Object[] args) {
        Map<String, Object> map = new HashMap<>();

        Annotation[][] parameterAnnotations = methodParameterAnnotations.get(method);
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof PathVariable) {
                    map.put(((PathVariable) annotation).value(), args[i]);
                }
            }
        }
        return map;
    }

    private Map<String, Object> buildRequestHeaderMap(Method method, Object[] args) {
        Map<String, Object> map = new HashMap<>();

        Annotation[][] parameterAnnotations = methodParameterAnnotations.get(method);
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestHeader) {
                    map.put(((RequestHeader) annotation).value(), args[i]);
                }
            }
        }
        return map;
    }

    private Object buildRequestBody(Method method, Object[] args) {
        Annotation[][] parameterAnnotations = methodParameterAnnotations.get(method);
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestBody) {
                    return args[i];
                }
            }
        }
        return null;
    }
}
