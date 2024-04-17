import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<Enumeration, Enum<?>> {

    private Class<? extends Enum<?>> enumClass;
    private Enumeration annotation;
    @Override
    public void initialize(Enumeration constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (annotation.optional()) {
            if (value == null) {
                return true; // Optional한 필드면 유효하다고 간주
            }
        } else {
            if (value == null) {
                return false;
            }
        }

        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            if (enumValue.equals(value)) {
                return true;
            }
        }

        return false;
    }
}
