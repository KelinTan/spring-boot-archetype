// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.mybatis;

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
    /**
     * Database name,unique
     */
    String name();

    /**
     * Database url, support spring value expression,like ${spring.datasource.url}
     */
    String databaseUrl();

    /**
     * Database userName,support spring value expression,like ${spring.datasource.userName}
     */

    String databaseUserName();

    /**
     * Database password,support spring value expression,like ${spring.datasource.password}
     */
    String databasePassword();

    /**
     * Mybatis mapper packages
     */
    String[] mapperPackages() default {};

    /**
     * Mybatis mapper xml locations
     */
    String mapperXmlLocation() default "";

    /**
     * Mybatis typeAlias package
     */
    String typeAliasesPackage() default "";

    /**
     * Database schema location for test
     */
    String schemaLocation() default "";

    /**
     * Database data location for test
     */
    String dataLocation() default "";
}
