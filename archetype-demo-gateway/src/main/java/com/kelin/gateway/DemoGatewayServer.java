// Copyright 2022 Kelin Inc. All rights reserved.

package com.kelin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Kelin Tan
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.kelin.archetype.client")
public class DemoGatewayServer {
    public static void main(String[] args) {
        SpringApplication.run(DemoGatewayServer.class, args);
    }
}
