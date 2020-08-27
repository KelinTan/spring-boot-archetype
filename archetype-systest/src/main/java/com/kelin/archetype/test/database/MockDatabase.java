// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.test.database;

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
     * specific tables to migrate data,default migrate all tables in data config
     */
    String[] tables() default {};

    /**
     * specific tables {@link MapperTable} to migrate data,default migrate all tables in data config
     */
    Class<?>[] mappers() default {};
}
