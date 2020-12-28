// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.mybatis.plugins;

import com.kelin.archetype.common.database.MapperTable;
import com.kelin.archetype.common.log.LogMessageBuilder;
import com.kelin.archetype.common.rest.exception.RestExceptionFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * @author Kelin Tan
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare",
        args = {Connection.class, Integer.class})})
@Component
public class MybatisShardingPlugin implements Interceptor {
    private static final String DELEGATE_MAPPER_STATEMENT = "delegate.mappedStatement";
    private static final String DELEGATE_BOUND_SQL = "delegate.boundSql";
    private static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue(DELEGATE_MAPPER_STATEMENT);
        Class<?> clazz = getMapperClazz(mappedStatement.getId());

        if (clazz.isAnnotationPresent(MapperTable.class)) {
            MapperTable mapperTable = clazz.getAnnotation(MapperTable.class);
            if (mapperTable.sharding()) {
                BoundSql boundSql = (BoundSql) metaStatementHandler.getValue(DELEGATE_BOUND_SQL);
                int id = getShardingKeyValue(boundSql, mapperTable.shardingKey(), mappedStatement.getId());
                metaStatementHandler.setValue(DELEGATE_BOUND_SQL_SQL,
                        getShardingSql(boundSql.getSql(), mapperTable.value(), id, mapperTable.count()));
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private int getShardingKeyValue(BoundSql boundSql, String shardingKey, String mapperStatementId) {
        Map<?, ?> map = (Map<?, ?>) boundSql.getParameterObject();
        Object shardingValue = map.get(shardingKey);
        if (shardingValue == null) {
            throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder().message(
                    "Invalid statement").parameter("statement", mapperStatementId).build());
        }

        if (shardingValue instanceof Integer) {
            return (Integer) shardingValue;
        } else if (shardingValue instanceof Long) {
            return Math.toIntExact((Long) shardingValue);
        }

        throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder().message(
                "Invalid statement").parameter("statement", mapperStatementId).build());
    }

    @SneakyThrows
    private Class<?> getMapperClazz(String mapperStatementId) {
        return ClassUtils.getClass(mapperStatementId.substring(0, mapperStatementId.lastIndexOf(".")));
    }

    private String getShardingSql(String sql, String table, int id, int count) {
        return sql.replace(table, table + "_" + getShardingIdx(id, count));
    }

    private int getShardingIdx(int tableId, int tableCount) {
        return tableId % tableCount;
    }
}
