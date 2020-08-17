// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api;

import static com.kelin.archetype.api.model.constant.ApiConstants.RPC_CLIENT_SCAN_PACKAGE;

import com.kelin.archetype.base.rpc.RpcClientScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RpcClientScan(RPC_CLIENT_SCAN_PACKAGE)
public class SpringBootArchetypeServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootArchetypeServer.class, args);
    }
}
