// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.config;

import static com.kelin.archetype.database.config.BizDatabase.NAME;

import com.kelin.archetype.database.mybatis.MybatisDatabase;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kelin Tan
 */
@Configuration
@MybatisDatabase(name = NAME,
        databaseUrl = "${spring.datasource.biz.jdbc-url}",
        databaseUserName = "${spring.datasource.biz.username}",
        databasePassword = "${spring.datasource.biz.password}",
        mapperPackages = "com.kelin.archetype.database.mapper.biz",
        mapperXmlLocation = "classpath:mappers/biz/*.xml",
        typeAliasesPackage = "com.kelin.archetype.database.entity.biz",
        schemaLocation = "classpath:schema/biz/mysql_*.sql",
        dataLocation = "classpath:data/biz/*.sql"
)
public class BizDatabase {
    public static final String NAME = "Biz";
}
