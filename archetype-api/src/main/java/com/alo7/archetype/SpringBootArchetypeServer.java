// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype;

import com.alo7.archetype.rpc.RpcClientScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RpcClientScan("com.alo7.archetype.client")
public class SpringBootArchetypeServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootArchetypeServer.class, args);
    }
}
