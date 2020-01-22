// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.api;

import com.alo7.archetype.base.rpc.RpcClientScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RpcClientScan("com.alo7.archetype.api.client")
public class SpringBootArchetypeServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootArchetypeServer.class, args);
    }
}
