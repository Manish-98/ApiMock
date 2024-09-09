package one.june.apimock.model.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegexTokenTest {

    @Test
    void shouldReturnTrueIfInputDataMatchesTheRegex() {
        RegexToken regexToken = new RegexToken("[0-9]+");
        boolean matchResult = regexToken.matches("12344");

        Assertions.assertTrue(matchResult);
    }

    @Test
    void shouldReturnFalseIfInputDataNotMatchesTheRegex() {
        RegexToken regexToken = new RegexToken("[0-9]+");
        boolean matchResult = regexToken.matches("abc");

        Assertions.assertFalse(matchResult);
    }
}