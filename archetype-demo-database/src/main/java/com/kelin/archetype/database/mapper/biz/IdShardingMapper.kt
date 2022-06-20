// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.mapper.biz

import com.kelin.archetype.database.MapperTable
import com.kelin.archetype.database.entity.biz.IdSharding
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

/**
 * @author Kelin Tan
 */
@MapperTable(value = IdShardingMapper.TABLE, columns = IdShardingMapper.COLUMNS, sharding = true, count = 2)
interface IdShardingMapper {
    @Select("SELECT $COLUMNS FROM $TABLE WHERE id = #{id}")
    fun findById(@Param("id") id: Long): IdSharding?

    @Insert("INSERT INTO $TABLE($COLUMNS) VALUES (#{id},#{data})")
    fun insert(@Param("id") id: Long, @Param("data") data: String)

    @Update("UPDATE $TABLE SET data = #{data} WHERE id = #{id}")
    fun updateById(@Param("id") id: Long, @Param("data") data: String)

    @Delete("DELETE FROM $TABLE WHERE id = #{id}")
    fun deleteById(@Param("id") id: Long)

    companion object {
        const val COLUMNS = "id,data"
        const val TABLE = "id_sharding"
    }
}