package io.github.komet0141.simpleJson.util;


public abstract class StringUtils {
    public static String repeat(String s, int count) {
        String result = "";
        for (int i = 0; i < count; i++) result += s;
        return result;
    }
}
