// Copyright 2020 Alo7 Inc. All rights reserved.

package com.alo7.archetype.api.controller

import com.alo7.archetype.base.http.HttpRequest
import com.alo7.archetype.base.testing.KtBaseSpringWebTest
import com.alo7.archetype.base.testing.database.MockDatabase
import org.junit.Test

/**
 * @author Kelin Tan
 */
@MockDatabase
class KtUserApiControllerWebTest : KtBaseSpringWebTest() {
    @Test
    fun `test find all users`() {
        HttpRequest.withPath("$serverPrefix$API_PREFIX/findAll")
            .performGet()
            .json() verify {
            get("result") verify {
                size() eq 4
                item(0) verify {
                    get("id") eq 1
                }
            }
        }
    }

    companion object {
        const val API_PREFIX = "/api/v1/user"
    }
}
