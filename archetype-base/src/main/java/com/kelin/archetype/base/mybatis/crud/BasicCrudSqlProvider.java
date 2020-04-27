// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.mybatis.crud;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * @author Kelin Tan
 */
@SuppressWarnings("unused")
public class BasicCrudSqlProvider extends SqlProviderSupport {
    public String findOne(Long id, ProviderContext context) {
        return new SQL()
                .SELECT(columns(context))
                .FROM(table(context))
                .WHERE(DEFAULT_ID + " = " + id)
                .toString();
    }

    public String delete(Long id, ProviderContext context) {
        return new SQL()
                .DELETE_FROM(table(context))
                .WHERE(DEFAULT_ID + " =" + id)
                .toString();
    }

    public String update(Object entity, ProviderContext context) {
        return doUpdate(entity, context, false);
    }

    public String updateSelective(Object entity, ProviderContext context) {
        return doUpdate(entity, context, true);
    }

    public String insert(Object entity, ProviderContext context) {
        return doInsert(entity, context, false);
    }

    public String insertSelective(Object entity, ProviderContext context) {
        return doInsert(entity, context, true);
    }

    public String findAll(ProviderContext context) {
        return new SQL()
                .SELECT(columns(context))
                .FROM(table(context))
                .toString();
    }

    public String findByEntity(Object entity, ProviderContext context) {
        return new SQL()
                .SELECT(columns(context))
                .FROM(table(context))
                .WHERE(Stream.of(fields(entity))
                        .filter(field -> value(entity, field) != null)
                        .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                .toString();
    }

    public String findByEntityWithPage(Object entity, Pageable page, ProviderContext context) {
        String sql = new SQL()
                .SELECT(columns(context))
                .FROM(table(context))
                .WHERE(Stream.of(fields(entity))
                        .filter(field -> value(entity, field) != null)
                        .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                .toString();
        if (page != null) {
            return sql + " limit " + page.getOffset() + "," + page.getPageSize();
        }

        return sql;
    }

    public String countByEntity(Object entity, ProviderContext context) {
        return new SQL()
                .SELECT("count(*)")
                .FROM(table(context))
                .WHERE(Stream.of(fields(entity))
                        .filter(field -> value(entity, field) != null)
                        .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                .toString();
    }

    private String doUpdate(Object entity, ProviderContext context, boolean selective) {
        Field[] fields = filterFields(selective, entity);
        return new SQL()
                .UPDATE(table(context))
                .SET(Stream.of(fields)
                        .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                .WHERE("id = #{id}")
                .toString();
    }

    private String doInsert(Object entity, ProviderContext context, boolean selective) {
        Field[] fields = filterFields(selective, entity);
        return new SQL()
                .INSERT_INTO(table(context))
                .INTO_COLUMNS(columns(fields))
                .INTO_VALUES(Stream.of(fields).map(this::bindParameter).toArray(String[]::new))
                .toString();
    }

    private Field[] filterFields(boolean selective, Object entity) {
        Field[] fields = fields(entity);
        if (selective) {
            fields = Stream.of(fields).filter(field -> value(entity, field) != null).toArray(Field[]::new);
        }
        return fields;
    }
}
