// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller

import com.kelin.archetype.api.SpringBootArchetypeServer
import com.kelin.archetype.core.testing.KtBaseSpringWebTest
import com.kelin.archetype.core.testing.database.MockDatabase
import org.apache.http.HttpStatus
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@MockDatabase
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [SpringBootArchetypeServer::class]
)
class KtUserApiControllerWebTest : KtBaseSpringWebTest() {
    @Test
    fun `test find all users`() {
        Http.withPath("$API_PREFIX/findAll")
            .performGet()
            .json() verify {
            -"result" verify {
                size eq 4
                item(0) verify {
                    -"id" eq 1
                    -"id" not 2
                    "nId".node.isNullOrNone
                    +"userName" startsWith "test"
                    -"userName" endsWith "1"
                }
            }
        }
    }

    @Test
    fun `test find user`() {
        Http.withPath("$API_PREFIX/findUser")
            .withParam("id", 1)
            .performGet()
            .json() verify {
            -"result" verify {
                isNull eq false
                -"id" eq 1
            }
        }
    }

    @Test
    fun `test save user`() {
        Http.withPath("$API_PREFIX/save")
            .withParam("name", "performPostWithParam")
            .performPost()
            .json() verify {
            -"result" verify {
                isNull eq false
                -"userName" eq "performPostWithParam"
            }
        }
    }

    @Test
    fun `test put user`() {
        Http.withPath("$API_PREFIX/save3")
            .withContent(
                """
                {"userName":"performPut"}
            """.trimIndent()
            )
            .performPut()
            .json() verify {
            -"result" verify {
                "id".node.asInt() greater 0
                -"userName" eq "performPut"
            }
        }
    }

    @Test
    fun `test save user valid error`() {
        Http.withPath("$API_PREFIX/save2")
            .withContent("{}")
            .performPost()
            .json() verify {
            -"errorCode" eq HttpStatus.SC_BAD_REQUEST
        }
    }

    @Test
    fun `test save user method not supported`() {
        Http.withPath("$API_PREFIX/save3")
            .withContent("{}")
            .performPost()
            .json() verify {
            -"errorCode" eq HttpStatus.SC_METHOD_NOT_ALLOWED
        }
    }

    companion object {
        const val API_PREFIX = "/api/v1/user"
    }
}
