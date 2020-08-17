// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.config;

import static com.kelin.archetype.base.consants.Profile.PROFILE_TEST;

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
@Profile(PROFILE_TEST)
@Primary
public class FakeDataSourceConfig {
    @Bean(DataSourceConfig.PRIMARY)
    @Primary
    public DataSource primaryDataSource() {
        return new FakeDataSource("classpath:schema/primary/*.sql", "classpath:data/primary/*.sql");
    }

    @Bean(DataSourceConfig.BIZ)
    public DataSource bizDataSource() {
        return new FakeDataSource("classpath:schema/biz/*.sql", "classpath:data/biz/*.sql");
    }
}
