package one.june.apimock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrimitiveSchema implements Schema {
    Type type;
}
