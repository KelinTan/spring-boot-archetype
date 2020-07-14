// Copyright 2020 Alo7 Inc. All rights reserved.
package com.kelin.archetype.common.mapper.primary

import com.kelin.archetype.base.mybatis.crud.BasicCrudMapper
import com.kelin.archetype.base.mybatis.crud.MapperTable
import com.kelin.archetype.common.entity.primary.User
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