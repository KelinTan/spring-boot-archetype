// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.persistence.mapper.biz;

import com.alo7.archetype.persistence.entity.biz.BizAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Kelin Tan
 */
public interface BizAccountMapper {
    List<BizAccount> findAll();

    BizAccount findByAccount(String account);

    void updateToken(@Param("id") Long id, @Param("token") String token);
}
