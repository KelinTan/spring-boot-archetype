// Copyright 2020 Alo7 Inc. All rights reserved.

package com.kelin.archetype.api.client

import com.kelin.archetype.api.SpringBootArchetypeServer
import com.kelin.archetype.base.testing.KtBaseSpringTest
import com.kelin.archetype.base.testing.database.MockDatabase
import com.kelin.archetype.common.entity.primary.User
import com.kelin.archetype.common.mapper.primary.UserMapper
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = [SpringBootArchetypeServer::class])
@MockDatabase
class KtUserClientTest : KtBaseSpringTest() {
    @Autowired
    lateinit var userClient: UserClient

    @Autowired
    lateinit var userMapper: UserMapper

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
}