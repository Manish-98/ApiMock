package one.june.apimock.parser;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import one.june.apimock.model.HttpMethod;
import one.june.apimock.model.MockRequest;
import one.june.apimock.model.Schema;

import java.util.HashMap;

public class GetSchemaExtractor extends SchemaExtractor {
    @Override
    public MockRequest extract(String path, PathItem pathItem) {
        Operation get = pathItem.getGet();
        if (get == null) return null;

        HashMap<String, Schema> responseCodeSchemas = getSchema(get);
        return new MockRequest(path, HttpMethod.GET, responseCodeSchemas);
    }
}
