// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller

import com.kelin.archetype.api.ApiApplication
import com.kelin.archetype.database.config.PrimaryDatabase
import com.kelin.archetype.test.KtBaseSpringWebTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@MockDatabase(name = PrimaryDatabase.NAME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ApiApplication::class]
)
class KtUserApiControllerAsyncWebTest : KtBaseSpringWebTest() {
    @Test
    fun `test find all users with async http`() {
        AsyncHttp.withPath("$API_PREFIX/findAll")
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
    fun `test save user async http client`() {
        AsyncHttp.withPath("$API_PREFIX/save")
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
    fun `test put user async http client`() {
        AsyncHttp.withPath("$API_PREFIX/save3")
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

    companion object {
        const val API_PREFIX = "/api/v1/user"
    }
}
