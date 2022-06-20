// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.mapper.biz

import com.kelin.archetype.database.MapperTable
import com.kelin.archetype.database.entity.biz.IdSharding
import com.kelin.archetype.database.mybatis.crud.BaseCrudMapper

/**
 * @author Kelin Tan
 */
@MapperTable(value = IdShardingMapper.TABLE, columns = IdShardingMapper.COLUMNS, sharding = true, count = 2)
interface IdShardingMapperV2 : BaseCrudMapper<IdSharding> {
}