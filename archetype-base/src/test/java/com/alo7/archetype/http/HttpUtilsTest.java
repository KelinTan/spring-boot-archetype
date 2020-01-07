// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.http;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpUtilsTest {
    @Test
    public void concatPathTest() {
        Assert.assertEquals(HttpUtils.concatPath("xxx.com/", "/test"), "xxx.com/test");
        Assert.assertEquals(HttpUtils.concatPath("xxx.com/", "test"), "xxx.com/test");
        Assert.assertEquals(HttpUtils.concatPath("xxx.com", "/test"), "xxx.com/test");
        Assert.assertEquals(HttpUtils.concatPath("xxx.com", "test"), "xxx.com/test");
    }

    @Test
    public void formatUrlWithPathParamsTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("path1", 1);
        map.put("path2", 2);
        Assert.assertEquals(HttpUtils.formatUrlWithPathParams("xxx.com/api/1/test", map), "xxx.com/api/1/test");
        Assert.assertEquals(HttpUtils.formatUrlWithPathParams("xxx.com/api/{path1}/test", map), "xxx.com/api/1/test");
        Assert.assertEquals(HttpUtils.formatUrlWithPathParams("xxx.com/api/{path1}/{path2}", map), "xxx.com/api/1/2");
    }

    @Test
    public void formatUrlWithParamsTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        Assert.assertEquals(HttpUtils.formatUrlWithParams("xxx.com/api/1/test", map),
                "xxx.com/api/1/test?key1=1&key2=2");
        Assert.assertEquals(HttpUtils.formatUrlWithParams("xxx.com/api/1/test?key0=0", map),
                "xxx.com/api/1/test?key0=0&key1=1&key2=2");
    }
}
