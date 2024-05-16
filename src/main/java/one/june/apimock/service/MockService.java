package one.june.apimock.service;

import lombok.AllArgsConstructor;
import one.june.apimock.model.MockRequest;
import one.june.apimock.parser.OpenApiParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class MockService {
    private final OpenApiParser parser;

    public List<MockRequest> parseOpenApi(MultipartFile openApiDoc) {
        try {
            String extension = openApiDoc.getOriginalFilename().substring(openApiDoc.getOriginalFilename().lastIndexOf('.'));
            File file = File.createTempFile("openApi", extension);
            openApiDoc.transferTo(file);

            return parser.parse(file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
