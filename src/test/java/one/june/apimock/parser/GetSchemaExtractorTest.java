package one.june.apimock.parser;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import one.june.apimock.model.HttpMethod;
import one.june.apimock.model.MockRequest;
import one.june.apimock.model.schema.PrimitiveSchema;
import one.june.apimock.model.Type;
import one.june.apimock.model.token.DataToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class GetSchemaExtractorTest {

    @Test
    void shouldReturnNullIfNoGetRequestAvailable() {
        GetSchemaExtractor schemaExtractor = new GetSchemaExtractor();

        MockRequest mockRequest = schemaExtractor.extract("/segment1/segment2", new PathItem());

        Assertions.assertNull(mockRequest);
    }

    @Test
    void shouldReturnNullIfNoResponseAvailable() {
        GetSchemaExtractor schemaExtractor = new GetSchemaExtractor();
        String path = "/segment1/segment2";
        PathItem pathItem = new PathItem();
        Operation operation = new Operation();
        ApiResponses responses = new ApiResponses();
        MediaType type = new MediaType();
        type.setSchema(new StringSchema());
        operation.setResponses(responses);
        pathItem.setGet(operation);

        MockRequest mockRequest = schemaExtractor.extract(path, pathItem);

        Assertions.assertNull(mockRequest);
    }

    @Test
    void shouldReturnMockRequestForGivenGetRequest() {
        GetSchemaExtractor schemaExtractor = new GetSchemaExtractor();
        String path = "/segment1/segment2";
        PathItem pathItem = new PathItem();
        Operation operation = new Operation();
        ApiResponses responses = new ApiResponses();
        ApiResponse apiResponse = new ApiResponse();
        Content content = new Content();
        MediaType type = new MediaType();
        type.setSchema(new StringSchema());
        content.put("application/json", type);
        apiResponse.setContent(content);
        responses.addApiResponse("200", apiResponse);
        operation.setResponses(responses);
        pathItem.setGet(operation);

        MockRequest mockRequest = schemaExtractor.extract(path, pathItem);

        MockRequest expected = new MockRequest("/segment1/segment2", HttpMethod.GET, List.of(new DataToken("segment1"), new DataToken("segment2")), Map.of("200", new PrimitiveSchema(Type.STRING)), "200");
        Assertions.assertEquals(expected, mockRequest);
    }
}