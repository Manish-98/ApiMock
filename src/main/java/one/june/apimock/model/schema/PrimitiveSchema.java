package one.june.apimock.model.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import one.june.apimock.model.Type;

@Data
@AllArgsConstructor
public class PrimitiveSchema implements Schema {
    Type type;
}
