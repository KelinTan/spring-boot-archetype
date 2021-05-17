// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.client

import com.kelin.archetype.api.SpringBootArchetypeApplication
import com.kelin.archetype.common.rest.exception.RestException
import com.kelin.archetype.database.config.PrimaryDatabase
import com.kelin.archetype.database.entity.primary.User
import com.kelin.archetype.database.mapper.primary.UserMapper
import com.kelin.archetype.test.KtBaseSpringTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = [SpringBootArchetypeApplication::class])
@MockDatabase(name = PrimaryDatabase.NAME)
class KtUserClientTest : KtBaseSpringTest() {
    @Autowired
    lateinit var userClient: UserClient

    @Autowired
    lateinit var userClient2: UserClient2

    @Autowired
    lateinit var userMapper: UserMapper

    @Test(expected = RestException::class)
    fun testRpcError() {
        userClient.findAllError()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testRpcCustomError() {
        userClient2.findAllError()
    }

    @Test
    fun testRpcDeleteWithPathVariable() {
        userMapper.findOne(3L) not null
        userClient.delete(3L)
        userMapper.findOne(3L) eq null
    }

    @Test
    fun testRpcGetByIdWithPathVariable() {
        userClient.findUser(1L) verify {
            result verify {
                id eq 1
                userName eq "test1"
            }
        }
    }

    @Test
    fun testRpcGetByIdWithRequestParam() {
        userClient.findUser2(1L) verify {
            result verify {
                id eq 1
                userName eq "test1"
            }
        }
    }

    @Test
    fun testRpcPostWithRequestParam() {
        userClient.save("rpcSave") verify {
            result verify {
                userName eq "rpcSave"
            }
        }
    }

    @Test
    fun testRpcPostWithRequestBody() {
        userClient.save2(User().apply {
            userName = "rpcSave2"
        }) verify {
            result verify {
                userName eq "rpcSave2"
            }
        }
    }

    @Test
    fun testRpcPutWithRequestBody() {
        userClient.save3(User().apply {
            userName = "rpcSave3"
        }) verify {
            result verify {
                userName eq "rpcSave3"
            }
        }
    }

    @Test
    fun testRpcGetNoParams() {
        userClient.findAll() verify {
            result verify {
                item(0) verify {
                    id eq 1
                }
            }
        }
    }

    @Test
    fun `test rpc get use async http client`() {
        userClient.findAllAsync() verify {
            result verify {
                item(0) verify {
                    id eq 1
                }
            }
        }
    }

    @Test
    fun testRpcDeleteWithRequestParam() {
        userMapper.findOne(3L) not null
        userClient.delete2(3L)
        userMapper.findOne(3L) eq null
    }
}