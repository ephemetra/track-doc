package io.github.core;

public class CommonUtils {

    private CommonUtils() {
    }

    public static String defaultIfBlank(String value, String defaultValue) {
        return value != null && !value.trim().isEmpty() ? value : defaultValue;
    }
}
