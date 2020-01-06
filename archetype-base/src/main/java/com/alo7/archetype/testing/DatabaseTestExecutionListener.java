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
    private volatile boolean initializedSchema;

    @Override
    public final int getOrder() {
        return 5000;
    }

    @Override
    public void beforeTestClass(TestContext testContext) {
        Set<MockDatabase> sqlAnnotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(
                testContext.getTestClass(), MockDatabase.class, MockDatabases.class);
        if (sqlAnnotations.isEmpty() || initializedSchema) {
            return;
        }

        sqlAnnotations.forEach(mockDatabase -> {
            DataSource dataSource = TestContextTransactionUtils.retrieveDataSource(
                    testContext, mockDatabase.dataSource());
            loadScripts(dataSource, mockDatabase.schema(), new String[] {});
        });
        this.initializedSchema = true;
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

                //truncate specific table or all
                if (mockDatabase.table().length != 0) {
                    JdbcTestUtils.deleteFromTables(jdbcTemplate, mockDatabase.table());
                } else {
                    JdbcTestUtils.deleteFromTables(jdbcTemplate, Arrays.stream(resources)
                            .map(resource -> FilenameUtils.removeExtension(resource.getFilename())).collect(
                                    Collectors.joining(",")));
                }
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
            loadScripts(dataSource, mockDatabase.data(), mockDatabase.table());
        });
    }

    private void loadScripts(DataSource dataSource, String path, String[] table) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(path);
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
            resourceDatabasePopulator.setScripts(filterResource(resources, table));
            resourceDatabasePopulator.execute(dataSource);
        } catch (IOException e) {
            log.error("load resource error:", e);
        }
    }

    private Resource[] filterResource(Resource[] resources, String[] table) {
        return Arrays.stream(resources).filter(s -> checkResourceLoad(s, table))
                .toArray(Resource[]::new);
    }

    /**
     * load all or specific resource
     */
    private boolean checkResourceLoad(Resource resource, String[] table) {
        return table.length == 0 || Arrays.stream(table).anyMatch(
                s -> s.equals(FilenameUtils.removeExtension(resource.getFilename())));
    }
}
