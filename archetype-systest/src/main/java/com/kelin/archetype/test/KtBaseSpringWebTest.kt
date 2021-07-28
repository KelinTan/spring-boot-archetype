// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.test

import com.kelin.archetype.common.http.AsyncHttpRequest
import com.kelin.archetype.common.http.HttpRequest
import org.springframework.boot.web.server.LocalServerPort

/**
 * @author Kelin Tan
 */
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
}