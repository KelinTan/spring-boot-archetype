// Copyright 2023 Kelin Inc. All rights reserved.

package com.kelin.archetype.database

import org.testcontainers.containers.MySQLContainer

class MySQLContainerInitializer : MySQLContainer<MySQLContainerInitializer>("mysql:5.7") {
    companion object {
        private var containers: MutableMap<String, MySQLContainerInitializer> = mutableMapOf()

        @JvmStatic
        fun getContainer(databaseName: String): MySQLContainerInitializer {
            if (!containers.containsKey(databaseName)) {
                val databaseContainer = MySQLContainerInitializer()
                    .withDatabaseName(databaseName)
                    .withUsername("user")
                    .withPassword("password")
                    .withUrlParam("useSSL", "false")
                databaseContainer.start()
                containers[databaseName] = databaseContainer
            }
            return containers[databaseName]!!
        }
    }
}