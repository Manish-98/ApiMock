package one.june.apimock.generator;

import one.june.apimock.model.schema.Schema;

public interface DataGenerator<T> {
    T generate(Schema schema);
}
