// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kelin Tan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcClient {
    /**
     * Rpc endpoint,support spring value expression,like ${user.client.endpoint}
     */
    String endpoint();

    /**
     * Rpc errorHandler when rpc status is 4xx or 5xx
     */
    Class<? extends RpcErrorHandler> errorHandler() default DefaultRpcErrorHandler.class;
}
