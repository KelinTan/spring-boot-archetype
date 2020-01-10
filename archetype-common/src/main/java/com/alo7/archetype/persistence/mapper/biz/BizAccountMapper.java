// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.persistence.mapper.biz;

import com.alo7.archetype.mybatis.crud.BasicCrudMapper;
import com.alo7.archetype.mybatis.crud.MapperTable;
import com.alo7.archetype.persistence.entity.biz.BizAccount;
import org.apache.ibatis.annotations.Param;

/**
 * @author Kelin Tan
 */
@MapperTable(value = "biz_account", columns = "id,account,password,token,age,height,money,birth_date,verify")
public interface BizAccountMapper extends BasicCrudMapper<BizAccount> {
    BizAccount findByAccount(String account);

    void updateToken(@Param("id") Long id, @Param("token") String token);
}
