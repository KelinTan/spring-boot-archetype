package com.kelin.archetype.api.controller.internal

import com.kelin.archetype.api.session.SessionExceptionFactory
import com.kelin.archetype.client.entity.AccountResponse
import com.kelin.archetype.client.service.AccountApiService
import com.kelin.archetype.common.exception.RestExceptionFactory
import com.kelin.archetype.database.mapper.biz.BizAccountMapper
import com.kelin.archetype.jwt.JwtManager
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Kelin Tan
 */
@RestController
@RequestMapping("/internal/account")
@Slf4j
class AccountApiServiceImpl(
    private val jwtManager: JwtManager,
    private val accountMapper: BizAccountMapper
) : AccountApiService {
    override fun verifyToken(token: String): AccountResponse {
        try {
            val jwt = jwtManager.verify(token)

            if (jwt != null) {
                val account = accountMapper.findByAccount(jwt.subject)
                    ?: throw RestExceptionFactory.toNotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "${jwt.subject} not found"
                    )
                return AccountResponse().also {
                    it.token = token
                    it.account = account.account
                }
            }
        } catch (e: Exception) {
        }
        throw SessionExceptionFactory.toAccountSessionExpired()
    }
}