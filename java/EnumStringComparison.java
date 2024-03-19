package com.threlease.base.utils;

public class EnumStringComparison {
    public boolean compareEnumString(String str, Enum<?> value) {
        if (value == null || str == null) {
            return false;
        }

        // 문자열과 열거형(enum) 값 비교
        return str.equals(value.toString());
    }
}
