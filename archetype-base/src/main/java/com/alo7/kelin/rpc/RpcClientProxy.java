// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.kelin.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Kelin Tan
 */
public class RpcClientProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "this is proxy";
    }
}
