package org.example.util;

import io.micrometer.core.instrument.util.StringUtils;

public class CharacterUtil {

    public static String toLowerFirstLetter(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
        }
        return str;
    }
}
