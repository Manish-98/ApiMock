package one.june.apimock.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import one.june.apimock.model.schema.Schema;
import one.june.apimock.model.token.Token;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Data
@Document
@Slf4j
public class MockRequest {
    @Id
    String id;
    String path;
    HttpMethod httpMethod;
    List<Token> tokens;
    int tokenCount;
    Map<String, Schema> responseCodeSchemas;
    String selectedResponseCode;

    public MockRequest(String path, HttpMethod httpMethod, List<Token> tokens, Map<String, Schema> responseCodeSchemas, String selectedResponseCode) {
        this.id = UUID.randomUUID().toString();
        this.path = path;
        this.httpMethod = httpMethod;
        this.tokens = tokens;
        this.tokenCount = tokens.size();
        this.responseCodeSchemas = responseCodeSchemas;
        this.selectedResponseCode = selectedResponseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockRequest that = (MockRequest) o;
        return Objects.equals(path, that.path) && httpMethod == that.httpMethod && Objects.equals(tokens, that.tokens) && Objects.equals(responseCodeSchemas, that.responseCodeSchemas) && Objects.equals(selectedResponseCode, that.selectedResponseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, httpMethod, tokens, responseCodeSchemas, selectedResponseCode);
    }

    public boolean matches(String requestPath) {
        String[] segments = requestPath.substring(1).split("/");
        log.info("Segments: {}, {}", segments.length, segments);
        for (int index = 0; index < segments.length; index++) {
            if (!tokens.get(index).matches(segments[index])) return false;
        }

        return true;
    }
}
