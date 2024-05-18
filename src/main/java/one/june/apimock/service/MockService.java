package one.june.apimock.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.june.apimock.model.MockRequest;
import one.june.apimock.parser.OpenApiParser;
import one.june.apimock.repository.MockRequestRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class MockService {
    private final OpenApiParser parser;
    private final MockRequestRepository mockRequestRepository;

    public List<MockRequest> parseOpenApi(MultipartFile openApiDoc) {
        String filename = openApiDoc.getOriginalFilename();
        try {
            String extension = Objects.requireNonNull(filename).substring(filename.lastIndexOf('.'));
            File file = File.createTempFile("openApi", extension);
            openApiDoc.transferTo(file);

            String filePath = file.getAbsolutePath();
            log.info("Received file {} for parsing with path {}.", filename, filePath);
            List<MockRequest> mockRequests = parser.parse(filePath);

            return mockRequests.stream().parallel().map(mockRequest -> {
                try {
                    return mockRequestRepository.save(mockRequest);
                } catch (DataIntegrityViolationException e) {
                    log.error("Found duplicate mock requests {}", e.getMessage());
                    return null;
                }
            }).filter(Objects::nonNull).toList();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            log.error("Invalid file: {} due to {} ", filename, e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Error parsing file {} due to {} ", filename, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
