package one.june.apimock.utils;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import one.june.apimock.model.schema.PrimitiveSchema;
import one.june.apimock.model.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

class SchemaUtilsTest {

    @Nested
    class SchemaFromOpenApi {
        @Test
        void shouldReturnPrimitiveSchemaForStringDataType() {
            StringSchema stringSchema = new StringSchema();
            Assertions.assertEquals(new PrimitiveSchema(Type.STRING), SchemaUtils.from(stringSchema));
        }

        @Test
        void shouldReturnPrimitiveSchemaForBooleanDataType() {
            BooleanSchema booleanSchema = new BooleanSchema();
            Assertions.assertEquals(new PrimitiveSchema(Type.BOOLEAN), SchemaUtils.from(booleanSchema));
        }

        @Test
        void shouldReturnPrimitiveSchemaForNumberDataType() {
            NumberSchema numberSchema = new NumberSchema();
            Assertions.assertEquals(new PrimitiveSchema(Type.NUMBER), SchemaUtils.from(numberSchema));
        }

        @Test
        void shouldReturnPrimitiveSchemaForIntegerDataType() {
            IntegerSchema integerSchema = new IntegerSchema();
            Assertions.assertEquals(new PrimitiveSchema(Type.INTEGER), SchemaUtils.from(integerSchema));
        }

        @Test
        void shouldReturnPrimitiveSchemaForUuidDataType() {
            UUIDSchema uuidSchema = new UUIDSchema();
            Assertions.assertEquals(new PrimitiveSchema(Type.UUID), SchemaUtils.from(uuidSchema));
        }

        @Test
        void shouldReturnObjectSchemaForObjectDataType() {
            ObjectSchema objectSchema = new ObjectSchema();
            objectSchema.setProperties(Map.of("key1", new StringSchema(), "key2", new BooleanSchema()));
            one.june.apimock.model.schema.ObjectSchema expectedSchema = new one.june.apimock.model.schema.ObjectSchema(Map.of(
                    "key1", new PrimitiveSchema(Type.STRING),
                    "key2", new PrimitiveSchema(Type.BOOLEAN)
            ));

            Assertions.assertEquals(expectedSchema, SchemaUtils.from(objectSchema));
        }

        @Test
        void shouldReturnArraySchemaForArrayDataType() {
            ArraySchema arraySchema = new ArraySchema();
            arraySchema.setItems(new StringSchema());
            one.june.apimock.model.schema.ArraySchema expectedSchema = new one.june.apimock.model.schema.ArraySchema(new PrimitiveSchema(Type.STRING));

            Assertions.assertEquals(expectedSchema, SchemaUtils.from(arraySchema));
        }
    }

    @Nested
    class PatternFromOpenApiSchema {
        @Test
        void shouldReturnAlphanumericRegexPatternForStringSchema() {
            StringSchema stringSchema = new StringSchema();
            Assertions.assertEquals("[a-zA-Z0-9]+", SchemaUtils.patternFrom(stringSchema));
        }

        @Test
        void shouldReturnBooleanRegexForBooleanSchema() {
            BooleanSchema booleanSchema = new BooleanSchema();
            Assertions.assertEquals("(true|false)", SchemaUtils.patternFrom(booleanSchema));
        }

        @Test
        void shouldReturnIntegerRegexForIntegerSchema() {
            IntegerSchema integerSchema = new IntegerSchema();
            Assertions.assertEquals("[0-9]+", SchemaUtils.patternFrom(integerSchema));
        }

        @Test
        void shouldReturnDecimalRegexForNumberSchema() {
            NumberSchema numberSchema = new NumberSchema();
            Assertions.assertEquals("[0-9]+(.[0-9]+)?", SchemaUtils.patternFrom(numberSchema));
        }

        @Test
        void shouldReturnUuidRegexForUuidSchema() {
            UUIDSchema uuidSchema = new UUIDSchema();
            Assertions.assertEquals("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", SchemaUtils.patternFrom(uuidSchema));
        }

        @Test
        void shouldReturnAllMatchRegexForUnknownSchema() {
            BinarySchema binarySchema = new BinarySchema();
            Assertions.assertEquals(".*", SchemaUtils.patternFrom(binarySchema));
        }
    }

    @Nested
    class GeneratePrimitiveData {
        @Test
        void shouldReturnRandomStringForStringSchema() {
            Object data = SchemaUtils.generatePrimitiveData(new PrimitiveSchema(Type.STRING));
            Assertions.assertTrue(data instanceof String);
        }

        @Test
        void shouldReturnRandomBooleanForBooleanSchema() {
            Object data = SchemaUtils.generatePrimitiveData(new PrimitiveSchema(Type.BOOLEAN));
            Assertions.assertTrue(data instanceof Boolean);
        }

        @Test
        void shouldReturnRandomDecimalForNumberSchema() {
            Object data = SchemaUtils.generatePrimitiveData(new PrimitiveSchema(Type.NUMBER));
            Assertions.assertTrue(data instanceof Double);
        }

        @Test
        void shouldReturnRandomIntegerForIntegerSchema() {
            Object data = SchemaUtils.generatePrimitiveData(new PrimitiveSchema(Type.INTEGER));
            Assertions.assertTrue(data instanceof Integer);
        }

        @Test
        void shouldReturnRandomUuidForUuidSchema() {
            Object data = SchemaUtils.generatePrimitiveData(new PrimitiveSchema(Type.UUID));
            Assertions.assertTrue(data instanceof UUID);
        }

        @Test
        void shouldReturnNullForNullSchema() {
            Object data = SchemaUtils.generatePrimitiveData(new PrimitiveSchema(Type.NULL));
            Assertions.assertNull(data);
        }

        @Test
        void shouldReturnNullForEmptySchema() {
            Object data = SchemaUtils.generatePrimitiveData(new PrimitiveSchema(Type.EMPTY));
            Assertions.assertNull(data);
        }
    }
}