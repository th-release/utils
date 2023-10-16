package com.secure.chat.utils;

import java.security.SecureRandom;

public class GetRandom {
    private static final String NUMERIC = "0123456789";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHANUMERIC = NUMERIC + LOWERCASE + UPPERCASE;

    private String getCharacters(String type) {
        return switch (type) {
            case "numeric" -> NUMERIC;
            case "lowercase" -> LOWERCASE;
            case "uppercase" -> UPPERCASE;
            case "all", "alphanumeric" -> ALPHANUMERIC;
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };

//        if (type.equals("numeric")) return NUMERIC;
//        else if (type.equals("lowercase")) return LOWERCASE;
//        else if (type.equals("uppercase")) return UPPERCASE;
//        else if (type.equals("all") || type.equals("alphanumeric")) return ALPHANUMERIC;
//        else throw new IllegalArgumentException("Invalid type: " + type);
    }

    public String getRandom(String type, int length) {
        StringBuilder result = new StringBuilder();
        String characters = getCharacters(type);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            result.append(characters.charAt(randomIndex));
        }

        return result.toString();
    }
}
