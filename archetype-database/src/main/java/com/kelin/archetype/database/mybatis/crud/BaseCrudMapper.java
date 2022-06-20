// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.mybatis.crud;

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
 * Provide BaseCrudMapper to support basic crud for single table
 * <p>
 * the tables need has auto_increment id for primary key and entity's fields need LOWER_CAMEL
 * <p>
 * Actually,we do not suggest use BasicCrud in mybatis just because mybatis is not an orm
 *
 * @author Kelin Tan
 */
public interface BaseCrudMapper<T extends BaseEntity> {
    @SelectProvider(type = BaseCrudSqlProvider.class, method = "findOne")
    T findOne(Long id);

    @InsertProvider(type = BaseCrudSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = SqlProviderSupport.DEFAULT_ID)
    void insertSelective(T entity);

    @InsertProvider(type = BaseCrudSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = SqlProviderSupport.DEFAULT_ID)
    void insert(T entity);

    @UpdateProvider(type = BaseCrudSqlProvider.class, method = "updateSelective")
    void updateSelective(T entity);

    @UpdateProvider(type = BaseCrudSqlProvider.class, method = "update")
    void update(T entity);

    @DeleteProvider(type = BaseCrudSqlProvider.class, method = "delete")
    void delete(Long id);

    @SelectProvider(type = BaseCrudSqlProvider.class, method = "findAll")
    List<T> findAll();

    @SelectProvider(type = BaseCrudSqlProvider.class, method = "findByEntity")
    List<T> findByEntity(T entity);

    @SelectProvider(type = BaseCrudSqlProvider.class, method = "findByEntityWithPage")
    List<T> findByEntityWithPage(T entity, Pageable page);

    @SelectProvider(type = BaseCrudSqlProvider.class, method = "countByEntity")
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