package one.june.apimock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class MockRequest {
    String path;
    HttpMethod httpMethod;
    Map<String, Schema> responseCodeSchemas;
}
