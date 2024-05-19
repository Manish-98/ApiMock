package one.june.apimock.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResponseMappingRequest {
    @NotBlank(message = "Http method should not be blank")
    HttpMethod httpMethod;

    @NotBlank(message = "Request path should not be blank")
    String path;

    @NotBlank(message = "Response code should not be blank")
    @Pattern(regexp = "^[1-5][0-9]{2}$", message = "Response code should be a number in the range 100 - 599")
    String responseCode;

    String status;
}
