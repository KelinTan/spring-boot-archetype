// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.config;

import static com.kelin.archetype.common.constants.Profile.PROFILE_DEV;
import static com.kelin.archetype.common.constants.Profile.PROFILE_STAGING;

import com.kelin.archetype.api.model.constant.ApiConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({PROFILE_DEV, PROFILE_STAGING})
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage(ApiConstants.SWAGGER_API_PACKAGE))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("Swagger")
                        .build());
    }
}
