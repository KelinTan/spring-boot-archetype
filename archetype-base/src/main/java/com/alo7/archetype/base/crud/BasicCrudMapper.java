// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.base.crud;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

/**
 * Provide BasicCrudMapper to support basic crud for single table
 * <p>
 * the tables need has auto_increment id for primary key and entity's fields need LOWER_CAMEL
 * <p>
 * Actually,we do not suggest use BasicCrud in mybatis just because mybatis is not an orm
 *
 * @author Kelin Tan
 */
public interface BasicCrudMapper<T extends BasicEntity> {
    @SelectProvider(type = BasicCrudSqlProvider.class, method = "findOne")
    T findOne(Long id);

    @InsertProvider(type = BasicCrudSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = SqlProviderSupport.DEFAULT_ID)
    void insertSelective(T entity);

    @InsertProvider(type = BasicCrudSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = SqlProviderSupport.DEFAULT_ID)
    void insert(T entity);

    @UpdateProvider(type = BasicCrudSqlProvider.class, method = "updateSelective")
    void updateSelective(T entity);

    @UpdateProvider(type = BasicCrudSqlProvider.class, method = "update")
    void update(T entity);

    @DeleteProvider(type = BasicCrudSqlProvider.class, method = "delete")
    void delete(Long id);

    @SelectProvider(type = BasicCrudSqlProvider.class, method = "findAll")
    List<T> findAll();

    @SelectProvider(type = BasicCrudSqlProvider.class, method = "findByEntity")
    List<T> findByEntity(T entity);

    @SelectProvider(type = BasicCrudSqlProvider.class, method = "findByEntityWithPage")
    List<T> findByEntityWithPage(T entity, Pageable page);

    @SelectProvider(type = BasicCrudSqlProvider.class, method = "countByEntity")
    int countByEntity(T entity);

    default Page<T> findPage(T entity, Pageable page) {
        long total = countByEntity(entity);
        if (total == 0) {
            return new PageImpl<>(Collections.emptyList(), page, total);
        }
        List<T> content = findByEntityWithPage(entity, page);
        return new PageImpl<>(content, page, total);
    }
}