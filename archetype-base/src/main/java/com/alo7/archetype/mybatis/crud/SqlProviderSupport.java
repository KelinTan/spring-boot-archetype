// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.mybatis.crud;

import com.alo7.archetype.log.LogMessageBuilder;
import com.google.common.base.CaseFormat;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * @author Kelin Tan
 */
public abstract class SqlProviderSupport {
    public static final String DEFAULT_ID = "id";

    public static final Field[] EMPTY_FIELDS = new Field[0];

    protected String table(ProviderContext context) {
        return getMapperAnnotation(context).value();
    }

    protected String columns(ProviderContext context) {
        return getMapperAnnotation(context).columns();
    }

    protected String[] columns(Field[] fields) {
        return Stream.of(fields).map(this::columnName).toArray(String[]::new);
    }

    protected String columnName(Field field) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
    }

    protected String bindParameter(Field field) {
        return "#{" + field.getName() + "}";
    }

    protected Field[] fields(Object entity) {
        return entity == null ? EMPTY_FIELDS : entity.getClass().getDeclaredFields();
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
        throw new RuntimeException(LogMessageBuilder.builder()
                .message("@BasicCrudMapper need @MapperTable")
                .parameter("class", context.getMapperType().getName())
                .build());
    }
}
