package com.kelin.archetype.common.database

import com.kelin.archetype.test.KtTestUtils
import org.junit.Test

/**
 * @author Kelin Tan
 */
class DbFactoryTest: KtTestUtils {
    @Test
    fun `test db factory`() {
        DbFactory.computeDataSourceName("biz") eq "biz.dataSource"
        DbFactory.computeSqlSessionFactoryName("biz") eq "biz.sqlSessionFactory"
        DbFactory.computeSqlSessionTemplateName("biz") eq "biz.sqlSessionTemplate"
        DbFactory.computeTransactionManagerName("biz") eq "biz.transactionManager"
    }
}