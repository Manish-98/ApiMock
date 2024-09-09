package one.june.apimock.model.token;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class DataToken implements Token {
    String data;

    @Override
    public boolean matches(String data) {
        return Objects.equals(data, this.data);
    }
}
