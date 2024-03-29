// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.mapper.biz

import com.kelin.archetype.database.MapperTable
import com.kelin.archetype.database.entity.biz.BizAccount
import com.kelin.archetype.database.mybatis.crud.BaseCrudMapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

/**
 * @author Kelin Tan
 */
@MapperTable(value = BizAccountMapper.TABLE, columns = BizAccountMapper.COLUMNS)
interface BizAccountMapper : BaseCrudMapper<BizAccount> {
    @Select("SELECT $COLUMNS FROM $TABLE WHERE account = #{account}")
    fun findByAccount(account: String): BizAccount?

    @Update("UPDATE $TABLE SET token = #{token} WHERE id = #{id}")
    fun updateToken(
        @Param("id") id: Long?,
        @Param("token") token: String?
    )

    companion object {
        const val COLUMNS = "id,account,password,token,age,height,money,birth_date,verify"
        const val TABLE = "biz_account"
    }
}