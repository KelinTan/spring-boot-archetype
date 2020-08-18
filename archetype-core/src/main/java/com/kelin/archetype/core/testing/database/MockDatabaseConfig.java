// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.testing.database;

import lombok.Data;

import javax.sql.DataSource;

/**
 * @author Kelin Tan
 */
@Data
public class MockDatabaseConfig {
    private DataSource dataSource;
    private String schemaLocation;
    private String dataLocation;
}
