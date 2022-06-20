// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.config;

import static com.kelin.archetype.database.config.PrimaryDatabase.NAME;

import com.kelin.archetype.database.mybatis.MybatisDatabase;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kelin Tan
 */
@Configuration
@MybatisDatabase(name = NAME,
        databaseUrl = "${spring.datasource.primary.jdbc-url}",
        databaseUserName = "${spring.datasource.primary.username}",
        databasePassword = "${spring.datasource.primary.password}",
        mapperPackages = "com.kelin.archetype.database.mapper.primary",
        mapperXmlLocation = "classpath:mappers/primary/*.xml",
        typeAliasesPackage = "com.kelin.archetype.database.entity.primary",
        schemaLocation = "classpath:schema/primary/*.sql",
        dataLocation = "classpath:data/primary/*.sql"
)
public class PrimaryDatabase {
    public static final String NAME = "Primary";
}
