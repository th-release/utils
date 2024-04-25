import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QueryParameter {
    private String name;
    private Object value;
}
