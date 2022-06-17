package com.kelin.archetype.api.controller.internal

import com.kelin.archetype.client.entity.UserResponse
import com.kelin.archetype.client.service.UserApiService
import com.kelin.archetype.common.exception.RestExceptionFactory
import com.kelin.archetype.database.mapper.primary.UserMapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/internal/user")
class UserApiServiceImpl(
    private val userMapper: UserMapper
) : UserApiService {
    override fun getUserById(id: Long): UserResponse {
        val user = userMapper.findOne(id)
            ?: throw RestExceptionFactory.toNotFoundException(HttpStatus.NOT_FOUND.value(), "$id user not found")
        return UserResponse().also {
            it.id = user.id
            it.name = user.userName
        }
    }
}