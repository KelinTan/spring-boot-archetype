// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.database.sharding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Kelin Tan
 */
public class YearShardingStrategy implements ShardingStrategy {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public String getShardingTable(String baseTable, String shardingValue, int tableCount) {
        //may be support more format
        LocalDate date = LocalDate.parse(shardingValue, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));

        return baseTable + TABLE_DELIMITER + date.getYear();
    }
}
