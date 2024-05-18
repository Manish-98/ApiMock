package one.june.apimock.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import one.june.apimock.model.ArraySchema;
import one.june.apimock.model.ObjectSchema;
import one.june.apimock.model.PrimitiveSchema;
import one.june.apimock.model.Schema;
import one.june.apimock.utils.SchemaUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class JsonGenerator implements DataGenerator<JsonNode> {
    private final ObjectMapper objectMapper;

    @Override
    public JsonNode generate(Schema schema) {
        if (schema instanceof ObjectSchema objectSchema) {
            ObjectNode jsonNode = objectMapper.createObjectNode();
            Map<String, Schema> properties = objectSchema.getProperties();
            properties.forEach((name, propSchema) -> {
                if (propSchema instanceof PrimitiveSchema primitiveSchema) {
                    Object data = SchemaUtils.generatePrimitiveData(primitiveSchema);
                    switch (primitiveSchema.getType()) {
                        case STRING -> jsonNode.put(name, (String) data);
                        case BOOLEAN -> jsonNode.put(name, (Boolean) data);
                        case INTEGER -> jsonNode.put(name, (Integer) data);
                        case NUMBER -> jsonNode.put(name, (Double) data);
                        case UUID -> jsonNode.put(name, data.toString());
                        case NULL, EMPTY -> jsonNode.putNull(name);
                    }
                } else {
                    jsonNode.set(name, generate(propSchema));
                }
            });
            return jsonNode;
        } else if (schema instanceof ArraySchema arraySchema) {
            ArrayNode arrayNode = objectMapper.createArrayNode();
            Schema itemSchema = arraySchema.getItems();
            for (int count = 0; count < 4; count++) {
                if (itemSchema instanceof ObjectSchema innerSchema) {
                    arrayNode.add(generate(innerSchema));
                } else {
                    PrimitiveSchema primitiveSchema = (PrimitiveSchema) itemSchema;
                    Object data = SchemaUtils.generatePrimitiveData(primitiveSchema);
                    switch (primitiveSchema.getType()) {
                        case STRING -> arrayNode.add((String) data);
                        case BOOLEAN -> arrayNode.add((Boolean) data);
                        case INTEGER -> arrayNode.add((Integer) data);
                        case NUMBER -> arrayNode.add((Double) data);
                        case UUID -> arrayNode.add(data.toString());
                        case NULL, EMPTY -> arrayNode.addNull();
                    }
                }
            }
            return arrayNode;
        }

        return null;
    }
}
