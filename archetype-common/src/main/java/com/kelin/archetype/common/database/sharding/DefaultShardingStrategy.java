// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.database.sharding;

import com.kelin.archetype.common.log.LogMessageBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Kelin Tan
 */
public class DefaultShardingStrategy implements ShardingStrategy {
    @Override
    public String getShardingTable(String baseTable, String shardingValue, int tableCount) {
        if (!StringUtils.isNumeric(shardingValue)) {
            throw new RuntimeException(LogMessageBuilder.builder()
                    .message("DefaultShardingStrategy shardingKey need number")
                    .parameter("table", baseTable)
                    .parameter("key", shardingValue)
                    .build());
        }
        return baseTable + getShardingTableDelimiter() + Integer.parseInt(shardingValue) % tableCount;
    }
}
