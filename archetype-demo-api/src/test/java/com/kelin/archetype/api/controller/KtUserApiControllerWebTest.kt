// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.controller

import com.kelin.archetype.api.ApiApplication
import com.kelin.archetype.common.beans.RestResponse
import com.kelin.archetype.database.config.PrimaryDatabase
import com.kelin.archetype.database.entity.primary.User
import com.kelin.archetype.test.KtBaseSpringWebTest
import com.kelin.archetype.test.database.MockDatabase
import org.apache.http.HttpStatus
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType

/**
 * @author Kelin Tan
 */
@MockDatabase(name = PrimaryDatabase.NAME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ApiApplication::class]
)
class KtUserApiControllerWebTest : KtBaseSpringWebTest() {
    @Test
    fun `find all users with header`() {
        Http.withPath("$API_PREFIX/findAll")
            .performHeader()
            .status() eq HttpStatus.SC_OK
    }

    @Test
    fun `test find all users`() {
        val response = Http.withPath("$API_PREFIX/findAll")
            .performGet()
            .json()

        response verify {
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

    @Test
    fun `test find all users with async http`() {
        AsyncHttp.withPath("${API_PREFIX}/findAll")
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
        AsyncHttp.withPath("${API_PREFIX}/save")
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
        AsyncHttp.withPath("${API_PREFIX}/save3")
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
    fun `test find all users with web client`() {
        WebClient.get().uri("${API_PREFIX}/findAll")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody(object : ParameterizedTypeReference<RestResponse<List<User>>>() {})
            .returnResult() verify {
            responseBody verify {
                result verify {
                    size eq 4
                    item(0) verify {
                        id eq 1
                    }
                }
            }
        }

        WebClient.get().uri("${API_PREFIX}/findAll")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
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

    companion object {
        const val API_PREFIX = "/api/v1/user"
    }
}
