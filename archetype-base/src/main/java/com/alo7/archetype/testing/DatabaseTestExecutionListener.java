// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.testing;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.transaction.TestContextTransactionUtils;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kelin Tan
 */
@Slf4j
public class DatabaseTestExecutionListener extends AbstractTestExecutionListener {
    private volatile boolean initialized;

    @Override
    public final int getOrder() {
        return 5000;
    }

    @Override
    public void beforeTestClass(TestContext testContext) {
        Set<MockDatabase> sqlAnnotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(
                testContext.getTestClass(), MockDatabase.class, MockDatabases.class);
        if (sqlAnnotations.isEmpty() || initialized) {
            return;
        }

        sqlAnnotations.forEach(mockDatabase -> {
            DataSource dataSource = TestContextTransactionUtils.retrieveDataSource(
                    testContext, mockDatabase.dataSource());
            loadScripts(dataSource, mockDatabase.schema());
        });
        this.initialized = true;
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        Set<MockDatabase> sqlAnnotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(
                testContext.getTestClass(), MockDatabase.class, MockDatabases.class);
        if (sqlAnnotations.isEmpty()) {
            return;
        }

        sqlAnnotations.forEach(mockDatabase -> {
            DataSource dataSource = TestContextTransactionUtils.retrieveDataSource(
                    testContext, mockDatabase.dataSource());
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            try {
                Resource[] resources = resolver.getResources(mockDatabase.data());
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                JdbcTestUtils.deleteFromTables(jdbcTemplate, Arrays.stream(resources)
                        .map(resource -> FilenameUtils.removeExtension(resource.getFilename())).collect(
                                Collectors.joining(",")));
            } catch (IOException e) {
                log.error("load resource error:", e);
            }
        });
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        Set<MockDatabase> sqlAnnotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(
                testContext.getTestClass(), MockDatabase.class, MockDatabases.class);
        if (sqlAnnotations.isEmpty()) {
            return;
        }

        sqlAnnotations.forEach(mockDatabase -> {
            DataSource dataSource = TestContextTransactionUtils.retrieveDataSource(
                    testContext, mockDatabase.dataSource());
            loadScripts(dataSource, mockDatabase.data());
        });
    }

    private void loadScripts(DataSource dataSource, String path) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(path);
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
            databasePopulator.setScripts(resources);
            databasePopulator.execute(dataSource);
        } catch (IOException e) {
            log.error("load resource error:", e);
        }
    }
}
