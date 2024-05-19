package one.june.apimock.parser;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import lombok.extern.slf4j.Slf4j;
import one.june.apimock.model.HttpMethod;
import one.june.apimock.model.MockRequest;
import one.june.apimock.model.Schema;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
public class GetSchemaExtractor extends SchemaExtractor {
    @Override
    public MockRequest extract(String path, PathItem pathItem) {
        Operation get = pathItem.getGet();
        if (get == null) return null;

        HashMap<String, Schema> responseCodeSchemas = getSchema(get);
        Optional<String> responseCode = responseCodeSchemas.keySet().stream().findFirst();

        if (responseCode.isPresent())
            return new MockRequest(path, HttpMethod.GET, responseCodeSchemas, responseCode.get());

        log.error("No mock response provided for {} {}", HttpMethod.GET, path);
        return null;
    }
}
