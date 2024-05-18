package one.june.apimock.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.UUID;

@Data
@Document
public class MockRequest {
    @Id
    String id;
    String path;
    HttpMethod httpMethod;
    Map<String, Schema> responseCodeSchemas;

    public MockRequest(String path, HttpMethod httpMethod, Map<String, Schema> responseCodeSchemas) {
        this.id = UUID.randomUUID().toString();
        this.path = path;
        this.httpMethod = httpMethod;
        this.responseCodeSchemas = responseCodeSchemas;
    }
}
