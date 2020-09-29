// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kelin Tan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RpcClientRegistrar.class)
public @interface RpcClientScan {
    /**
     * alias for basePackages
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * Rpc client scan basePackages
     */
    @AliasFor("value")
    String[] basePackages() default {};
}
