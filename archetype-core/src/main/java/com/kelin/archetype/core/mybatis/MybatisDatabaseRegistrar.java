// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.mybatis;

import com.kelin.archetype.common.consants.Profile;
import com.kelin.archetype.common.database.DbFactory;
import com.kelin.archetype.common.database.FakeDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Kelin Tan
 */
@Slf4j
public class MybatisDatabaseRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware {
    private BeanFactory beanFactory;
    private Environment environment;

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata meta, @NotNull BeanDefinitionRegistry registry) {
        Class<?> clazz = ClassUtils.getClass(meta.getClassName());
        MybatisDatabase database = clazz.getAnnotation(MybatisDatabase.class);
        if (database == null) {
            return;
        }

        //scan mybatis mappers
        ClassPathMapperScanner mapperScanner = new ClassPathMapperScanner(registry);
        mapperScanner.setSqlSessionFactoryBeanName(computeSqlSessionFactoryName(database));
        mapperScanner.registerFilters();
        mapperScanner.doScan(database.mapperPackages());

        DefaultListableBeanFactory defaultBeanFactory = (DefaultListableBeanFactory) this.beanFactory;

        //register datasource
        DataSource dataSource = createDataSource(database);
        defaultBeanFactory.registerSingleton(DbFactory.computeDataSourceName(database.name()), dataSource);

        //register sqlSession
        SqlSessionFactory sqlSessionFactory = createSqlSessionFactory(dataSource, database);
        defaultBeanFactory.registerSingleton(computeSqlSessionFactoryName(database), sqlSessionFactory);

        //register sql session template
        defaultBeanFactory.registerSingleton(computeSqlSessionTemplateName(database),
                new SqlSessionTemplate(sqlSessionFactory));

        //register transactional manager
        defaultBeanFactory.registerSingleton(computeTransactionManagerName(database),
                new DataSourceTransactionManager(dataSource));
    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(@NotNull Environment environment) {
        this.environment = environment;
    }

    private String computeSqlSessionFactoryName(MybatisDatabase database) {
        return database.name() + ".sqlSessionFactory";
    }

    private String computeSqlSessionTemplateName(MybatisDatabase database) {
        return database.name() + ".sqlSessionTemplate";
    }

    private String computeTransactionManagerName(MybatisDatabase database) {
        return database.name() + ".transactionManager";
    }

    private DataSource createDataSource(MybatisDatabase database) {
        if (Profile.isTest(this.environment)) {
            return new FakeDataSource(database.schemaLocation(), database.dataLocation());
        }
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(resolvePlaceholders(database.databaseUrl()))
                .username(resolvePlaceholders(database.databaseUserName()))
                .password(resolvePlaceholders(database.databasePassword()))
                .build();
    }

    private String resolvePlaceholders(String placeHolder) {
        return this.environment.resolvePlaceholders(placeHolder);
    }

    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource, MybatisDatabase database)
            throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        ClassPathResource classPathResource = new ClassPathResource(database.mapperXmlLocation());
        if (classPathResource.exists()) {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(database.mapperXmlLocation());
            sqlSessionFactoryBean.setMapperLocations(resources);
        }

        sqlSessionFactoryBean.setTypeAliasesPackage(database.typeAliasesPackage());
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
}
