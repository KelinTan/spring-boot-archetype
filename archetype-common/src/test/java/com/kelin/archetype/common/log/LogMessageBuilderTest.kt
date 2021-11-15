// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.log

import com.kelin.archetype.test.KtTestUtils
import org.junit.jupiter.api.Test

/**
 * @author Kelin Tan
 */
class LogMessageBuilderTest : KtTestUtils {
    @Test
    fun testLogBuilder() {
        LogMessageBuilder.builder().message("this a test")
            .parameter("key1", "value1")
            .parameter("key2", "key2")
            .build() eq "this a test Parameters: key1=value1,key2=key2"
    }
}