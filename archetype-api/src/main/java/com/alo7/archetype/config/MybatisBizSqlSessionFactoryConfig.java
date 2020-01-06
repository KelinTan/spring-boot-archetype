// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.config;

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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Kelin Tan
 */
@Configuration
@MapperScan(basePackages = "com.alo7.archetype.persistence.mapper.biz", sqlSessionFactoryRef = "bizSqlSessionFactory")
public class MybatisBizSqlSessionFactoryConfig {
    @Autowired
    @Qualifier("bizDataSource")
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory bizSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mappers/biz/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.alo7.archetype.persistence.entity.biz");
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
    public SqlSessionTemplate bizSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(bizSqlSessionFactory());
    }

    @Bean(name = "bizTransactionManager")
    public DataSourceTransactionManager bizTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
