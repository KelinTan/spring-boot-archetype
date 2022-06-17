// Copyright 2022 Alo7 Inc. All rights reserved.

package com.kelin.gateway.exception

/**
 * @author Kelin Tan
 */
enum class GatewayErrorCode(
    val errorCode: Int,
    val errorMessage: String
) {
    SESSION_EXPIRE(1000, "Session expired")
}