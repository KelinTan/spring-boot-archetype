// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.testing.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;

/**
 * FakeDataSource use H2 db and extends HikariDataSource
 *
 * @author Kelin Tan
 */
public class FakeDataSource extends HikariDataSource {
    @Getter
    @Setter
    private String schemaLocation;
    @Getter
    @Setter
    private String dataLocation;

    public FakeDataSource(String schemaLocation, String dataLocation) {
        super();
        this.setJdbcUrl("jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        this.schemaLocation = schemaLocation;
        this.dataLocation = dataLocation;
    }
}
