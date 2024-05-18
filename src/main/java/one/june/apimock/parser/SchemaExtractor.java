package one.june.apimock.parser;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import one.june.apimock.model.MockRequest;
import one.june.apimock.model.PrimitiveSchema;
import one.june.apimock.model.Schema;
import one.june.apimock.model.Type;
import one.june.apimock.utils.SchemaUtils;

import java.util.HashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public abstract class SchemaExtractor {
    protected static HashMap<String, Schema> getSchema(Operation post) {
        HashMap<String, Schema> responseCodeSchemas = new HashMap<>();
        post.getResponses().forEach(((String responseCode, ApiResponse apiResponse) -> {
            Content content = apiResponse.getContent();
            if (content == null) {
                responseCodeSchemas.put(responseCode, new PrimitiveSchema(Type.EMPTY));
            } else {
                MediaType mediaType = content.getOrDefault(APPLICATION_JSON_VALUE, new MediaType());
                responseCodeSchemas.put(responseCode, SchemaUtils.from(mediaType.getSchema()));
            }
        }));
        return responseCodeSchemas;
    }
    abstract MockRequest extract(String path, PathItem pathItem);
}
