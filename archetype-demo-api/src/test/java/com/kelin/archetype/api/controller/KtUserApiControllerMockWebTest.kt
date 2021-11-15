// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller

import com.kelin.archetype.api.ApiApplication
import com.kelin.archetype.database.entity.primary.User
import com.kelin.archetype.database.mapper.primary.UserMapper
import com.kelin.archetype.test.KtBaseSpringMockWebTest
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`
import org.mockito.Spy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ApiApplication::class]
)
class KtUserApiControllerMockWebTest : KtBaseSpringMockWebTest() {
    @Spy
    lateinit var userMapper: UserMapper

    @InjectMocks
    @Autowired
    lateinit var userApiController: UserApiController

    @Test
    fun `test find all users with mock`() {
        `when`(userMapper.findAll()).thenReturn(listOf(User().apply {
            userName = "mockKt"
            id = 2
        }))

        Http.withPath("$API_PREFIX/findAll")
            .performGet()
            .json() verify {
            -"result" verify {
                size eq 1
                item(0) verify {
                    -"id" eq 2
                    -"userName" eq "mockKt"
                }
            }
        }
    }

    companion object {
        const val API_PREFIX = "/api/v1/user"
    }
}
