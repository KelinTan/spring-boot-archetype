package com.alo7.archetype.json;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
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
        Assert.assertEquals(JsonConverter.serialize(object), "{\"id\":1,\"name\":\"test\"}");
    }

    @Test
    public void testDeserialize() {
        TestObject testObject = JsonConverter.deserialize("{\"id\":1,\"name\":\"test\"}", TestObject.class);

        Assert.assertNotNull(testObject);
        Assert.assertSame(testObject.getId(), 1);
        Assert.assertEquals(testObject.getName(), "test");
    }

    @Test
    public void testReadTree() {
        JsonNode jsonNode = JsonConverter.readTree("{\"id\":1,\"name\":\"test\"}");

        Assert.assertNotNull(jsonNode);
        Assert.assertSame(jsonNode.get("id").asInt(), 1);
        Assert.assertEquals(jsonNode.get("name").asText(), "test");
    }

    @Test
    public void testDeserializeList() {
        List<TestObject> testObjects = JsonConverter.deserializeList("[{\"id\":1,\"name\":\"test\"},{\"id\":2,"
                + "\"name\":\"test2\"}]", TestObject.class);

        Assert.assertNotNull(testObjects);
        Assert.assertSame(testObjects.size(), 2);
        Assert.assertEquals(testObjects.get(0).getName(), "test");
        Assert.assertEquals(testObjects.get(1).getName(), "test2");
    }

    @Test
    public void testDeserializeMap() {
        Map<String, String> map = JsonConverter.deserializeMap("{\"id\":1,\"name\":\"test\"}", String.class,
                String.class);

        Assert.assertNotNull(map);
        Assert.assertSame(map.size(), 2);
        Assert.assertEquals(map.get("id"), "1");
        Assert.assertEquals(map.get("name"), "test");
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
