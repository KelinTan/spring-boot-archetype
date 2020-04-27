// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api;

import com.kelin.archetype.base.rpc.RpcClientScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RpcClientScan("com.Kelin.archetype.api.client")
public class SpringBootArchetypeServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootArchetypeServer.class, args);
    }
}
