// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.testing

import com.kelin.archetype.core.http.HttpRequest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
open class KtBaseSpringWebTest : KtBaseSpringTest() {
    @LocalServerPort
    var serverPort = 0

    private val host: String
        get() = "http://localhost:$serverPort"

    @Suppress("PropertyName")
    val Http: HttpRequest
        get() = HttpRequest(host)
}