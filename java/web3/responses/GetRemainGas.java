import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetRemainGas {
    private long gasLimit;
    private long gasUsed;
    private long remainGas;
}
