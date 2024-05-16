package one.june.apimock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ObjectSchema implements Schema {
    Map<String, Schema> properties;
}
