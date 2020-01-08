// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * @author Kelin Tan
 */
@Configuration
@Profile("test")
@Primary
public class FakeDataSourceConfig {
    @Bean("primaryDataSource")
    @Primary
    public DataSource primaryDataSource() {
        return buildFakeDataSource();
    }

    @Bean("bizDataSource")
    public DataSource bizDataSource() {
        return buildFakeDataSource();
    }

    private HikariDataSource buildFakeDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false")
                .type(HikariDataSource.class)
                .build();
    }
}
