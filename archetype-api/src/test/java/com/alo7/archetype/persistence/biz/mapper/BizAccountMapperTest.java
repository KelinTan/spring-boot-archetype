// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.persistence.biz.mapper;

import com.alo7.archetype.SpringBootArchetypeServer;
import com.alo7.archetype.config.DataSourceConfig;
import com.alo7.archetype.persistence.entity.biz.BizAccount;
import com.alo7.archetype.persistence.mapper.biz.BizAccountMapper;
import com.alo7.archetype.testing.BaseSpringTest;
import com.alo7.archetype.testing.database.MockDatabase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = SpringBootArchetypeServer.class)
@MockDatabase(name = DataSourceConfig.BIZ)
public class BizAccountMapperTest extends BaseSpringTest {
    @Autowired
    private BizAccountMapper bizAccountMapper;

    @Test
    public void testFindByAccount() {
        Assert.assertNull(bizAccountMapper.findByAccount("noAccount"));
        BizAccount account = bizAccountMapper.findByAccount("test1");

        Assert.assertNotNull(account);
        Assert.assertEquals("test1", account.getAccount());
        Assert.assertEquals("password1", account.getPassword());
        Assert.assertEquals("token1", account.getToken());
    }

    @Test
    public void testUpdateToken() {
        bizAccountMapper.updateToken(1L, "updateToken");

        Assert.assertEquals("updateToken", bizAccountMapper.findOne(1L).getToken());
    }
}
