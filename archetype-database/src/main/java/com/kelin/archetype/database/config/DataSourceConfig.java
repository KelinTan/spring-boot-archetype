// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author Kelin Tan
 */
@Configuration
public class DataSourceConfig {
    public static final String PRIMARY = "primaryDataSource";
    public static final String BIZ = "bizDataSource";

    @Bean(PRIMARY)
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    @Primary
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(BIZ)
    @ConfigurationProperties(prefix = "spring.datasource.biz")
    public DataSource bizDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }
}
