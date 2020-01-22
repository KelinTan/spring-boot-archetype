// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.base.testing.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kelin Tan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MockDatabases {
    MockDatabase[] value();
}
