// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.persistence.mapper.biz;

import com.alo7.archetype.persistence.entity.biz.BizAccount;

import java.util.List;

/**
 * @author Kelin Tan
 */
public interface BizAccountMapper {
    List<BizAccount> findAll();
}
