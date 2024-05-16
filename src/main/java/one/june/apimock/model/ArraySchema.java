package one.june.apimock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArraySchema implements Schema {
    Schema items;
}
