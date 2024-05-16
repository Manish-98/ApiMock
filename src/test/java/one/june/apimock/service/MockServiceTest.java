package one.june.apimock.service;

import one.june.apimock.model.ArraySchema;
import one.june.apimock.model.HttpMethod;
import one.june.apimock.model.MockRequest;
import one.june.apimock.model.ObjectSchema;
import one.june.apimock.model.PrimitiveSchema;
import one.june.apimock.model.Schema;
import one.june.apimock.model.Type;
import one.june.apimock.parser.OpenApiParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MockServiceTest {

    @Test
    void shouldParseOpenApiDoc() throws IOException {
        MockService mockService = new MockService(new OpenApiParser());

        ClassPathResource resource = new ClassPathResource("static/openApiDoc.json");
        byte[] fileContent = Files.readAllBytes(Paths.get(resource.getURI()));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("openApiDoc.json", "openApiDoc.json", "application/json", fileContent);

        List<MockRequest> mockRequests = mockService.parseOpenApi(mockMultipartFile);
        HashMap<String, Schema> schema = new HashMap<>();

        schema.put("id", new PrimitiveSchema(Type.INTEGER));
        schema.put("email", new PrimitiveSchema(Type.STRING));
        schema.put("username", new PrimitiveSchema(Type.STRING));
        MockRequest mockRequestOne = new MockRequest("/users", HttpMethod.GET, Map.of("200", new ArraySchema(new ObjectSchema(schema))));
        MockRequest mockRequestTwo = new MockRequest("/users", HttpMethod.POST, Map.of("201", new ObjectSchema(schema)));
        MockRequest mockRequestThree = new MockRequest("/users/{userId}", HttpMethod.GET, Map.of("200", new ObjectSchema(schema), "404", new PrimitiveSchema(Type.EMPTY)));
        Assertions.assertEquals(List.of(mockRequestOne, mockRequestTwo, mockRequestThree), mockRequests);
    }
}