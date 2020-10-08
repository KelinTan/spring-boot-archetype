// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.http

import com.kelin.archetype.common.http.HttpUtils.concatPath
import com.kelin.archetype.common.http.HttpUtils.formatUrlWithParams
import com.kelin.archetype.common.http.HttpUtils.formatUrlWithPathParams
import com.kelin.archetype.test.KtTestUtils
import org.junit.Test

/**
 * @author Kelin Tan
 */
class HttpUtilsTest : KtTestUtils {
    @Test
    fun concatPathTest() {
        concatPath("xxx.com/", "/test") eq "xxx.com/test"
        concatPath("xxx.com/", "test") eq "xxx.com/test"
        concatPath("xxx.com", "/test") eq "xxx.com/test"
        concatPath("xxx.com", "test") eq "xxx.com/test"
    }

    @Test
    fun formatUrlWithPathParamsTest() {
        val map = mapOf("path1" to 1, "path2" to 2)
        formatUrlWithPathParams("xxx.com/api/1/test", map) eq "xxx.com/api/1/test"
        formatUrlWithPathParams("xxx.com/api/{path1}/test", map) eq "xxx.com/api/1/test"
        formatUrlWithPathParams("xxx.com/api/{path1}/{path2}", map) eq "xxx.com/api/1/2"
    }

    @Test
    fun formatUrlWithParamsTest() {
        val map = mapOf("key1" to 1, "key2" to 2)
        formatUrlWithParams("xxx.com/api/1/test", map) eq "xxx.com/api/1/test?key1=1&key2=2"
        formatUrlWithParams("xxx.com/api/1/test?key0=0", map) eq "xxx.com/api/1/test?key0=0&key1=1&key2=2"
    }
}