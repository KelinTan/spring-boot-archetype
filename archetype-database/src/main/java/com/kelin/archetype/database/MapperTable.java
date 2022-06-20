// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.database;

import com.kelin.archetype.database.sharding.DefaultShardingStrategy;
import com.kelin.archetype.database.sharding.ShardingStrategy;

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
     * Database table name
     */
    String value() default "";

    /**
     * Database table select columns,default * but not recommended
     */
    String columns() default "*";

    /**
     * Table sharding
     */
    boolean sharding() default false;

    /**
     * Table sharding key default id
     */
    String shardingKey() default "id";

    /**
     * Sharding Table count only when sharding = true
     */
    int count() default 0;

    /**
     * Sharding strategy
     */
    Class<? extends ShardingStrategy> shardingStrategy() default DefaultShardingStrategy.class;
}