// Copyright 2022 Kelin Inc. All rights reserved.

package com.kelin.archetype.client.service

import com.kelin.archetype.client.entity.UserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author Kelin Tan
 */
@FeignClient(url = "\${feign.api.client}", path = "/internal/user", value = "userFeignClient")
@Component
interface UserApiService {
    @GetMapping("/id")
    fun getUserById(@RequestParam("id") id: Long): UserResponse
}