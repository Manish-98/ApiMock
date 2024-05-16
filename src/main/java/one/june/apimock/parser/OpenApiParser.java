package one.june.apimock.parser;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;
import one.june.apimock.model.HttpMethod;
import one.june.apimock.model.MockRequest;
import one.june.apimock.model.PrimitiveSchema;
import one.june.apimock.model.Schema;
import one.june.apimock.model.Type;
import one.june.apimock.utils.SchemaUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class OpenApiParser {
    public List<MockRequest> parse(String filePath) {
        OpenAPIV3Parser parser = new OpenAPIV3Parser();
        OpenAPI openAPI = parser.read(filePath);
        Paths paths = openAPI.getPaths();

        ArrayList<MockRequest> mockRequests = new ArrayList<>();
        paths.forEach((String path, PathItem pathItem) -> {
            Operation get = pathItem.getGet();
            HashMap<String, Schema> responseCodeSchemas = new HashMap<>();
            get.getResponses().forEach(((String responseCode, ApiResponse apiResponse) -> {
                Content content = apiResponse.getContent();
                if (content == null) {
                    responseCodeSchemas.put(responseCode, new PrimitiveSchema(Type.EMPTY));
                } else {
                    MediaType mediaType = content.getOrDefault(APPLICATION_JSON_VALUE, new MediaType());
                    responseCodeSchemas.put(responseCode, new SchemaUtils().from(mediaType.getSchema()));
                }
            }));
            mockRequests.add(new MockRequest(path, HttpMethod.GET, responseCodeSchemas));
        });

        return mockRequests;
    }
}
