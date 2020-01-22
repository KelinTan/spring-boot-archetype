// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.common;

import com.alo7.archetype.api.SpringBootArchetypeServer;
import com.alo7.archetype.api.config.DataSourceConfig;
import com.alo7.archetype.base.testing.BaseSpringTest;
import com.alo7.archetype.base.testing.database.MockDatabase;
import com.alo7.archetype.base.testing.database.MockDatabases;
import com.alo7.archetype.common.entity.biz.BizAccount;
import com.alo7.archetype.common.mapper.biz.BizAccountMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        Assert.assertEquals(10, account.getAge().intValue());
        Assert.assertEquals(20, account.getHeight().longValue());
        Assert.assertEquals(100, account.getMoney().intValue());
        Assert.assertEquals(LocalDateTime.of(2020, 1, 10, 15, 34, 27), account.getBirthDate());
        Assert.assertTrue(account.getVerify());
    }

    @Test
    public void testBasicInsert() {
        LocalDateTime now = LocalDateTime.now();

        BizAccount insertEntity = BizAccount.builder()
                .account("testInsert")
                .password("testPwd")
                .token("testToken")
                .age(10)
                .height(10L)
                .money(BigDecimal.valueOf(15.55))
                .birthDate(now)
                .verify(false)
                .build();
        accountMapper.insert(insertEntity);
        BizAccount account = accountMapper.findOne(insertEntity.getId());

        Assert.assertTrue(account.getId() > 1);
        Assert.assertEquals("testInsert", account.getAccount());
        Assert.assertEquals("testPwd", account.getPassword());
        Assert.assertEquals("testToken", account.getToken());
        Assert.assertEquals(10, account.getAge().intValue());
        Assert.assertEquals(10, account.getHeight().longValue());
        Assert.assertEquals(BigDecimal.valueOf(15.55), account.getMoney());
        Assert.assertTrue(account.getBirthDate().isEqual(now));
        Assert.assertFalse(account.getVerify());
    }

    @Test
    public void testBasicInsertSelective() {
        LocalDateTime now = LocalDateTime.now();

        BizAccount insertEntity = BizAccount.builder()
                .account("testInsertSelective")
                .password("testPwd")
                .age(10)
                .height(10L)
                .money(BigDecimal.valueOf(15.55))
                .birthDate(now)
                .verify(false)
                .build();
        accountMapper.insertSelective(insertEntity);
        BizAccount account = accountMapper.findOne(insertEntity.getId());

        Assert.assertTrue(account.getId() > 1);
        Assert.assertEquals("testInsertSelective", account.getAccount());
        Assert.assertEquals("testPwd", account.getPassword());
        Assert.assertTrue(StringUtils.isBlank(account.getToken()));
        Assert.assertEquals(10, account.getAge().intValue());
        Assert.assertEquals(10, account.getHeight().longValue());
        Assert.assertEquals(BigDecimal.valueOf(15.55), account.getMoney());
        Assert.assertTrue(account.getBirthDate().isEqual(now));
        Assert.assertFalse(account.getVerify());
    }

    @Test
    public void testBasicUpdate() {
        LocalDateTime now = LocalDateTime.now();

        BizAccount updateEntity = BizAccount.builder()
                .account("testUpdate")
                .password("update")
                .token("update")
                .age(10)
                .height(10L)
                .money(BigDecimal.valueOf(15.55))
                .birthDate(now)
                .verify(true)
                .build();
        updateEntity.setId(1L);
        accountMapper.update(updateEntity);
        BizAccount account = accountMapper.findOne(updateEntity.getId());

        Assert.assertEquals("testUpdate", account.getAccount());
        Assert.assertEquals("update", account.getPassword());
        Assert.assertEquals("update", account.getToken());
        Assert.assertEquals(10, account.getAge().intValue());
        Assert.assertEquals(10, account.getHeight().longValue());
        Assert.assertEquals(BigDecimal.valueOf(15.55), account.getMoney());
        Assert.assertTrue(account.getBirthDate().isEqual(now));
        Assert.assertTrue(account.getVerify());
    }

    @Test
    public void testBasicUpdateSelective() {
        LocalDateTime now = LocalDateTime.now();

        BizAccount updateEntity = BizAccount.builder()
                .token("updateSelective")
                .age(10)
                .height(10L)
                .money(BigDecimal.valueOf(15.55))
                .birthDate(now)
                .verify(true)
                .build();
        updateEntity.setId(1L);
        accountMapper.updateSelective(updateEntity);
        BizAccount account = accountMapper.findOne(updateEntity.getId());

        Assert.assertEquals("test1", account.getAccount());
        Assert.assertEquals("password1", account.getPassword());
        Assert.assertEquals("updateSelective", account.getToken());
        Assert.assertEquals(10, account.getAge().intValue());
        Assert.assertEquals(10, account.getHeight().longValue());
        Assert.assertEquals(BigDecimal.valueOf(15.55), account.getMoney());
        Assert.assertTrue(account.getBirthDate().isEqual(now));
        Assert.assertTrue(account.getVerify());
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
