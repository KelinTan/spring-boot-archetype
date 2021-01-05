// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.entity.biz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kelin Tan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YearSharding {
    private int id;
    private String shardingTime;
    private String data;
}
