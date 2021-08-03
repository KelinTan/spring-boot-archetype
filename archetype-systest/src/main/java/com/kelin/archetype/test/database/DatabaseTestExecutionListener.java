// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.test.database;

import com.kelin.archetype.common.database.DbUtils;
import com.kelin.archetype.common.database.FakeDataSource;
import com.kelin.archetype.common.database.MapperTable;
import com.kelin.archetype.common.database.sharding.ShardingStrategy;
import com.kelin.archetype.common.exception.RestExceptionFactory;
import com.kelin.archetype.common.log.LogMessageBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kelin Tan
 */
@Slf4j
public class DatabaseTestExecutionListener extends AbstractTestExecutionListener {
    private static final Map<String, Boolean> DATA_SOURCE_SCHEMA_INITIALIZED = new ConcurrentHashMap<>();

    @Override
    public void beforeTestClass(TestContext testContext) {
        Set<MockDatabase> sqlAnnotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(
                testContext.getTestClass(), MockDatabase.class, MockDatabases.class);
        if (sqlAnnotations.isEmpty()) {
            return;
        }

        sqlAnnotations.forEach(mockDatabase -> {
            if (DATA_SOURCE_SCHEMA_INITIALIZED.getOrDefault(DbUtils.computeDataSourceName(mockDatabase.name()),
                    false)) {
                return;
            }
            MockDatabaseConfig config = getDataSourceConfig(testContext, mockDatabase);
            loadScripts(config.getDataSource(), config.getSchemaLocation(), new String[] {});
            log.debug("initialize schema: " + mockDatabase.name());
            DATA_SOURCE_SCHEMA_INITIALIZED.putIfAbsent(mockDatabase.name(), true);
        });
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        Set<MockDatabase> sqlAnnotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(
                testContext.getTestClass(), MockDatabase.class, MockDatabases.class);
        if (sqlAnnotations.isEmpty()) {
            return;
        }

        sqlAnnotations.forEach(mockDatabase -> {
            MockDatabaseConfig config = getDataSourceConfig(testContext, mockDatabase);
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            try {
                Resource[] resources = resolver.getResources(config.getDataLocation());
                JdbcTemplate jdbcTemplate = new JdbcTemplate(config.getDataSource());

                //truncate specific tables or all
                String[] mergeTables = mergeTables(mockDatabase);
                if (mergeTables.length != 0) {
                    log.debug("clear table: " + StringUtils.join(mergeTables, ","));
                    JdbcTestUtils.deleteFromTables(jdbcTemplate, mergeTables);
                } else {
                    log.debug("clear all table for dataSource: " + mockDatabase.name());
                    JdbcTestUtils.deleteFromTables(jdbcTemplate, Arrays.stream(resources)
                            .map(resource -> FilenameUtils.removeExtension(resource.getFilename()))
                            .toArray(String[]::new));
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
            MockDatabaseConfig config = getDataSourceConfig(testContext, mockDatabase);
            String[] mergeTables = mergeTables(mockDatabase);

            loadScripts(config.getDataSource(), config.getDataLocation(), mergeTables);
        });
    }

    private String[] mergeTables(MockDatabase mockDatabase) {
        List<String> mergeTables = getMockDatabaseTables(mockDatabase);
        return mergeTables.stream().distinct().toArray(String[]::new);
    }

    private MockDatabaseConfig getDataSourceConfig(TestContext testContext, MockDatabase mockDatabase) {
        DataSource dataSource = checkDataSource(testContext, mockDatabase);
        MockDatabaseConfig mockDatabaseConfig = new MockDatabaseConfig();
        mockDatabaseConfig.setDataSource(dataSource);
        if (dataSource instanceof FakeDataSource) {
            FakeDataSource fakeDataSource = (FakeDataSource) dataSource;
            mockDatabaseConfig.setSchemaLocation(fakeDataSource.getSchemaLocation());
            mockDatabaseConfig.setDataLocation(fakeDataSource.getDataLocation());
        } else {
            mockDatabaseConfig.setSchemaLocation(mockDatabase.schema());
            mockDatabaseConfig.setDataLocation(mockDatabase.data());
        }

        return mockDatabaseConfig;
    }

    private DataSource checkDataSource(TestContext testContext, MockDatabase mockDatabase) {
        DataSource dataSource = TestContextTransactionUtils.retrieveDataSource(testContext,
                DbUtils.computeDataSourceName(mockDatabase.name()));
        if (dataSource == null) {
            throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder()
                    .message("Invalid data source :")
                    .parameter("name", mockDatabase.name())
                    .build());
        }
        return dataSource;
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

    private List<String> getMockDatabaseTables(MockDatabase mockDatabase) {
        String[] annotationTables = mockDatabase.tables();

        List<String> mapperTables = new ArrayList<>();
        Arrays.stream(mockDatabase.mappers()).forEach(mapperClazz -> {
            if (mapperClazz.isAnnotationPresent(MapperTable.class)) {
                MapperTable mapperTable = mapperClazz.getAnnotation(MapperTable.class);
                mapperTables.addAll(getMapperTables(mapperTable));
            }
        });

        List<String> mergeTables = new ArrayList<>();
        mergeTables.addAll(Arrays.asList(annotationTables));
        mergeTables.addAll(mapperTables);

        return mergeTables;
    }

    @SneakyThrows
    private List<String> getMapperTables(MapperTable mapperTable) {
        if (mapperTable.sharding()) {
            List<String> tables = new ArrayList<>();
            ShardingStrategy strategy = mapperTable.shardingStrategy().newInstance();
            for (int i = 0; i < mapperTable.count(); i++) {
                tables.add(mapperTable.value() + strategy.getShardingTableDelimiter() + i);
            }
            return tables;
        } else {
            return Collections.singletonList(mapperTable.value());
        }
    }
}
