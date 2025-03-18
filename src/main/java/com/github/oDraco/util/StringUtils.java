package com.github.oDraco.util;

public abstract class StringUtils {

    public static String limitLength(String input, int length) {
        return input.substring(0, Math.min(length, input.length()));
    }
}
