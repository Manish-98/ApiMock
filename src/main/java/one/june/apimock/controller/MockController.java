package one.june.apimock.controller;

import lombok.AllArgsConstructor;
import one.june.apimock.service.MockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class MockController {
    private final MockService mockService;

    @PostMapping("/api/v1/parse")
    public ResponseEntity<Void> parseOpenApi(@RequestPart MultipartFile openApiDoc) {
        mockService.parseOpenApi(openApiDoc);
        return ResponseEntity.ok().build();
    }

}
