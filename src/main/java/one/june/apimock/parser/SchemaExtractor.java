package one.june.apimock.parser;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import one.june.apimock.model.MockRequest;
import one.june.apimock.model.PrimitiveSchema;
import one.june.apimock.model.Schema;
import one.june.apimock.model.Type;
import one.june.apimock.utils.SchemaUtils;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static one.june.apimock.utils.RegexPatterns.PATH_PARAMETER_PATTERN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public abstract class SchemaExtractor {
    protected static HashMap<String, Schema> getSchema(Operation post) {
        HashMap<String, Schema> responseCodeSchemas = new HashMap<>();
        post.getResponses().forEach(((String responseCode, ApiResponse apiResponse) -> {
            Content content = apiResponse.getContent();
            if (content == null) {
                responseCodeSchemas.put(responseCode, new PrimitiveSchema(Type.EMPTY));
            } else {
                MediaType mediaType = content.getOrDefault(APPLICATION_JSON_VALUE, new MediaType());
                responseCodeSchemas.put(responseCode, SchemaUtils.from(mediaType.getSchema()));
            }
        }));
        return responseCodeSchemas;
    }

    protected static List<String> getTokens(String path, Operation operation) {
        List<Parameter> parameters = operation.getParameters();
        if (parameters == null) return List.of();

        Map<String, String> pathPattern = parameters.stream()
                .filter(parameter -> Objects.equals(parameter.getIn(), "path"))
                .map(parameter -> Pair.of(parameter.getName(), SchemaUtils.patternFrom(parameter.getSchema())))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

        String[] tokens = path.substring(1).split("/");
        return Arrays.stream(tokens).map(token -> {
            if (Pattern.matches(PATH_PARAMETER_PATTERN, token)) return pathPattern.get(token.substring(1, token.length() - 1));
            else return token;
        }).toList();
    }

    abstract MockRequest extract(String path, PathItem pathItem);
}
