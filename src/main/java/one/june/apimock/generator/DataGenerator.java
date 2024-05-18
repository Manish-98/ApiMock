package one.june.apimock.generator;

import one.june.apimock.model.Schema;

public interface DataGenerator<T> {
    T generate(Schema schema);
}
