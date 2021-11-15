// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.database

import com.kelin.archetype.database.config.BizDatabase
import com.kelin.archetype.database.entity.biz.IdSharding
import com.kelin.archetype.database.mapper.biz.IdShardingMapperV2
import com.kelin.archetype.test.KtBaseSpringTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@MockDatabase(name = BizDatabase.NAME, mappers = [IdShardingMapperV2::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class IdShardingMapperV2Test : KtBaseSpringTest() {
    @Autowired
    lateinit var idShardingMapperV2: IdShardingMapperV2

    @Test
    fun `find one sharding`() {
        idShardingMapperV2.findOne(1) verify {
            id eq 1
            data eq "test1"
        }

        idShardingMapperV2.findOne(2) verify {
            id eq 2
            data eq "test2"
        }
    }

    @Test
    fun `insert one sharding`() {
        idShardingMapperV2.insertSelective(IdSharding().apply {
            id = 3
            data = "data3"
        })

        idShardingMapperV2.findOne(3) verify {
            id eq 3
            data eq "data3"
        }
    }
}