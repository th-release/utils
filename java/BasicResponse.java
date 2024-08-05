// compileOnly 'org.projectlombok:lombok'
// annotationProcessor 'org.projectlombok:lombok'
// com.google.gson.Gson

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Data
@Getter
@Setter
@Builder
public class BasicResponse {
    private boolean success;
    private Optional<String> message;
    private Optional<Object> data;

    public String toJson() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}
