// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.rpc;

import com.alo7.archetype.json.JsonConverter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kelin Tan
 */
@Log4j2
public class RpcClientProxy implements InvocationHandler {
    private Class<?> clazz;
    private String endpoint;
    private Map<Method, String> requestUrlOnMethods = new HashMap<>();
    private Map<Method, Annotation[][]> methodParameterAnnotations = new HashMap<>();
    private Map<Method, RequestMethod> requestMethodOnMethods = new HashMap<>();

    RpcClientProxy(Class<?> clazz, String endpoint) {
        super();

        init(clazz, endpoint);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            RequestMethod requestMethod = requestMethodOnMethods.get(method);
            Map<String, Object> parameterMap = buildParameterMap(method, args);
            Map<String, Object> pathParameterMap = buildPathParameterMap(method, args);
            Map<String, Object> headerMap = buildRequestHeaderMap(method, args);
            Object requestBody = buildRequestBody(method, args);

            HttpRequestBase baseRequest = createRequest(
                    buildUrl(endpoint, requestUrlOnMethods.get(method), parameterMap,
                            pathParameterMap),
                    requestMethod, headerMap, requestBody);

            CloseableHttpResponse response = RpcUtils.getDefaultHttpClient().execute(baseRequest);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (!StringUtils.equalsIgnoreCase(method.getGenericReturnType().getTypeName(), "void")) {
                    return JsonConverter.deserialize(EntityUtils.toString(entity), method.getGenericReturnType());
                } else {
                    EntityUtils.consumeQuietly(entity);
                }
            } else {
                log.warn(String.format("%s request is not ok. response is %s", baseRequest.getURI().toString(),
                        response.toString()));
            }
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    private void init(Class<?> clazz, String endpoint) {
        this.clazz = clazz;
        this.endpoint = endpoint;
        Method[] methods = clazz.getMethods();
        Arrays.stream(methods).forEach(method -> {
            this.requestUrlOnMethods.put(method, getRequestUri(method));
            this.methodParameterAnnotations.put(method, method.getParameterAnnotations());
            this.requestMethodOnMethods.put(method, getRequestMethod(method));
        });
    }

    private HttpRequestBase createRequest(String uri, RequestMethod method, Map<String, Object> headerMap,
            Object requestBody) {
        HttpRequestBase requestBase;
        switch (method) {
            case GET:
                requestBase = new HttpGet(uri);
                break;
            case DELETE:
                requestBase = new HttpDelete(uri);
                break;
            case POST:
                HttpPost post = new HttpPost(uri);
                if (requestBody != null) {
                    post.setEntity(
                            new StringEntity(Objects.requireNonNull(JsonConverter.serialize(requestBody)),
                                    ContentType.APPLICATION_JSON));
                }
                requestBase = post;
                break;
            case PUT:
                HttpPut put = new HttpPut(uri);
                if (requestBody != null) {
                    put.setEntity(
                            new StringEntity(Objects.requireNonNull(JsonConverter.serialize(requestBody)),
                                    ContentType.APPLICATION_JSON));
                }
                requestBase = put;
                break;
            default:
                throw new RuntimeException("not support request method");
        }

        headerMap.forEach((key, value) -> requestBase.addHeader(key, value.toString()));
        return requestBase;
    }

    private String buildUrl(String endpoint, String requestUrlOnMethod,
            Map<String, Object> parameterMap,
            Map<String, Object> pathParameterMap) {
        String url = RpcUtils.concatPath(endpoint, requestUrlOnMethod);

        url = RpcUtils.replacePathVariable(url, pathParameterMap);
        url = RpcUtils.appendParams(url, parameterMap);

        return url;
    }

    private RequestMethod getRequestMethod(Method method) {
        HttpMethod annotation = AnnotationUtils.findAnnotation(method, HttpMethod.class);
        if (annotation == null) {
            throw new RuntimeException(
                    String.format("%s.%s缺少HttpMethod", clazz.getName(), method.getName()));
        } else {
            annotation.method();
        }
        return annotation.method();
    }

    private String getRequestUri(Method method) {
        HttpMethod annotation = AnnotationUtils.findAnnotation(method, HttpMethod.class);
        if (annotation == null) {
            throw new RuntimeException(String.format("%s.%s缺少HttpMethod", clazz.getName(), method.getName()));
        }
        return annotation.value();
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
