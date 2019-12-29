// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype;

import com.alo7.archetype.rpc.RpcClientScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RpcClientScan("com.alo7.archetype.client")
@MapperScan("com.alo7.archetype.mapper")
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
