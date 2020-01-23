// Copyright 2020 Alo7 Inc. All rights reserved.

package com.alo7.archetype.common

import com.alo7.archetype.api.SpringBootArchetypeServer
import com.alo7.archetype.api.config.DataSourceConfig
import com.alo7.archetype.base.testing.BaseSpringTest
import com.alo7.archetype.base.testing.database.MockDatabase
import com.alo7.archetype.common.entity.biz.BizAccount
import com.alo7.archetype.common.mapper.biz.BizAccountMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.data.domain.PageRequest
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = [SpringBootArchetypeServer::class])
@MockDatabase(name = DataSourceConfig.BIZ, tables = ["biz_account"])
class BasicCrudMapperTestKt : BaseSpringTest() {
    @Autowired
    lateinit var accountMapper: BizAccountMapper

    @Test
    fun `test basic find one`() {
        assertThat(accountMapper.findOne(1L)).isEqualToIgnoringNullFields(BizAccount().apply {
            id = 1
            account = "test1"
            password = "password1"
            age = 10
            height = 20L
            money = BigDecimal("100.00")
            birthDate = LocalDateTime.of(2020, 1, 10, 15, 34, 27)
            verify = true
        })
    }

    @Test
    fun `test basic find all`() {
        assertThat(accountMapper.findAll()).hasSize(2).first().isEqualToIgnoringNullFields(BizAccount().apply {
            account = "test1"
            password = "password1"
            token = "token1"
            id = 1
        })
    }

    @Test
    fun `test basic find by entity`() {
        val list = accountMapper.findByEntity(BizAccount().apply {
            account = "test1"
        })
        assertThat(list).hasSize(1).first().isEqualToIgnoringNullFields(BizAccount().apply {
            id = 1
            account = "test1"
            password = "password1"
            token = "token1"
        })
    }

    @Test
    fun `test basic find with page`() {
        val list = accountMapper.findByEntityWithPage(null, PageRequest.of(0, 1))
        assertThat(list).hasSize(1).first().isEqualToIgnoringNullFields(
            BizAccount().apply {
                id = 1
                account = "test1"
                password = "password1"
                token = "token1"
            })
    }
}