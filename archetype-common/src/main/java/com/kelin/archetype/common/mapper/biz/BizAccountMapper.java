// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.mapper.biz;

import com.kelin.archetype.base.mybatis.crud.BasicCrudMapper;
import com.kelin.archetype.base.mybatis.crud.MapperTable;
import com.kelin.archetype.common.entity.biz.BizAccount;
import org.apache.ibatis.annotations.Param;

/**
 * @author Kelin Tan
 */
@MapperTable(value = "biz_account", columns = "id,account,password,token,age,height,money,birth_date,verify")
public interface BizAccountMapper extends BasicCrudMapper<BizAccount> {
    BizAccount findByAccount(String account);

    void updateToken(@Param("id") Long id, @Param("token") String token);
}
