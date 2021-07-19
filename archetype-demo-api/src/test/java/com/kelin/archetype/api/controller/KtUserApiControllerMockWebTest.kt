// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller

import com.kelin.archetype.api.ApiApplication
import com.kelin.archetype.database.config.PrimaryDatabase
import com.kelin.archetype.database.entity.primary.User
import com.kelin.archetype.database.mapper.primary.UserMapper
import com.kelin.archetype.test.KtBaseSpringWebTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@MockDatabase(name = PrimaryDatabase.NAME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ApiApplication::class]
)
class KtUserApiControllerMockWebTest : KtBaseSpringWebTest() {
    @Mock
    lateinit var userMapper: UserMapper

    @InjectMocks
    @Autowired
    lateinit var userApiController: UserApiController

    @Test
    fun `test find all users with mock`() {
        `when`(userMapper.findAll()).thenReturn(listOf(User().apply {
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
