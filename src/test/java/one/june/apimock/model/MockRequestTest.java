package one.june.apimock.model;

import one.june.apimock.model.token.DataToken;
import one.june.apimock.model.token.RegexToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class MockRequestTest {

    @Test
    void shouldReturnTrueIfInputRequestPathMatchesMockRequest() {
        MockRequest mockRequest = new MockRequest("/api/v1/users", HttpMethod.GET, List.of(new DataToken("api"), new DataToken("v1"), new DataToken("users")), Map.of(), "200");

        boolean matchResult = mockRequest.matches("/api/v1/users");

        Assertions.assertTrue(matchResult);
    }

    @Test
    void shouldReturnTrueIfInputRequestMatchesMockRequestWithPathParameters() {
        MockRequest mockRequest = new MockRequest("/users/{userId}", HttpMethod.GET, List.of(new DataToken("users"), new RegexToken("[0-9]+")), Map.of(), "200");

        boolean matchResult = mockRequest.matches("/users/1234");

        Assertions.assertTrue(matchResult);
    }

    @Test
    void shouldReturnFalseIfInputRequestNotMatchesMockRequestWithPathParameters() {
        MockRequest mockRequest = new MockRequest("/users/{userId}", HttpMethod.GET, List.of(new DataToken("users"), new RegexToken("[0-9]+")), Map.of(), "200");

        boolean matchResult = mockRequest.matches("/users/abc");

        Assertions.assertFalse(matchResult);
    }

    @Test
    void shouldReturnFalseIfInputRequestSegmentCountIsNotSameAsMockRequestTokenCount() {
        MockRequest mockRequest = new MockRequest("/users/{userId}", HttpMethod.GET, List.of(new DataToken("users"), new RegexToken("[0-9]+")), Map.of(), "200");

        boolean matchResult = mockRequest.matches("/users");

        Assertions.assertFalse(matchResult);
    }
}