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

import java.util.List;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = SpringBootArchetypeServer.class)
@MockDatabase(name = DataSourceConfig.BIZ)
public class BizAccountMapperTest extends BaseSpringTest {
    @Autowired
    private BizAccountMapper bizAccountMapper;

    @Test
    public void testFindAll() {
        List<BizAccount> accounts = bizAccountMapper.findAll();

        Assert.assertEquals(accounts.size(), 2);
        Assert.assertEquals(accounts.get(0).getId().intValue(), 1);
        Assert.assertEquals(accounts.get(0).getAccount(), "test1");
        Assert.assertEquals(accounts.get(0).getToken(), "token1");
    }
}
