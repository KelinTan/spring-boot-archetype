// Copyright 2020 Alo7 Inc. All rights reserved.

package com.alo7.archetype.base.testing

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
open class KtBaseSpringWebTest : KtBaseSpringTest() {
    @LocalServerPort
    var serverPort = 0

    val serverPrefix: String
        get() = "http://localhost:$serverPort"
}