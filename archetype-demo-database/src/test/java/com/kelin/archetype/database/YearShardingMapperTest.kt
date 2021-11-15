// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.database

import com.kelin.archetype.database.config.BizDatabase
import com.kelin.archetype.database.mapper.biz.YearShardingMapper
import com.kelin.archetype.test.KtBaseSpringTest
import com.kelin.archetype.test.database.MockDatabase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@MockDatabase(name = BizDatabase.NAME, mappers = [YearShardingMapper::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class YearShardingMapperTest : KtBaseSpringTest() {
    @Autowired
    lateinit var yearShardingMapper: YearShardingMapper

    @Test
    fun `find by date and id`() {
        yearShardingMapper.findByTimeAndId("2020-01-01", 1) verify {
            id eq 1
            data eq "2020"
            shardingTime eq "2020-01-01"
        }

        yearShardingMapper.findByTimeAndId("2021-01-01", 1) verify {
            id eq 1
            data eq "2021"
        }
    }
}