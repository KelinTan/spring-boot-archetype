// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.testing.database;

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
