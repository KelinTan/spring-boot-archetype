// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

/**
 * FakeH2DataSource use H2 db and extends HikariDataSource
 *
 * @author Kelin Tan
 */
public class FakeDataSource extends HikariDataSource {
    @Getter
    private final String schemaLocation;
    @Getter
    private final String dataLocation;

    public FakeDataSource(String schemaLocation, String dataLocation) {
        super();
        this.schemaLocation = schemaLocation;
        this.dataLocation = dataLocation;
    }
}
