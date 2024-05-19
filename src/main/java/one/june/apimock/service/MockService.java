package one.june.apimock.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.june.apimock.exception.MockNotFoundException;
import one.june.apimock.generator.JsonGenerator;
import one.june.apimock.model.HttpMethod;
import one.june.apimock.model.MockRequest;
import one.june.apimock.model.ResponseMappingRequest;
import one.june.apimock.model.Schema;
import one.june.apimock.parser.OpenApiParser;
import one.june.apimock.repository.MockRequestRepository;
import org.apache.commons.lang3.tuple.Pair;
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
    private final JsonGenerator jsonGenerator;
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

    public Pair<String, JsonNode> mockGet(String path) throws MockNotFoundException {
        log.info("Mocking GET request for {}", path);
        MockRequest mockRequest = mockRequestRepository.findByHttpMethodAndPath(HttpMethod.GET, path)
                .orElseThrow(MockNotFoundException::new);
        Schema schema = mockRequest.getResponseCodeSchemas().get(mockRequest.getSelectedResponseCode());

        return Pair.of(mockRequest.getSelectedResponseCode(), jsonGenerator.generate(schema));
    }

    public List<ResponseMappingRequest> updateResponseCodeMapping(List<ResponseMappingRequest> responseMappingRequests) {
        return responseMappingRequests.stream().peek(responseMappingRequest -> {
            MockRequest mockRequest = mockRequestRepository.findByHttpMethodAndPath(
                    responseMappingRequest.getHttpMethod(), responseMappingRequest.getPath()
            ).orElse(null);

            if (mockRequest != null) {
                if (mockRequest.getResponseCodeSchemas().containsKey(responseMappingRequest.getResponseCode())) {
                    mockRequest.setSelectedResponseCode(responseMappingRequest.getResponseCode());
                    mockRequestRepository.save(mockRequest);
                    responseMappingRequest.setStatus("Success");
                } else {
                    responseMappingRequest.setStatus("Invalid response code");
                }
            } else {
                responseMappingRequest.setStatus("Mock request not found");
            }
        }).toList();
    }
}
