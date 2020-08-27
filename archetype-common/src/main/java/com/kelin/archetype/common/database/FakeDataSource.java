// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

/**
 * FakeDataSource use H2 db and extends HikariDataSource
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
        this.setJdbcUrl("jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        this.schemaLocation = schemaLocation;
        this.dataLocation = dataLocation;
    }
}
