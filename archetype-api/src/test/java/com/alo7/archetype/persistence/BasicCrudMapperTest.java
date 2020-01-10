// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.persistence;

import com.alo7.archetype.SpringBootArchetypeServer;
import com.alo7.archetype.config.DataSourceConfig;
import com.alo7.archetype.persistence.entity.biz.BizAccount;
import com.alo7.archetype.persistence.mapper.biz.BizAccountMapper;
import com.alo7.archetype.testing.BaseSpringTest;
import com.alo7.archetype.testing.database.MockDatabase;
import com.alo7.archetype.testing.database.MockDatabases;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = SpringBootArchetypeServer.class)
@MockDatabases
        (@MockDatabase(name = DataSourceConfig.BIZ, tables = "biz_account"))
public class BasicCrudMapperTest extends BaseSpringTest {
    @Autowired
    private BizAccountMapper accountMapper;

    @Test
    public void testBasicFindOne() {
        BizAccount account = accountMapper.findOne(1L);

        Assert.assertEquals(1, account.getId().intValue());
        Assert.assertEquals("test1", account.getAccount());
        Assert.assertEquals("password1", account.getPassword());
        Assert.assertEquals("token1", account.getToken());
    }

    @Test
    public void testBasicInsert() {
        BizAccount insertEntity = BizAccount.builder()
                .account("testInsert")
                .password("testPwd")
                .token("testToken")
                .build();
        accountMapper.insert(insertEntity);
        BizAccount account = accountMapper.findOne(insertEntity.getId());

        Assert.assertTrue(account.getId() > 1);
        Assert.assertEquals("testInsert", account.getAccount());
        Assert.assertEquals("testPwd", account.getPassword());
        Assert.assertEquals("testToken", account.getToken());
    }

    @Test
    public void testBasicInsertSelective() {
        BizAccount insertEntity = BizAccount.builder()
                .account("testInsertSelective")
                .password("testPwd")
                .build();
        accountMapper.insertSelective(insertEntity);
        BizAccount account = accountMapper.findOne(insertEntity.getId());

        Assert.assertTrue(account.getId() > 1);
        Assert.assertEquals("testInsertSelective", account.getAccount());
        Assert.assertEquals("testPwd", account.getPassword());
        Assert.assertTrue(StringUtils.isBlank(account.getToken()));
    }

    @Test
    public void testBasicUpdate() {
        BizAccount updateEntity = BizAccount.builder()
                .account("testUpdate")
                .password("update")
                .token("update")
                .build();
        updateEntity.setId(1L);
        accountMapper.update(updateEntity);
        BizAccount account = accountMapper.findOne(updateEntity.getId());

        Assert.assertEquals("testUpdate", account.getAccount());
        Assert.assertEquals("update", account.getPassword());
        Assert.assertEquals("update", account.getToken());
    }

    @Test
    public void testBasicUpdateSelective() {
        BizAccount updateEntity = BizAccount.builder()
                .token("updateSelective")
                .build();
        updateEntity.setId(1L);
        accountMapper.updateSelective(updateEntity);
        BizAccount account = accountMapper.findOne(updateEntity.getId());

        Assert.assertEquals("test1", account.getAccount());
        Assert.assertEquals("password1", account.getPassword());
        Assert.assertEquals("updateSelective", account.getToken());
    }

    @Test
    public void testBasicDelete() {
        accountMapper.delete(3L);

        Assert.assertNull(accountMapper.findOne(3L));
    }

    @Test
    public void testBasicFindAll() {
        List<BizAccount> list = accountMapper.findAll();
        BizAccount account = list.get(0);

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(1, account.getId().intValue());
        Assert.assertEquals("test1", account.getAccount());
        Assert.assertEquals("password1", account.getPassword());
        Assert.assertEquals("token1", account.getToken());
    }

    @Test
    public void testBasicFindByEntity() {
        BizAccount account = BizAccount.builder()
                .account("test1")
                .build();
        List<BizAccount> list = accountMapper.findByEntity(account);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals(1, list.get(0).getId().intValue());
        Assert.assertEquals("test1", list.get(0).getAccount());
        Assert.assertEquals("password1", list.get(0).getPassword());
        Assert.assertEquals("token1", list.get(0).getToken());
    }

    @Test
    public void testBasicFindByEntityWithPage() {
        BizAccount account = BizAccount.builder()
                .build();
        List<BizAccount> list = accountMapper.findByEntityWithPage(account, PageRequest.of(0, 1));

        Assert.assertEquals(1, list.size());
        Assert.assertEquals(1, list.get(0).getId().intValue());
        Assert.assertEquals("test1", list.get(0).getAccount());
        Assert.assertEquals("password1", list.get(0).getPassword());
        Assert.assertEquals("token1", list.get(0).getToken());
    }

    @Test
    public void testBasicFindPage() {
        BizAccount account = BizAccount.builder()
                .build();
        Page<BizAccount> page = accountMapper.findPage(account, PageRequest.of(0, 1));

        List<BizAccount> list = page.getContent();
        Assert.assertEquals(2, page.getTotalElements());
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(1, list.get(0).getId().intValue());
        Assert.assertEquals("test1", list.get(0).getAccount());
        Assert.assertEquals("password1", list.get(0).getPassword());
        Assert.assertEquals("token1", list.get(0).getToken());
    }
}
