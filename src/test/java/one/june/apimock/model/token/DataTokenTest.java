package one.june.apimock.model.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DataTokenTest {

    @Test
    void shouldReturnTrueIfInputDataIsSameAsTokenData() {
        DataToken data = new DataToken("data");
        boolean matchResult = data.matches("data");
        Assertions.assertTrue(matchResult);
    }

    @Test
    void shouldReturnFalseIfInputDataIsNotSameAsTokenData() {
        DataToken data = new DataToken("other data");
        boolean matchResult = data.matches("data");
        Assertions.assertFalse(matchResult);
    }
}