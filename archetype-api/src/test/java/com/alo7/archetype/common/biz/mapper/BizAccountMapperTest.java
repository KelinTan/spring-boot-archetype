// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.common.biz.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.alo7.archetype.api.SpringBootArchetypeServer;
import com.alo7.archetype.api.config.DataSourceConfig;
import com.alo7.archetype.base.testing.BaseSpringTest;
import com.alo7.archetype.base.testing.database.MockDatabase;
import com.alo7.archetype.common.entity.biz.BizAccount;
import com.alo7.archetype.common.mapper.biz.BizAccountMapper;
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
        assertNull(bizAccountMapper.findByAccount("noAccount"));
        BizAccount account = bizAccountMapper.findByAccount("test1");

        assertNotNull(account);
        assertEquals("test1", account.getAccount());
        assertEquals("password1", account.getPassword());
        assertEquals("token1", account.getToken());
    }

    @Test
    public void testUpdateToken() {
        bizAccountMapper.updateToken(1L, "updateToken");

        assertEquals("updateToken", bizAccountMapper.findOne(1L).getToken());
    }
}
