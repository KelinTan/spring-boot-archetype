// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.service

import com.kelin.archetype.api.ApiApplication
import com.kelin.archetype.database.config.BizDatabase
import com.kelin.archetype.database.config.PrimaryDatabase
import com.kelin.archetype.database.mapper.biz.BizAccountMapper
import com.kelin.archetype.database.mapper.primary.UserMapper
import com.kelin.archetype.test.KtBaseSpringTest
import com.kelin.archetype.test.database.MockDatabase
import com.kelin.archetype.test.database.MockDatabases
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@MockDatabases(
    MockDatabase(name = PrimaryDatabase.NAME, mappers = [UserMapper::class]),
    MockDatabase(name = BizDatabase.NAME, mappers = [BizAccountMapper::class])
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = [ApiApplication::class]
)
class BizServiceTest : KtBaseSpringTest() {
    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var bizAccountMapper: BizAccountMapper

    @Autowired
    lateinit var bizService: BizService

    @Test
    fun `test update with primary transaction`() {
        userMapper.findOne(1L) verify {
            userName eq "test1"
        }

        try {
            bizService.updateWithPrimaryTransaction()
        } catch (e: Exception) {
        }
        userMapper.findOne(1L) verify {
            userName eq "test1"
        }
    }

    @Test
    fun `test update with biz transaction`() {
        bizAccountMapper.findOne(1L) verify {
            account eq "test1"
        }

        try {
            bizService.updateWithBizTransaction()
        } catch (e: Exception) {
        }

        bizAccountMapper.findOne(1L) verify {
            account eq "test1"
        }
    }

    @Test
    fun `test update with multiple transaction`() {
        userMapper.findOne(1L) verify {
            userName eq "test1"
        }

        bizAccountMapper.findOne(1L) verify {
            account eq "test1"
        }

        try {
            bizService.updateWithMultipleTransaction()
        } catch (e: Exception) {
        }

        userMapper.findOne(1L) verify {
            userName eq "test1"
        }
        bizAccountMapper.findOne(1L) verify {
            account eq "rollbackFail"
        }
    }

    @Test
    fun `find cache user by id`() {
        bizService.findCacheUserById(1) verify {
            id eq 1
        }

        userMapper.delete(1)

        bizService.findCacheUserById(1) verify {
            id eq 1
        }
    }

    @Test
    fun `find user by id`() {
        bizService.findUserById(2) not null

        userMapper.delete(2)

        bizService.findUserById(2) eq null
    }
}