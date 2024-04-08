package com.threlease.base.utils.enumeration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<Enumeration, CharSequence> {
    private Enumeration annotation;


    @Override
    public void initialize(Enumeration annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean isValid = false;
        Enum<?>[] enumValues = annotation.enumClass().getEnumConstants();
        for (Enum<?> enumValue : enumValues) {
            if (annotation.ignoreCase()) {
                if (enumValue.name().equalsIgnoreCase(value.toString())) {
                    isValid = true;
                    break;
                }
            } else {
                if (enumValue.name().equals(value.toString())) {
                    isValid = true;
                    break;
                }
            }
        }

        return isValid;
    }
}
