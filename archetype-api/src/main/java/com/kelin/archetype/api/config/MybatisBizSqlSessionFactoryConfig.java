// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.config;

import static com.kelin.archetype.api.model.constant.Constants.BIZ_ALIAS_PACKAGE;
import static com.kelin.archetype.api.model.constant.Constants.BIZ_MAPPER_PACKAGE;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Kelin Tan
 */
@Configuration
@MapperScan(basePackages = BIZ_MAPPER_PACKAGE, sqlSessionFactoryRef = "bizSqlSessionFactory")
public class MybatisBizSqlSessionFactoryConfig {
    private static final String BIZ_MAPPER_PATH = "classpath:mappers/biz/*.xml";

    @Autowired
    @Qualifier(DataSourceConfig.BIZ)
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory bizSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        ClassPathResource classPathResource = new ClassPathResource(BIZ_MAPPER_PATH);
        if (classPathResource.exists()) {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(BIZ_MAPPER_PATH);
            sqlSessionFactoryBean.setMapperLocations(resources);
        }
        sqlSessionFactoryBean.setTypeAliasesPackage(BIZ_ALIAS_PACKAGE);
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
