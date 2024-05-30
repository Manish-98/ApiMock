package one.june.apimock.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Document
public class MockRequest {
    @Id
    String id;
    String path;
    HttpMethod httpMethod;
    List<String> tokens;
    Map<String, Schema> responseCodeSchemas;
    String selectedResponseCode;

    public MockRequest(String path, HttpMethod httpMethod, List<String> tokens, Map<String, Schema> responseCodeSchemas, String selectedResponseCode) {
        this.id = UUID.randomUUID().toString();
        this.path = path;
        this.httpMethod = httpMethod;
        this.tokens = tokens;
        this.responseCodeSchemas = responseCodeSchemas;
        this.selectedResponseCode = selectedResponseCode;
    }
}
