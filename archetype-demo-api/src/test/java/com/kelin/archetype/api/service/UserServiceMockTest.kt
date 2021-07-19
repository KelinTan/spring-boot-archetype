// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.service

import com.kelin.archetype.api.ApiApplication
import com.kelin.archetype.database.mapper.primary.UserMapper
import com.kelin.archetype.test.KtBaseSpringTest
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
class UserServiceMockTest : KtBaseSpringTest() {
    @InjectMocks
    lateinit var userService: UserServiceImpl

    @Mock
    lateinit var userMapper: UserMapper

    @Test
    fun `find user by id with mock`() {
        Mockito.`when`(userMapper.findOne(2)).thenReturn(null)
        userService.findUserById(2) eq null
    }
}