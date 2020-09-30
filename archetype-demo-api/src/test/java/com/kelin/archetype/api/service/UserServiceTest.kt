// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.service

import com.kelin.archetype.api.SpringBootArchetypeApplication
import com.kelin.archetype.database.config.PrimaryDatabase
import com.kelin.archetype.database.mapper.primary.UserMapper
import com.kelin.archetype.test.KtBaseSpringTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@MockDatabase(name = PrimaryDatabase.NAME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = [SpringBootArchetypeApplication::class]
)
class UserServiceTest : KtBaseSpringTest() {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userMapper: UserMapper

    @Test
    fun `find cache user by id`() {
        userService.findCacheUserById(1) verify {
            id eq 1
        }

        userMapper.delete(1)

        userService.findCacheUserById(1) verify {
            id eq 1
        }
    }

    @Test
    fun `find user by id`() {
        userService.findUserById(2) not null

        userMapper.delete(2)

        userService.findUserById(2) eq null
    }
}