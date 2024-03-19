package com.threlease.base.utils;

public class EnumStringComparison {
    public static <T extends Enum<T>> boolean compareEnumString(String str, Class<T> enumClass) {
        if (enumClass == null || str == null) {
            return false;
        }

        for (T enumValue : enumClass.getEnumConstants()) {
            if (str.equals(enumValue.toString())) {
                return true; // 문자열과 일치하는 Enum 값을 찾으면 true 반환
            }
        }
        return false; // 일치하는 Enum 값이 없으면 false 반환
    }
}
