// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.mybatis.plugins;

import com.kelin.archetype.common.database.MapperTable;
import com.kelin.archetype.common.database.sharding.ShardingStrategy;
import com.kelin.archetype.common.log.LogMessageBuilder;
import com.kelin.archetype.common.rest.exception.RestExceptionFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
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

import java.lang.reflect.Field;
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
                String shardingValue = getShardingValue(boundSql, mapperTable.shardingKey(), mappedStatement.getId());
                metaStatementHandler.setValue(DELEGATE_BOUND_SQL_SQL,
                        getShardingSql(boundSql.getSql(), mapperTable, shardingValue));
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

    @SneakyThrows
    private String getShardingValue(BoundSql boundSql, String shardingKey, String mapperStatementId) {
        Object parameterObject = boundSql.getParameterObject();
        Object shardingValue;
        if (parameterObject instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) parameterObject;
            shardingValue = map.get(shardingKey);
        } else if (ClassUtils.isPrimitiveOrWrapper(parameterObject.getClass()) || parameterObject instanceof String) {
            shardingValue = parameterObject;
        } else {
            Field field = FieldUtils.getField(parameterObject.getClass(), shardingKey, true);
            shardingValue = field.get(parameterObject);
        }

        if (shardingValue == null) {
            throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder().message(
                    "Invalid statement").parameter("statement", mapperStatementId).build());
        }

        return String.valueOf(shardingValue);
    }

    @SneakyThrows
    private Class<?> getMapperClazz(String mapperStatementId) {
        return ClassUtils.getClass(mapperStatementId.substring(0, mapperStatementId.lastIndexOf(".")));
    }

    @SneakyThrows
    private String getShardingSql(String sql, MapperTable mapperTable, String shardingValue) {
        ShardingStrategy shardingStrategy = mapperTable.shardingStrategy().newInstance();
        return sql.replace(mapperTable.value(),
                shardingStrategy.getShardingTable(mapperTable.value(), shardingValue, mapperTable.count()));
    }
}
