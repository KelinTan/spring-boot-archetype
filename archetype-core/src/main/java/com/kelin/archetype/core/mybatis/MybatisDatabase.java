// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.mybatis;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kelin Tan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MybatisDatabaseRegistrar.class)
public @interface MybatisDatabase {
    String name();

    String databaseUrl();

    String databaseUserName();

    String databasePassword();

    String[] mapperPackages() default {};

    String mapperXmlLocation() default "";

    String typeAliasesPackage() default "";

    String schemaLocation() default "";

    String dataLocation() default "";
}
