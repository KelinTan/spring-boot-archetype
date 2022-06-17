// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.json

import com.fasterxml.jackson.core.type.TypeReference
import com.kelin.archetype.beans.rest.RestResponse
import com.kelin.archetype.test.KtTestUtils
import org.junit.jupiter.api.Test

/**
 * @author Kelin Tan
 */
class JsonConverterTest : KtTestUtils {
    @Test
    fun testSerialize() {
        JsonConverter.serialize(TestObject().apply {
            id = 1
            name = "test"
        }) eq """{"id":1,"name":"test"}"""
    }

    @Test
    fun testDeserialize() {
        JsonConverter.deserialize("""{"id":1,"name":"test"}""", TestObject::class.java) verify {
            id eq 1
            name eq "test"
        }
    }

    @Test
    fun testReadTree() {
        JsonConverter.readTree("""{"id":1,"name":"test"}""") verify {
            get("id").asInt() eq 1
            get("name").asText() eq "test"
        }
    }

    @Test
    fun testDeserializeList() {
        JsonConverter.deserializeList(
            """[{"id":1,"name":"test"},{"id":2,"name":"test2"}]"""", TestObject::class.java
        ) verify {
            size eq 2
            item(0) verify {
                name eq "test"
            }
            item(1) verify {
                name eq "test2"
            }
        }
    }

    @Test
    fun testDeserializeMap() {
        JsonConverter.deserializeMap(
            """{"id":1,"name":"test"}""",
            String::class.java,
            String::class.java
        ) verify {
            size eq 2
            get("id") eq "1"
            get("name") eq "test"
        }
    }

    @Test
    fun testDeserializeGeneric() {
        JsonConverter.deserializeGenerics(
            """{"result":{"id":1}}""",
            object : TypeReference<RestResponse<TestObject>>() {}
        ) verify {
            result verify {
                id eq 1
            }
        }
    }

    @Test
    fun testSerializeAsBytes() {
        JsonConverter.serializeAsBytes(TestObject().apply {
            id = 1
            name = "test"
        }) verify {
            size greater 0
        }
    }


    private class TestObject {
        var id: Int? = null
        var name: String? = null
    }
}