// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.test

import com.kelin.archetype.common.http.AsyncHttpRequest
import com.kelin.archetype.common.http.HttpRequest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient

/**
 * @author Kelin Tan
 */
@AutoConfigureWebMvc
abstract class KtBaseSpringWebTest : KtBaseSpringTest() {
    @LocalServerPort
    var serverPort = 0

    private val host: String
        get() = "http://localhost:$serverPort"

    @Suppress("PropertyName")
    val Http: HttpRequest
        get() = HttpRequest(host)

    @Suppress("PropertyName")
    val AsyncHttp: AsyncHttpRequest
        get() = AsyncHttpRequest(host)

    @Suppress("PropertyName")
    val WebClient: WebTestClient
        get() = WebTestClient.bindToServer().baseUrl(host).build()
}