// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.mybatis.crud;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

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
                .WHERE(fields(entity).stream().filter(field -> value(entity, field) != null)
                        .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                .toString();
    }

    public String findByEntityWithPage(Object entity, Pageable page, ProviderContext context) {
        String sql = new SQL()
                .SELECT(columns(context))
                .FROM(table(context))
                .WHERE(fields(entity).stream().filter(field -> value(entity, field) != null)
                        .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                .toString();
        if (page != null) {
            return sql + " LIMIT " + page.getOffset() + "," + page.getPageSize();
        }

        return sql;
    }

    public String countByEntity(Object entity, ProviderContext context) {
        return new SQL()
                .SELECT("COUNT(*)")
                .FROM(table(context))
                .WHERE(fields(entity).stream().filter(field -> value(entity, field) != null)
                        .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                .toString();
    }

    private String doUpdate(Object entity, ProviderContext context, boolean selective) {
        List<Field> fields = filterFields(selective, entity);
        return new SQL()
                .UPDATE(table(context))
                .SET(fields.stream().map(field -> columnName(field) + " = " + bindParameter(field))
                        .toArray(String[]::new))
                .WHERE("id = #{id}")
                .toString();
    }

    private String doInsert(Object entity, ProviderContext context, boolean selective) {
        List<Field> fields = filterFields(selective, entity);
        return new SQL()
                .INSERT_INTO(table(context))
                .INTO_COLUMNS(columns(fields))
                .INTO_VALUES(fields.stream().map(this::bindParameter).toArray(String[]::new))
                .toString();
    }

    private List<Field> filterFields(boolean selective, Object entity) {
        List<Field> fields = fields(entity);
        if (selective) {
            fields = fields.stream().filter(field -> value(entity, field) != null).collect(Collectors.toList());
        }
        return fields;
    }
}
