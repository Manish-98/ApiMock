package one.june.apimock.parser;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.extern.slf4j.Slf4j;
import one.june.apimock.model.MockRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class OpenApiParser {
    private final List<SchemaExtractor> extractors = List.of(
            new GetSchemaExtractor(),
            new PostSchemaExtractor()
    );

    public List<MockRequest> parse(String filePath) {
        OpenAPIV3Parser parser = new OpenAPIV3Parser();
        OpenAPI openAPI = parser.read(filePath);
        Paths paths = openAPI.getPaths();

        ArrayList<MockRequest> mockRequests = new ArrayList<>();
        paths.forEach((String path, PathItem pathItem) -> extractors.forEach(extractor -> {
            MockRequest mockRequest = extractor.extract(path, pathItem);
            if (mockRequest != null) mockRequests.add(mockRequest);
        }));

        log.info("Found {} APIs to be mocked for file {} ", mockRequests.size(), filePath);
        return mockRequests;
    }
}
