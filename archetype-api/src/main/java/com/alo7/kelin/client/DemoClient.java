// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.client;

import com.alo7.kelin.rpc.RpcClient;

/**
 * @author Kelin Tan
 */
@RpcClient
public interface DemoClient {
    String sayHello();
}
