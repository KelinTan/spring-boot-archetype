// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.client;

import com.alo7.kelin.testing.BaseWebTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * @author Kelin Tan
 */
public class DemoClientTest extends BaseWebTest {
    @Autowired
    DemoClient demoClient;

    @Test
    public void testRpcInit() {
        assert demoClient != null;
        assert Proxy.isProxyClass(demoClient.getClass());
    }

}
