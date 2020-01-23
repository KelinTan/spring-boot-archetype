// Copyright 2020 Alo7 Inc. All rights reserved.

package com.alo7.archetype.common

import com.alo7.archetype.api.SpringBootArchetypeServer
import com.alo7.archetype.api.config.DataSourceConfig
import com.alo7.archetype.base.testing.KtBaseSpringTest
import com.alo7.archetype.base.testing.database.MockDatabase
import com.alo7.archetype.common.entity.biz.BizAccount
import com.alo7.archetype.common.mapper.biz.BizAccountMapper
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
class KtBasicCrudMapperTest : KtBaseSpringTest() {
    @Autowired
    lateinit var accountMapper: BizAccountMapper

    @Test
    fun `test basic find one`() {
        accountMapper.findOne(1L) verify {
            id eq 1
            account eq "test1"
            password eq "password1"
            age eq 10
            height eq 20L
            money eq BigDecimal("100.00")
            birthDate eq LocalDateTime.of(2020, 1, 10, 15, 34, 27)
            verify eq true
        }
    }

    @Test
    fun `test basic find all`() {
        accountMapper.findAll() verify {
            size eq 2
            item(0) verify {
                account eq "test1"
                password eq "password1"
                token eq "token1"
                id eq 1
            }
        }
    }

    @Test
    fun `test basic find by entity`() {
        accountMapper.findByEntity(BizAccount().apply {
            account = "test1"
        }) verify {
            size eq 1
            item(0) verify {
                id eq 1
                account eq "test1"
                password eq "password1"
                token eq "token1"
            }
        }
    }

    @Test
    fun `test basic find with page`() {
        accountMapper.findByEntityWithPage(null, PageRequest.of(0, 1)) verify {
            size eq 1
            item(0) verify {
                id eq 1
                account eq "test1"
                password eq "password1"
                token eq "token1"
            }
        }
    }
}