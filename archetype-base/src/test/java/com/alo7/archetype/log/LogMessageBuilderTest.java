// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.log;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kelin Tan
 */
public class LogMessageBuilderTest {
    @Test
    public void testLogBuilder() {
        Assert.assertEquals(LogMessageBuilder.builder().message("this a test")
                .parameter("key1", "value1")
                .parameter("key2", "key2")
                .build(), "this a test Parameters: key1=value1,key2=key2");
    }
}
