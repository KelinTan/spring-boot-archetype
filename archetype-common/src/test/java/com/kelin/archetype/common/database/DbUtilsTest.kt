package com.kelin.archetype.common.database

import com.kelin.archetype.test.KtTestUtils
import org.junit.Test

/**
 * @author Kelin Tan
 */
class DbUtilsTest : KtTestUtils {
    @Test
    fun `test db utils`() {
        DbUtils.computeDataSourceName("biz") eq "biz.dataSource"
        DbUtils.computeSqlSessionFactoryName("biz") eq "biz.sqlSessionFactory"
        DbUtils.computeSqlSessionTemplateName("biz") eq "biz.sqlSessionTemplate"
        DbUtils.computeTransactionManagerName("biz") eq "biz.transactionManager"
    }
}