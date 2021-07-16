// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller

import com.kelin.archetype.api.SpringBootArchetypeApplication
import com.kelin.archetype.api.client.UserClient
import com.kelin.archetype.common.rest.exception.RestExceptionFactory
import com.kelin.archetype.database.config.PrimaryDatabase
import com.kelin.archetype.test.KtBaseSpringWebTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

/**
 * @author Kelin Tan
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [SpringBootArchetypeApplication::class]
)
@MockDatabase(name = PrimaryDatabase.NAME)
class KtHystrixDemoControllerTest : KtBaseSpringWebTest() {
    @MockBean
    lateinit var userClient: UserClient

    @Test
    fun `test find all users with fallback`() {
        Mockito.`when`(userClient.findAll()).thenThrow(RestExceptionFactory.toSystemException())

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