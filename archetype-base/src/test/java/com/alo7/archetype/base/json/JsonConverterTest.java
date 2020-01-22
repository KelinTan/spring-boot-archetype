// Copyright 2019 Alo7 Inc. All rights reserved.

package com.alo7.archetype.base.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class JsonConverterTest {
    @Test
    public void testSerialize() {
        TestObject object = TestObject.builder()
                .id(1)
                .name("test")
                .build();
        assertEquals(JsonConverter.serialize(object), "{\"id\":1,\"name\":\"test\"}");
    }

    @Test
    public void testDeserialize() {
        TestObject testObject = JsonConverter.deserialize("{\"id\":1,\"name\":\"test\"}", TestObject.class);

        assertNotNull(testObject);
        assertSame(testObject.getId(), 1);
        assertEquals(testObject.getName(), "test");
    }

    @Test
    public void testReadTree() {
        JsonNode jsonNode = JsonConverter.readTree("{\"id\":1,\"name\":\"test\"}");

        assertNotNull(jsonNode);
        assertSame(jsonNode.get("id").asInt(), 1);
        assertEquals(jsonNode.get("name").asText(), "test");
    }

    @Test
    public void testDeserializeList() {
        List<TestObject> testObjects = JsonConverter.deserializeList("[{\"id\":1,\"name\":\"test\"},{\"id\":2,"
                + "\"name\":\"test2\"}]", TestObject.class);

        assertNotNull(testObjects);
        assertSame(testObjects.size(), 2);
        assertEquals(testObjects.get(0).getName(), "test");
        assertEquals(testObjects.get(1).getName(), "test2");
    }

    @Test
    public void testDeserializeMap() {
        Map<String, String> map = JsonConverter.deserializeMap("{\"id\":1,\"name\":\"test\"}", String.class,
                String.class);

        assertNotNull(map);
        assertSame(map.size(), 2);
        assertEquals(map.get("id"), "1");
        assertEquals(map.get("name"), "test");
    }

    @Test
    public void testSerializeAsBytes() {
        TestObject object = TestObject.builder()
                .id(1)
                .name("test")
                .build();
        byte[] bytes = JsonConverter.serializeAsBytes(object);

        assertTrue(bytes != null && bytes.length > 0);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TestObject {
        private Integer id;
        private String name;
    }
}
