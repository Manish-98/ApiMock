package one.june.apimock.utils;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import one.june.apimock.model.PrimitiveSchema;
import one.june.apimock.model.Schema;

import java.util.HashMap;

import static one.june.apimock.model.Type.BOOLEAN;
import static one.june.apimock.model.Type.INTEGER;
import static one.june.apimock.model.Type.NULL;
import static one.june.apimock.model.Type.NUMBER;
import static one.june.apimock.model.Type.STRING;
import static one.june.apimock.model.Type.UUID;

public class SchemaUtils {
    public Schema from(io.swagger.v3.oas.models.media.Schema<?> inputSchema) {
        if (inputSchema instanceof StringSchema) {
            return new PrimitiveSchema(STRING);
        } else if (inputSchema instanceof BooleanSchema) {
            return new PrimitiveSchema(BOOLEAN);
        } else if (inputSchema instanceof IntegerSchema) {
            return new PrimitiveSchema(INTEGER);
        } else if (inputSchema instanceof NumberSchema) {
            return new PrimitiveSchema(NUMBER);
        } else if (inputSchema instanceof UUIDSchema) {
            return new PrimitiveSchema(UUID);
        } else if (inputSchema instanceof ObjectSchema objectSchema) {
            HashMap<String, Schema> props = new HashMap<>();
            objectSchema.getProperties().forEach((name, schema) -> props.put(name, from(schema)));
            return new one.june.apimock.model.ObjectSchema(props);
        } else if (inputSchema instanceof ArraySchema arraySchema) {
            Schema itemsSchema = from(arraySchema.getItems());
            return new one.june.apimock.model.ArraySchema(itemsSchema);
        }

        return new PrimitiveSchema(NULL);
    }
}
