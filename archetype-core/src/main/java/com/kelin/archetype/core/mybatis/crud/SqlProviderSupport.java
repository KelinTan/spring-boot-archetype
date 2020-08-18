// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.mybatis.crud;

import com.google.common.base.CaseFormat;
import com.kelin.archetype.core.log.LogMessageBuilder;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        List<Field> filterFields = new ArrayList<>(declaredFields.length);

        //fix jacoco https://www.eclemma.org/jacoco/trunk/doc/faq.html
        for (Field field : declaredFields) {
            if (!field.isSynthetic()) {
                filterFields.add(field);
            }
        }

        return filterFields;
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
