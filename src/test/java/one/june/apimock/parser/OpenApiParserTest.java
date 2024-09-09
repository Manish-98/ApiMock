package one.june.apimock.parser;

import one.june.apimock.model.HttpMethod;
import one.june.apimock.model.MockRequest;
import one.june.apimock.model.Type;
import one.june.apimock.model.schema.ArraySchema;
import one.june.apimock.model.schema.ObjectSchema;
import one.june.apimock.model.schema.PrimitiveSchema;
import one.june.apimock.model.token.DataToken;
import one.june.apimock.model.token.RegexToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Map;

class OpenApiParserTest {

    @Test
    void shouldReturnParsedOpenApiMockRequests() {
        String openApiDocPath = new ClassPathResource("static/openApiDoc.json").getPath();
        OpenApiParser openApiParser = new OpenApiParser();

        List<MockRequest> mockRequests = openApiParser.parse(openApiDocPath);

        List<MockRequest> expected = List.of(
                new MockRequest("/users", HttpMethod.GET, List.of(new DataToken("users")), Map.of("200", new ArraySchema(new ObjectSchema(Map.of("id", new PrimitiveSchema(Type.INTEGER), "username", new PrimitiveSchema(Type.STRING), "email", new PrimitiveSchema(Type.STRING))))), "200"),
                new MockRequest("/users", HttpMethod.POST, List.of(new DataToken("users")), Map.of("201", new ObjectSchema(Map.of("id", new PrimitiveSchema(Type.INTEGER), "username", new PrimitiveSchema(Type.STRING), "email", new PrimitiveSchema(Type.STRING)))), "201"),
                new MockRequest("/users/{userId}", HttpMethod.GET, List.of(new DataToken("users"), new RegexToken("[0-9]+")), Map.of("200", new ObjectSchema(Map.of("id", new PrimitiveSchema(Type.INTEGER), "username", new PrimitiveSchema(Type.STRING), "email", new PrimitiveSchema(Type.STRING))), "404", new PrimitiveSchema(Type.EMPTY)), "200")
        );
        Assertions.assertEquals(expected, mockRequests);
    }

    @Test
    void shouldThrowExceptionWhenDocumentPathInvalid() {
        String openApiDocPath = new ClassPathResource("static/invalid.json").getPath();
        OpenApiParser openApiParser = new OpenApiParser();

        Assertions.assertThrows(NullPointerException.class, () -> openApiParser.parse(openApiDocPath));
    }

    @Test
    void shouldThrowExceptionWhenDocumentInvalid() {
        String openApiDocPath = new ClassPathResource("static/invalidApiDoc").getPath();
        OpenApiParser openApiParser = new OpenApiParser();

        Assertions.assertThrows(NullPointerException.class, () -> openApiParser.parse(openApiDocPath));
    }
}