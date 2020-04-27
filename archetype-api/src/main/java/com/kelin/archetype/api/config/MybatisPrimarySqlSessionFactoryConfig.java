// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.config;

import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Kelin Tan
 */
@Configuration
@MapperScan(basePackages = "com.kelin.archetype.common.mapper.primary", sqlSessionFactoryRef =
        "primarySqlSessionFactory")
public class MybatisPrimarySqlSessionFactoryConfig {
    @Autowired
    @Qualifier(DataSourceConfig.PRIMARY)
    private DataSource dataSource;

    @Bean
    @Primary
    public SqlSessionFactory primarySqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mappers/primary/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.kelin.archetype.common.entity.primary");
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDefaultFetchSize(100);
        configuration.setDefaultStatementTimeout(30);
        configuration.setCacheEnabled(false);
        configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate primarySqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(primarySqlSessionFactory());
    }

    @Bean(name = "primaryTransactionManager")
    @Primary
    public DataSourceTransactionManager primaryTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
