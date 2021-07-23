// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.service.impl;

import com.kelin.archetype.api.service.BizService;
import com.kelin.archetype.database.config.BizDatabase;
import com.kelin.archetype.database.config.PrimaryDatabase;
import com.kelin.archetype.database.entity.biz.BizAccount;
import com.kelin.archetype.database.entity.primary.User;
import com.kelin.archetype.database.mapper.biz.BizAccountMapper;
import com.kelin.archetype.database.mapper.primary.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kelin Tan
 */
@Service
@ManagedResource
public class BizServiceImpl implements BizService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BizAccountMapper bizAccountMapper;

    @Override
    @ManagedOperation
    @Transactional(transactionManager = PrimaryDatabase.NAME + ".transactionManager")
    public void updateWithPrimaryTransaction() {
        User update = new User();
        update.setId(1L);
        update.setUserName("rollback");
        userMapper.updateSelective(update);

        throw new RuntimeException();
    }

    @Override
    @Transactional(transactionManager = BizDatabase.NAME + ".transactionManager")
    public void updateWithBizTransaction() {
        BizAccount bizAccountUpdate = new BizAccount();
        bizAccountUpdate.setId(1L);
        bizAccountUpdate.setAccount("rollback");
        bizAccountMapper.updateSelective(bizAccountUpdate);

        throw new RuntimeException();
    }

    @Override
    @Transactional(transactionManager = PrimaryDatabase.NAME + ".transactionManager")
    public void updateWithMultipleTransaction() {
        User update = new User();
        update.setId(1L);
        update.setUserName("rollback");
        userMapper.updateSelective(update);

        BizAccount bizAccountUpdate = new BizAccount();
        bizAccountUpdate.setId(1L);
        bizAccountUpdate.setAccount("rollbackFail");
        bizAccountMapper.updateSelective(bizAccountUpdate);

        throw new RuntimeException();
    }

    @Override
    public User findCacheUserById(Long id) {
        return findUserById(id);
    }

    @Override
    public User findUserById(Long id) {
        return userMapper.findOne(id);
    }
}
