// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.database;

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
public @interface MapperTable {
    /**
     * Alias for table
     */
    @AliasFor("table")
    String value() default "";

    /**
     * Database table name
     */
    @AliasFor("value")
    String table() default "";

    /**
     * Database table select columns,default * but not recommended
     */
    String columns() default "*";
}