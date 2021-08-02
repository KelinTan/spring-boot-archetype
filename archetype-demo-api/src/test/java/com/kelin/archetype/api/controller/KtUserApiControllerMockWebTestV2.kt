// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller

import com.kelin.archetype.api.ApiApplication
import com.kelin.archetype.database.entity.primary.User
import com.kelin.archetype.database.mapper.primary.UserMapper
import com.kelin.archetype.test.KtBaseSpringMockWebTest
import org.junit.Test
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
class KtUserApiControllerMockWebTestV2 : KtBaseSpringMockWebTest() {
    @Spy
    lateinit var userMapper: UserMapper

    @InjectMocks
    @Autowired
    lateinit var userApiController: UserApiController

    @Test
    fun `test find user by id with mock`() {
        `when`(userMapper.findOne(1)).thenReturn(User().apply {
            userName = "mockKtV2"
            id = 2
        })

        Http.withPath("$API_PREFIX/1")
            .performGet()
            .json() verify {
            -"result" verify {
                -"id" eq 2
                -"userName" eq "mockKtV2"
            }
        }
    }

    companion object {
        const val API_PREFIX = "/api/v1/user"
    }
}
