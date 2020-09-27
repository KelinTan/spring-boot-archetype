// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api;

import static com.kelin.archetype.api.model.constant.ApiConstants.RPC_CLIENT_SCAN_PACKAGE;

import com.kelin.archetype.core.rpc.RpcClientScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RpcClientScan(RPC_CLIENT_SCAN_PACKAGE)
public class SpringBootArchetypeServer {
    public static void main(String[] args) {
        //to make GlobalException to handle NoHandlerFoundException
        System.setProperty("spring.mvc.throwExceptionIfNoHandlerFound", "true");
        System.setProperty("spring.mvc.staticPathPattern", "/swagger-ui.html");
        SpringApplication.run(SpringBootArchetypeServer.class, args);
    }
}
