// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.config;

import com.kelin.archetype.base.testing.database.FakeDataSource;
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
    @Bean(DataSourceConfig.PRIMARY)
    @Primary
    public DataSource primaryDataSource() {
        return new FakeDataSource("schema/primary/*.sql", "data/primary/*.sql");
    }

    @Bean(DataSourceConfig.BIZ)
    public DataSource bizDataSource() {
        return new FakeDataSource("schema/biz/*.sql", "data/biz/*.sql");
    }
}
