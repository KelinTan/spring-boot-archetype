// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.service

import com.kelin.archetype.api.ApiApplication
import com.kelin.archetype.api.service.impl.BizServiceImpl
import com.kelin.archetype.database.mapper.primary.UserMapper
import com.kelin.archetype.test.KtBaseSpringMockTest
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = [ApiApplication::class]
)
class BizServiceMockTest : KtBaseSpringMockTest() {
    @InjectMocks
    lateinit var bizService: BizServiceImpl

    @Mock
    lateinit var userMapper: UserMapper

    @Test
    fun `find user by id with mock`() {
        Mockito.`when`(userMapper.findOne(2)).thenReturn(null)
        bizService.findUserById(2) eq null
    }
}