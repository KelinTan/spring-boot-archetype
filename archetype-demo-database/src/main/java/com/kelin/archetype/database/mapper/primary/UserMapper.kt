// Copyright 2020 Kelin Inc. All rights reserved.
package com.kelin.archetype.database.mapper.primary

import com.kelin.archetype.common.database.MapperTable
import com.kelin.archetype.core.mybatis.crud.BasicCrudMapper
import com.kelin.archetype.database.entity.primary.User
import org.apache.ibatis.annotations.Select

/**
 * @author Kelin Tan
 */
@MapperTable(value = UserMapper.TABLE, columns = UserMapper.COLUMNS)
interface UserMapper : BasicCrudMapper<User> {
    @Select("SELECT $COLUMNS FROM $TABLE WHERE user_name = #{name}")
    fun findByName(name: String): List<User>

    companion object {
        const val TABLE = "user"
        const val COLUMNS = "id,user_name"
    }
}