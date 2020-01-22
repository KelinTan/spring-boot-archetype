// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = SpringBootArchetypeServer.class)
@MockDatabase(name = DataSourceConfig.BIZ, tables = "biz_account")
public class BasicCrudMapperTest extends BaseSpringTest {
    @Autowired
    private BizAccountMapper accountMapper;

    @Test
    public void testBasicFindOne() {
        BizAccount account = accountMapper.findOne(1L);

        BizAccount expect = BizAccount.builder()
                .account("test1")
                .password("password1")
                .age(10)
                .height(20L)
                .money(new BigDecimal("100.00"))
                .birthDate(LocalDateTime.of(2020, 1, 10, 15, 34, 27))
                .verify(true)
                .build();
        assertThat(account).isEqualToIgnoringNullFields(expect);
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

        assertThat(account).isEqualToIgnoringNullFields(insertEntity);
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

        assertThat(account).isEqualToIgnoringNullFields(insertEntity);
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

        assertThat(account).isEqualToIgnoringNullFields(updateEntity);
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

        assertThat(account).isEqualToIgnoringNullFields(updateEntity);
    }

    @Test
    public void testBasicDelete() {
        accountMapper.delete(3L);

        assertThat(accountMapper.findOne(3L)).isNull();
    }

    @Test
    public void testBasicFindAll() {
        List<BizAccount> list = accountMapper.findAll();
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).isEqualToIgnoringNullFields(BizAccount.builder()
                .account("test1")
                .password("password1")
                .token("token1")
                .build());
    }

    @Test
    public void testBasicFindByEntity() {
        BizAccount account = BizAccount.builder()
                .account("test1")
                .build();
        List<BizAccount> list = accountMapper.findByEntity(account);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getId().intValue());
        assertEquals("test1", list.get(0).getAccount());
        assertEquals("password1", list.get(0).getPassword());
        assertEquals("token1", list.get(0).getToken());
    }

    @Test
    public void testBasicFindByEntityWithPage() {
        BizAccount account = BizAccount.builder()
                .build();
        List<BizAccount> list = accountMapper.findByEntityWithPage(account, PageRequest.of(0, 1));

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getId().intValue());
        assertEquals("test1", list.get(0).getAccount());
        assertEquals("password1", list.get(0).getPassword());
        assertEquals("token1", list.get(0).getToken());
    }

    @Test
    public void testBasicFindPage() {
        BizAccount account = BizAccount.builder()
                .build();
        Page<BizAccount> page = accountMapper.findPage(account, PageRequest.of(0, 1));

        List<BizAccount> list = page.getContent();
        assertEquals(2, page.getTotalElements());
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getId().intValue());
        assertEquals("test1", list.get(0).getAccount());
        assertEquals("password1", list.get(0).getPassword());
        assertEquals("token1", list.get(0).getToken());
    }
}
