// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kelin Tan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpMethod {
    /**
     * alias for path
     */
    @AliasFor("path")
    String value() default "";

    /**
     * Http path,join with {@link RpcClient#endpoint()}
     */
    @AliasFor("value")
    String path() default "";

    /**
     * @see org.springframework.web.bind.annotation.RequestMethod
     */
    RequestMethod method() default RequestMethod.GET;

    /**
     * Http request connect timeout milliseconds default 5000
     */
    int connectionTimeout() default 5000;

    /**
     * Http request read timeout milliseconds default 5000
     */
    int readTimeout() default 5000;

    /**
     * Http Request retry times
     */
    int retryTimes() default 1;

    /**
     * Use AsyncHttpClient to perform http request
     */
    boolean async() default false;
}
