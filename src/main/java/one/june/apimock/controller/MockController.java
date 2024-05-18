package one.june.apimock.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import one.june.apimock.exception.MockNotFoundException;
import one.june.apimock.model.MockRequest;
import one.june.apimock.service.MockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class MockController {
    private final MockService mockService;

    @PostMapping("/api/v1/parse")
    public ResponseEntity<List<MockRequest>> parseOpenApi(@RequestPart MultipartFile openApiDoc) {
        return ResponseEntity.ok().body(mockService.parseOpenApi(openApiDoc));
    }

    @GetMapping("/**")
    public ResponseEntity<JsonNode> mockGet(HttpServletRequest request) throws MockNotFoundException {
        JsonNode response = mockService.mockGet(request.getRequestURI());
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(MockNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerMockNotFoundException(MockNotFoundException ignoredE) {
    }
}
