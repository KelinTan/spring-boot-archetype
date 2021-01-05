// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.database.sharding;

/**
 * @author Kelin Tan
 */
public interface ShardingStrategy {
    String TABLE_DELIMITER = "_";

    /**
     * Get sharding table from baseTable & shardingValue & tableCount
     */
    String getShardingTable(String baseTable, String shardingValue, int tableCount);
}
