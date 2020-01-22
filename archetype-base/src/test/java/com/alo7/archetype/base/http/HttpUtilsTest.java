// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.base.http;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpUtilsTest {
    @Test
    public void concatPathTest() {
        assertEquals(HttpUtils.concatPath("xxx.com/", "/test"), "xxx.com/test");
        assertEquals(HttpUtils.concatPath("xxx.com/", "test"), "xxx.com/test");
        assertEquals(HttpUtils.concatPath("xxx.com", "/test"), "xxx.com/test");
        assertEquals(HttpUtils.concatPath("xxx.com", "test"), "xxx.com/test");
    }

    @Test
    public void formatUrlWithPathParamsTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("path1", 1);
        map.put("path2", 2);
        assertEquals(HttpUtils.formatUrlWithPathParams("xxx.com/api/1/test", map), "xxx.com/api/1/test");
        assertEquals(HttpUtils.formatUrlWithPathParams("xxx.com/api/{path1}/test", map), "xxx.com/api/1/test");
        assertEquals(HttpUtils.formatUrlWithPathParams("xxx.com/api/{path1}/{path2}", map), "xxx.com/api/1/2");
    }

    @Test
    public void formatUrlWithParamsTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        assertEquals(HttpUtils.formatUrlWithParams("xxx.com/api/1/test", map),
                "xxx.com/api/1/test?key1=1&key2=2");
        assertEquals(HttpUtils.formatUrlWithParams("xxx.com/api/1/test?key0=0", map),
                "xxx.com/api/1/test?key0=0&key1=1&key2=2");
    }
}
