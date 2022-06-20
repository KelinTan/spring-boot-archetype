// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.mapper.biz

import com.kelin.archetype.database.MapperTable
import com.kelin.archetype.database.entity.biz.YearSharding
import com.kelin.archetype.database.mapper.biz.YearShardingMapper.Companion.COLUMNS
import com.kelin.archetype.database.mapper.biz.YearShardingMapper.Companion.SHARDING_KEY
import com.kelin.archetype.database.mapper.biz.YearShardingMapper.Companion.TABLE
import com.kelin.archetype.database.sharding.YearShardingStrategy
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**
 * @author Kelin Tan
 */
@MapperTable(
    value = TABLE,
    columns = COLUMNS,
    sharding = true,
    shardingKey = SHARDING_KEY,
    shardingStrategy = YearShardingStrategy::class
)
interface YearShardingMapper {
    @Select("SELECT $COLUMNS FROM $TABLE WHERE sharding_time = #{shardingTime} and id = #{id}")
    fun findByTimeAndId(@Param("shardingTime") shardingTime: String, @Param("id") id: Int): YearSharding?

    companion object {
        const val COLUMNS = "id,sharding_time,data"
        const val TABLE = "year_sharding"
        const val SHARDING_KEY = "shardingTime"
    }
}