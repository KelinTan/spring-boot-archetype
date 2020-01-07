// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.testing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kelin Tan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(value = MockDatabases.class)
public @interface MockDatabase {
    String dataSource() default "";

    String schema() default "";

    String data() default "";

    String[] table() default {};
}
