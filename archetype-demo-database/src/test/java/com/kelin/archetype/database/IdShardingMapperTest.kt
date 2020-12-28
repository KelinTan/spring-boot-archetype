// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database

import com.kelin.archetype.database.config.BizDatabase
import com.kelin.archetype.database.mapper.biz.IdShardingMapper
import com.kelin.archetype.test.KtBaseSpringTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@MockDatabase(name = BizDatabase.NAME, mappers = [IdShardingMapper::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class IdShardingMapperTest : KtBaseSpringTest() {
    @Autowired
    lateinit var idShardingMapper: IdShardingMapper

    @Test
    fun `find by id sharding`() {
        idShardingMapper.findById(1) verify {
            id eq 1
            data eq "test1"
        }

        idShardingMapper.findById(2) verify {
            id eq 2
            data eq "test2"
        }
    }

    @Test
    fun `insert by id sharding`() {
        idShardingMapper.insert(3, "data3")

        idShardingMapper.findById(3) verify {
            data eq "data3"
        }
    }

    @Test
    fun `update by id sharding`() {
        idShardingMapper.updateById(1, "update")

        idShardingMapper.findById(1) verify {
            data eq "update"
        }
    }

    @Test
    fun `delete by id sharding`() {
        idShardingMapper.findById(2) not null
        idShardingMapper.deleteById(2)
        idShardingMapper.findById(2) eq null
    }
}