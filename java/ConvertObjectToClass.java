import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertObjectToClass {
    public <T> T convertObjectToClass(Object data, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(data, clazz);
    }
}
