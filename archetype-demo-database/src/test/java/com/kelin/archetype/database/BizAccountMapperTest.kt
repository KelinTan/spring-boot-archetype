// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database

import com.kelin.archetype.database.config.BizDatabase
import com.kelin.archetype.database.mapper.biz.BizAccountMapper
import com.kelin.archetype.test.KtBaseSpringTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author Kelin Tan
 */
@MockDatabase(name = BizDatabase.NAME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BizAccountMapperTest : KtBaseSpringTest() {
    @Autowired
    lateinit var bizAccountMapper: BizAccountMapper

    @Test
    fun `find by account`() {
        bizAccountMapper.findByAccount("test1") verify {
            account eq "test1"
            id eq 1
            password eq "password1"
            token eq "token1"
            age eq 10
            height eq 20
            money eq BigDecimal(100)
            birthDate eq LocalDateTime.of(2020, 1, 10, 15, 34, 27)
            verify eq true
        }
    }

    @Test
    fun `update token`() {
        bizAccountMapper.updateToken(1, "update")

        bizAccountMapper.findOne(1) verify {
            token eq "update"
        }
    }
}