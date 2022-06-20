// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller

import com.kelin.archetype.api.ApiApplication
import com.kelin.archetype.api.client.UserClient
import com.kelin.archetype.database.config.PrimaryDatabase
import com.kelin.archetype.rpc.RpcExceptionFactory
import com.kelin.archetype.test.KtBaseSpringMockWebTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ApiApplication::class]
)
@MockDatabase(name = PrimaryDatabase.NAME)
class KtHystrixDemoControllerTest : KtBaseSpringMockWebTest() {
    @Mock
    lateinit var userClient: UserClient

    @InjectMocks
    @Autowired
    lateinit var hystrixDemoController: HystrixDemoController

    @Test
    fun `test find all users with fallback`() {
        Mockito.`when`(userClient.findAll()).thenThrow(RpcExceptionFactory.toException("userClient", "findAll", ""))

        Http.withPath("$API_PREFIX/demo")
            .performGet()
            .json() verify {
            -"result" verify {
                item(0) verify {
                    -"userName" eq "fallback"
                }
            }
        }
    }

    companion object {
        const val API_PREFIX = "/api/v1/hystrix"
    }
}