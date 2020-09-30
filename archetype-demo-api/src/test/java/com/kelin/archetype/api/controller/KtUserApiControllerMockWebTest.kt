// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller

import com.kelin.archetype.api.SpringBootArchetypeApplication
import com.kelin.archetype.database.config.PrimaryDatabase
import com.kelin.archetype.database.entity.primary.User
import com.kelin.archetype.database.mapper.primary.UserMapper
import com.kelin.archetype.test.KtBaseSpringWebTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

/**
 * @author Kelin Tan
 */
@MockDatabase(name = PrimaryDatabase.NAME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [SpringBootArchetypeApplication::class]
)
class KtUserApiControllerMockWebTest : KtBaseSpringWebTest() {
    @MockBean
    lateinit var userMapper: UserMapper

    @Test
    fun `test find all users with mock`() {
        Mockito.`when`(userMapper.findAll()).thenReturn(listOf(User().apply {
            userName = "mock"
            id = 2
        }))

        Http.withPath("$API_PREFIX/findAll")
            .performGet()
            .json() verify {
            -"result" verify {
                size eq 1
                item(0) verify {
                    -"id" eq 2
                    -"userName" eq "mock"
                }
            }
        }
    }

    companion object {
        const val API_PREFIX = "/api/v1/user"
    }
}
