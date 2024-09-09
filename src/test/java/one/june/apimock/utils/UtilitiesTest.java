package one.june.apimock.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UtilitiesTest {

    @Nested
    class GetOrDefault {
        @Test
        void shouldReturnInputData() {
            String data = Utilities.getOrDefault("data", "default");

            Assertions.assertEquals("data", data);
        }

        @Test
        void shouldReturnDefaultValueIfInputDataIsNull() {
            String data = Utilities.getOrDefault(null, "default");

            Assertions.assertEquals("default", data);
        }
    }

}