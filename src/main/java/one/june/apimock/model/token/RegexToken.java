package one.june.apimock.model.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Data
@Slf4j
@AllArgsConstructor
public class RegexToken implements Token {
    String pattern;

    @Override
    public boolean matches(String input) {
        return Pattern.compile(pattern).matcher(input).matches();
    }
}
