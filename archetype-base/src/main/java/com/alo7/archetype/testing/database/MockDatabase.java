// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.testing.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * to mock db in test
 *
 * @author Kelin Tan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(value = MockDatabases.class)
public @interface MockDatabase {
    /**
     * specific dataSource name,default get primary dataSource
     */
    String name() default "";

    /**
     * specific schema location if not use {@link FakeDataSource}
     */
    String schema() default "";

    /**
     * specific data location if not use {@link FakeDataSource}
     */
    String data() default "";

    /**
     * specific table to migrate data,default migrate all table in data config
     */
    String[] table() default {};
}
