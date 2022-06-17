package com.kelin.archetype.client.service

import com.kelin.archetype.client.entity.AccountResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author Kelin Tan
 */
@FeignClient(url = "\${feign.api.client}", path = "/internal/account", value = "accountFeignClient")
interface AccountApiService {
    @GetMapping("/verify")
    fun verifyToken(@RequestParam("token") token: String): AccountResponse
}