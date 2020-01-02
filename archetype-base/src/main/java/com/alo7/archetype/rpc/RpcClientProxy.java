// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rpc;

import com.alo7.archetype.http.HttpRequest;
import com.alo7.archetype.http.HttpUtils;
import com.alo7.archetype.json.JsonConverter;
import com.alo7.archetype.log.LogMessageBuilder;
import com.alo7.archetype.rest.exception.RestExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
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

/**
 * @author Kelin Tan
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private Class<?> clazz;
    private String endpoint;
    private RpcErrorHandler rpcErrorHandler;
    private Map<Method, String> requestUrlOnMethods = new HashMap<>();
    private Map<Method, Annotation[][]> methodParameterAnnotations = new HashMap<>();
    private Map<Method, RequestMethod> requestMethodOnMethods = new HashMap<>();

    RpcClientProxy(Class<?> clazz, String endpoint, RpcErrorHandler rpcErrorHandler) {
        super();

        init(clazz, endpoint, rpcErrorHandler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            RequestMethod requestMethod = requestMethodOnMethods.get(method);
            Map<String, Object> parameterMap = buildParameterMap(method, args);
            Map<String, Object> pathParameterMap = buildPathParameterMap(method, args);
            Map<String, Object> headerMap = buildRequestHeaderMap(method, args);
            Object requestBody = buildRequestBody(method, args);

            String url = buildUrl(endpoint, requestUrlOnMethods.get(method), pathParameterMap);
            HttpRequest request = createRequest(url, requestMethod, parameterMap, headerMap, requestBody);

            CloseableHttpResponse response = request.execute().response();
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (HttpUtils.isHttpOk(status)) {
                if (!StringUtils.equalsIgnoreCase(method.getGenericReturnType().getTypeName(), "void")) {
                    return JsonConverter.deserialize(EntityUtils.toString(entity), method.getGenericReturnType());
                } else {
                    EntityUtils.consumeQuietly(entity);
                }
            } else {
                rpcErrorHandler.handle(status, EntityUtils.toString(entity));
            }
        } catch (Exception e) {
            log.error(LogMessageBuilder.builder()
                    .message("rpc client proxy invoke error")
                    .parameter("method", method)
                    .parameter("args", args)
                    .build(), e);
            throw RestExceptionFactory.toSystemException();
        }
        return null;
    }

    private void init(Class<?> clazz, String endpoint, RpcErrorHandler rpcErrorHandler) {
        this.clazz = clazz;
        this.endpoint = endpoint;
        this.rpcErrorHandler = rpcErrorHandler;
        Method[] methods = clazz.getMethods();
        Arrays.stream(methods).forEach(method -> {
            this.requestUrlOnMethods.put(method, getRequestUri(method));
            this.methodParameterAnnotations.put(method, method.getParameterAnnotations());
            this.requestMethodOnMethods.put(method, getRequestMethod(method));
        });
    }

    private HttpRequest createRequest(String uri, RequestMethod method, Map<String, Object> paramsMap,
            Map<String, Object> headerMap,
            Object requestBody) {
        HttpRequest request = HttpRequest
                .withPath(uri)
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
                throw new RuntimeException(LogMessageBuilder.builder()
                        .message("UnSupported http Method")
                        .parameter("uri", uri)
                        .parameter("method", method)
                        .build());
        }
    }

    private String buildUrl(String endpoint, String requestUrlOnMethod, Map<String, Object> pathParameterMap) {
        String url = HttpUtils.concatPath(endpoint, requestUrlOnMethod);
        return HttpUtils.formatUrlWithPathParams(url, pathParameterMap);
    }

    private RequestMethod getRequestMethod(Method method) {
        HttpMethod annotation = getHttpMethod(method);
        return annotation.method();
    }

    private String getRequestUri(Method method) {
        HttpMethod annotation = getHttpMethod(method);
        return annotation.value();
    }

    private HttpMethod getHttpMethod(Method method) {
        HttpMethod annotation = AnnotationUtils.findAnnotation(method, HttpMethod.class);
        if (annotation == null) {
            throw new RuntimeException(LogMessageBuilder.builder()
                    .message("@RpcClient method 缺少 HttpMethod:")
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
