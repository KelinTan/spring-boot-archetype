// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.mybatis.crud;

import com.google.common.base.CaseFormat;
import com.kelin.archetype.common.exception.RestExceptionFactory;
import com.kelin.archetype.common.log.LogMessageBuilder;
import com.kelin.archetype.database.MapperTable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kelin Tan
 */
public abstract class SqlProviderSupport {
    public static final String DEFAULT_ID = "id";

    protected String table(ProviderContext context) {
        MapperTable mapperTable = getMapperAnnotation(context);
        if (StringUtils.isBlank(mapperTable.value())) {
            throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder()
                    .message("@MapperTable need value or table")
                    .parameter("class", context.getMapperType().getName())
                    .build());
        }
        return mapperTable.value();
    }

    protected String columns(ProviderContext context) {
        return getMapperAnnotation(context).columns();
    }

    protected String[] columns(List<Field> fields) {
        return fields.stream().map(this::columnName).toArray(String[]::new);
    }

    protected String columnName(Field field) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
    }

    protected String bindParameter(Field field) {
        return "#{" + field.getName() + "}";
    }

    protected List<Field> fields(Object entity) {
        if (entity == null) {
            return Collections.emptyList();
        }
        List<Field> declaredFields = FieldUtils.getAllFieldsList(entity.getClass());
        //fix jacoco https://www.eclemma.org/jacoco/trunk/doc/faq.html
        return declaredFields.stream().filter(field -> !field.isSynthetic()).collect(Collectors.toList());
    }

    protected Object value(Object entity, Field field) {
        try {
            field.setAccessible(true);
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            field.setAccessible(false);
        }
    }

    private MapperTable getMapperAnnotation(ProviderContext context) {
        if (context.getMapperType().isAnnotationPresent(MapperTable.class)) {
            return context.getMapperType().getAnnotation(MapperTable.class);
        }

        throw RestExceptionFactory.toSystemException(LogMessageBuilder.builder()
                .message("@BasicCrudMapper need @MapperTable")
                .parameter("class", context.getMapperType().getName())
                .build());
    }
}
