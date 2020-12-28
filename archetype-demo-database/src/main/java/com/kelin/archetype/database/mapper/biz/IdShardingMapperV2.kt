// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database.mapper.biz

import com.kelin.archetype.common.database.MapperTable
import com.kelin.archetype.core.mybatis.crud.BaseCrudMapper
import com.kelin.archetype.database.entity.biz.IdSharding

/**
 * @author Kelin Tan
 */
@MapperTable(value = IdShardingMapper.TABLE, columns = IdShardingMapper.COLUMNS, sharding = true, count = 2)
interface IdShardingMapperV2 : BaseCrudMapper<IdSharding> {
}