// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api;

import static com.kelin.archetype.api.model.constant.ApiConstants.RPC_CLIENT_SCAN_PACKAGE;

import com.kelin.archetype.common.constants.Profile;
import com.kelin.archetype.core.rpc.RpcClientScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@RpcClientScan(RPC_CLIENT_SCAN_PACKAGE)
public class ApiApplication {
    public static void main(String[] args) {
        //to make GlobalException to handle NoHandlerFoundException
        System.setProperty("spring.mvc.throwExceptionIfNoHandlerFound", "true");
        System.setProperty("spring.mvc.staticPathPattern", "/swagger-ui.html");
        ConfigurableApplicationContext context = SpringApplication.run(ApiApplication.class, args);
        setActiveProfile(context);
    }

    private static void setActiveProfile(ConfigurableApplicationContext context) {
        Arrays.stream(context.getEnvironment().getActiveProfiles()).findFirst().ifPresent(
                profile -> Profile.activeProfile = profile);
    }
}
