public class EnumToRegex {
    public static String run(Class<? extends Enum<?>> enumClass) {
        StringBuilder regExpBuilder = new StringBuilder();

        // Enum 클래스로부터 모든 상수를 가져와서 처리
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        for (Enum<?> constant : enumConstants) {
            // Enum 상수의 이름을 정규식에 추가
            regExpBuilder.append(constant.name()).append("|");
        }

        // 마지막에 추가된 "|" 제거
        if (regExpBuilder.length() > 0) {
            regExpBuilder.setLength(regExpBuilder.length() - 1);
        }

        // 정규식 반환
        return regExpBuilder.toString();
    }

    public static String getOnePattern(Class<? extends Enum<?>> enumClass) {
        return "^(" + run(enumClass) + ")$";
    }
}
