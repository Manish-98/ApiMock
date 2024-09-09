package one.june.apimock.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.june.apimock.model.Type;
import one.june.apimock.model.schema.ArraySchema;
import one.june.apimock.model.schema.ObjectSchema;
import one.june.apimock.model.schema.PrimitiveSchema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class JsonGeneratorTest {
    private final JsonGenerator jsonGenerator = new JsonGenerator(new ObjectMapper());

    @Test
    void shouldGenerateJsonDataForObjectSchema() {
        JsonNode jsonData = jsonGenerator.generate(new ObjectSchema(Map.of("key1", new PrimitiveSchema(Type.STRING))));

        Assertions.assertTrue(jsonData.at("/key1").isTextual());
    }

    @Test
    void shouldGenerateJsonDataForNestedObjectSchema() {
        JsonNode jsonData = jsonGenerator.generate(new ObjectSchema(Map.of("key1", new ObjectSchema(Map.of("innerKey", new PrimitiveSchema(Type.STRING))))));

        Assertions.assertTrue(jsonData.at("/key1").isObject());
        Assertions.assertTrue(jsonData.at("/key1/innerKey").isTextual());
    }

    @Test
    void shouldGenerateJsonDataForArraySchemaWithPrimitiveData() {
        JsonNode jsonData = jsonGenerator.generate(new ArraySchema(new PrimitiveSchema(Type.STRING)));

        Assertions.assertTrue(jsonData.isArray());
        Assertions.assertEquals(4, jsonData.size());
        jsonData.elements().forEachRemaining(data -> Assertions.assertTrue(data.isTextual()));
    }

    @Test
    void shouldGenerateJsonDataForArraySchemaWithObjectData() {
        JsonNode jsonData = jsonGenerator.generate(new ArraySchema(new ObjectSchema(Map.of("key1", new PrimitiveSchema(Type.STRING)))));

        Assertions.assertTrue(jsonData.isArray());
        Assertions.assertEquals(4, jsonData.size());
        jsonData.elements().forEachRemaining(data -> {
            Assertions.assertTrue(data.isObject());
            Assertions.assertTrue(data.at("/key1").isTextual());
        });
    }

    @Test
    void shouldReturnNullWhenSchemaTypeIsPrimitive() {
        JsonNode jsonData = jsonGenerator.generate(new PrimitiveSchema(Type.NULL));

        Assertions.assertNull(jsonData);
    }
}