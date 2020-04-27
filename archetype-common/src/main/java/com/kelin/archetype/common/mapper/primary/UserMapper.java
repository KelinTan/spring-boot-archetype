// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.mapper.primary;

import com.kelin.archetype.base.mybatis.crud.BasicCrudMapper;
import com.kelin.archetype.base.mybatis.crud.MapperTable;
import com.kelin.archetype.common.entity.primary.User;

import java.util.List;

/**
 * @author Kelin Tan
 */
@MapperTable(value = "user", columns = "id,user_name")
public interface UserMapper extends BasicCrudMapper<User> {
    List<User> findByName(String name);
}