// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.testing.database;

import lombok.Data;

import javax.sql.DataSource;

/**
 * @author Kelin Tan
 */
@Data
public class DataSourceConfig {
    private DataSource dataSource;
    private String schemaLocation;
    private String dataLocation;
}
