package one.june.apimock.controller;

import lombok.AllArgsConstructor;
import one.june.apimock.model.MockRequest;
import one.june.apimock.service.MockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
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
}
