// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.test.database;

import com.kelin.archetype.common.database.FakeDataSource;
import com.kelin.archetype.common.database.MapperTable;

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
     * specific database singleton bean name
     */
    String name() default "";

    /**
     * specific schema location if not use {@link FakeDataSource#getSchemaLocation()}
     */
    String schema() default "";

    /**
     * ø specific data location if not use {@link FakeDataSource#getDataLocation()}
     */
    String data() default "";

    /**
     * specific tables to reset data,default migrate all tables in database
     */
    String[] tables() default {};

    /**
     * specific mappers to reset data,default migrate all tables in database, {@link MapperTable#table()}
     */
    Class<?>[] mappers() default {};
}
