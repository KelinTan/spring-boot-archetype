// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

import com.kelin.archetype.common.exception.RpcExceptionFactory;
import com.kelin.archetype.common.http.AsyncHttpRequest;
import com.kelin.archetype.common.http.HttpConfig;
import com.kelin.archetype.common.http.HttpRequest;
import com.kelin.archetype.common.json.JsonConverter;
import com.kelin.archetype.common.log.LogMessageBuilder;
import com.kelin.archetype.common.utils.HttpUtils;
import com.kelin.archetype.common.utils.ProxyUtils;
import com.kelin.archetype.core.tracing.http.TracingHttpClientFactory;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import lombok.SneakyThrows;
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
import org.springframework.web.bind.annotation.RequestMapping;
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

    @SneakyThrows
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        HystrixCommand annotation = AnnotationUtils.findAnnotation(method, HystrixCommand.class);
        if (annotation != null) {
            return invokeHystrixRequest(annotation, proxy, method, args);
        } else {
            return invokeRequest(method, args);
        }
    }

    private Object invokeHystrixRequest(HystrixCommand annotation, Object proxy, Method method, Object[] args) {
        com.netflix.hystrix.HystrixCommand command = new com.netflix.hystrix.HystrixCommand(Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey(annotation.groupKey()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(annotation.commandKey()))
                .andCommandPropertiesDefaults(HystrixPropertiesManager
                        .initializeCommandProperties(Arrays.asList(annotation.commandProperties())))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(annotation.threadPoolKey()))
                .andThreadPoolPropertiesDefaults(HystrixPropertiesManager
                        .initializeThreadPoolProperties(Arrays.asList(annotation.threadPoolProperties())))) {
            @Override
            protected Object run() {
                return invokeRequest(method, args);
            }

            @Override
            protected Object getFallback() {
                if (!method.isDefault()) {
                    throw new RuntimeException(LogMessageBuilder.builder()
                            .message("RpcClient with hystrix method need default ")
                            .parameter("rpc", getRpcName(method.getName()))
                            .build());
                }
                log.error(LogMessageBuilder.builder()
                        .message("RpcClient fallback")
                        .parameter("rpc", getRpcName(method.getName()))
                        .build());
                return ProxyUtils.safeInvokeDefaultMethod(proxy, method, args);
            }
        };
        return command.execute();
    }

    private Object invokeRequest(Method method, Object[] args) {
        HttpConfig httpConfig = requestHttpConfigOnMethods.get(method);
        if (httpConfig.isAsync()) {
            return invokeAsyncRequest(method, httpConfig, args);
        } else {
            return invokeSyncRequest(method, httpConfig, args);
        }
    }

    private Object invokeAsyncRequest(Method method, HttpConfig httpConfig, Object[] args) {
        AsyncHttpRequest request = getAsyncHttpRequest(method, httpConfig, args);
        for (int i = 0; i < httpConfig.getRetryTimes(); i++) {
            Response response = request.execute(TracingHttpClientFactory.getTracingAsyncHttpClient());
            if (response == null) {
                throw RpcExceptionFactory.toException(clazz.getName(), method.getName(), LogMessageBuilder.builder()
                        .message("Rpc failed with empty response: " + method.getName())
                        .parameter("retryTimes", i)
                        .build());
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
                rpcErrorHandler.handle(status, body, clazz.getName(), method.getName());
                break;
            }
            log.warn(LogMessageBuilder.builder()
                    .message("Rpc failed")
                    .parameter("rpc", getRpcName(method.getName()))
                    .parameter("retryTimes", i)
                    .parameter("errorResponse", body)
                    .build());
            if (i == httpConfig.getRetryTimes() - 1) {
                rpcErrorHandler.handle(status, body, clazz.getName(), method.getName());
            }
        }

        //should not reach here
        return null;
    }

    private Object invokeSyncRequest(Method method, HttpConfig httpConfig, Object[] args) {
        HttpRequest httpRequest = getHttpRequest(method, httpConfig, args);
        for (int i = 0; i < httpConfig.getRetryTimes(); i++) {
            CloseableHttpResponse response = httpRequest.execute(TracingHttpClientFactory.getTracingHttpClient())
                    .response();
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
            } else {
                String responseBody = HttpUtils.safeEntityToString(entity);
                if (HttpUtils.isBadRequest(status)) {
                    //4xx error we do not retry
                    rpcErrorHandler.handle(status, responseBody, clazz.getName(), method.getName());
                    break;
                } else {
                    log.warn(LogMessageBuilder.builder()
                            .message("Rpc failed")
                            .parameter("rpc", getRpcName(method.getName()))
                            .parameter("retryTimes", i)
                            .parameter("errorResponse", responseBody)
                            .build());
                    if (i == httpConfig.getRetryTimes() - 1) {
                        rpcErrorHandler.handle(status, responseBody, clazz.getName(), method.getName());
                    }
                }
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
        HttpRequest request = createRequest(url, method, requestMethod, config, parameterMap,
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
        AsyncHttpRequest request = createAsyncRequest(url, method, requestMethod, config, parameterMap,
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
            RpcConfig rpcConfig = parseRpcConfig(method);
            this.requestUrlOnMethods.put(method, rpcConfig.getPath());

            this.methodParameterAnnotations.put(method, method.getParameterAnnotations());
            this.requestMethodOnMethods.put(method, rpcConfig.getMethod());
            this.requestHttpConfigOnMethods.put(method, rpcConfig.getHttpConfig());
        });
    }

    private String getRequestPathOnMethod(Method method, HttpMethod httpMethod) {
        if (StringUtils.isEmpty(httpMethod.value()) && StringUtils.isEmpty(httpMethod.path())) {
            throw new RuntimeException(LogMessageBuilder.builder()
                    .message("@HttpMethod need value or path")
                    .parameter("rpc", getRpcName(method.getName()))
                    .build());
        }
        if (StringUtils.isNotBlank(httpMethod.value())) {
            return httpMethod.value();
        }
        return httpMethod.path();
    }

    private HttpRequest createRequest(String uri, Method method, RequestMethod requestMethod, HttpConfig config,
            Map<String, Object> paramsMap,
            Map<String, Object> headerMap,
            Object requestBody) {
        headerMap.put(RpcConstants.RPC_NAME_HEADER, getRpcName(method.getName()));
        HttpRequest request = HttpRequest
                .host(uri)
                .withConfig(config)
                .withParams(paramsMap)
                .withHeaders(headerMap);
        if (requestBody != null) {
            request.withContent(JsonConverter.serialize(requestBody));
        }
        switch (requestMethod) {
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
                        .message("Unsupported http method")
                        .parameter("uri", uri)
                        .parameter("method", method)
                        .build());
        }
    }

    private AsyncHttpRequest createAsyncRequest(String uri, Method method, RequestMethod httpMethod, HttpConfig config,
            Map<String, Object> paramsMap,
            Map<String, Object> headerMap,
            Object requestBody) {
        headerMap.put(RpcConstants.RPC_NAME_HEADER, getRpcName(method.getName(), true));

        AsyncHttpRequest request = AsyncHttpRequest
                .host(uri)
                .withConfig(config)
                .withParams(paramsMap)
                .withHeaders(headerMap);
        if (requestBody != null) {
            request.withContent(JsonConverter.serialize(requestBody));
        }
        switch (httpMethod) {
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
                        .message("Unsupported http method")
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
            throw new RuntimeException(LogMessageBuilder.builder()
                    .message("RpcClient method need @HttpMethod ")
                    .parameter("rpc", getRpcName(method.getName()))
                    .build());
        }
        return annotation;
    }

    private RpcConfig parseRpcConfig(Method method) {
        HttpMethod httpMethod = AnnotationUtils.findAnnotation(method, HttpMethod.class);
        if (httpMethod != null) {
            return RpcConfig.builder()
                    .path(getRequestPathOnMethod(method, httpMethod))
                    .method(httpMethod.method())
                    .httpConfig(HttpConfig.builder()
                            .connectionTimeout(httpMethod.connectionTimeout())
                            .readTimeout(httpMethod.readTimeout())
                            .retryTimes(httpMethod.retryTimes())
                            .async(httpMethod.async())
                            .build())
                    .build();
        } else {
            RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);

            if (requestMapping == null) {
                throw new RuntimeException(LogMessageBuilder.builder()
                        .message("RpcClient method need @HttpMethod or @RequestMapping")
                        .parameter("rpc", getRpcName(method.getName()))
                        .build());
            }

            return RpcConfig.builder()
                    //only support first value
                    .path(requestMapping.value()[0])
                    .method(requestMapping.method()[0])
                    .httpConfig(HttpConfig.builder()
                            .connectionTimeout(RpcConstants.CONNECTION_TIMEOUT)
                            .readTimeout(RpcConstants.READ_TIMEOUT)
                            .retryTimes(RpcConstants.RETRY_TIMES)
                            .async(RpcConstants.DEFAULT_ASYNC)
                            .build())
                    .build();
        }
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

    private String getRpcName(String methodName) {
        return getRpcName(methodName, false);
    }

    private String getRpcName(String methodName, boolean async) {
        if (async) {
            return clazz.getName() + "." + methodName + ".async";
        } else {
            return clazz.getName() + "." + methodName;
        }
    }
}
