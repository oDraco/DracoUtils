package com.github.oDraco.util;

import java.util.Map;

public abstract class StringUtils {

    public static String limitLength(String input, int length) {
        return input.substring(0, Math.min(length, input.length()));
    }

    public static String replace(String input, Map<String, String> replaces) {
        for (Map.Entry<String, String> entry : replaces.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }
        return input;
    }
}
