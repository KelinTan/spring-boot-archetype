package com.kelin.archetype.test

import com.kelin.archetype.database.MySQLContainerInitializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class MysqlContainerTest {
    @Container
    private val mysqlContainer = MySQLContainerInitializer().withDatabaseName("biz-test")

    @Test
    fun test() {
        assertThat(mysqlContainer.isRunning).isTrue()
    }
}