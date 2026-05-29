/*
   tiny-common - Tiny Common Helpers
   Copyright 2026 Tarmo Lehtpuu

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package ee.moo.tiny.common.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public final class StringUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private StringUtil() {
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public static String lpad(String s, char pad, int length) {
        if (s == null) {
            s = "";
        }

        var builder = new StringBuilder(s);
        while (builder.length() < length) {
            builder.insert(0, pad);
        }

        return builder.toString();
    }

    public static String rpad(String s, char pad, int length) {
        if (s == null) {
            s = "";
        }

        var builder = new StringBuilder(s);
        while (builder.length() < length) {
            builder.append(pad);
        }

        return builder.toString();
    }

    public static int length(String s) {
        return s == null ? 0 : s.length();
    }

    public static List<String> split(String string, String regex) {
        if (string == null) {
            return List.of();
        }

        return Arrays.stream(string.split(regex))
            .filter(s -> !StringUtil.isEmpty(s))
            .toList();
    }

    public static String capitalize(String s) {
        if (isEmpty(s)) {
            return s;
        }

        var result = new StringBuilder();
        var tokens = s
            .replace("-", " ")
            .replace("_", " ")
            .split(" ");

        for (var word : tokens) {
            if (word.isEmpty()) {
                continue;
            }

            result.append(Character.toUpperCase(word.charAt(0)));

            if (word.length() > 1) {
                result.append(word.substring(1).toLowerCase());
            }

            result.append(" ");
        }

        return trim(result.toString());
    }

    public static String commonPrefix(String s1, String s2) {
        int l = Math.min(s1.length(), s2.length());
        int i = 0;

        while (i < l && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }

        return s1.substring(0, i);
    }

    public static String commonPrefix(List<String> strings) {
        if (strings.isEmpty()) {
            return "";
        }

        var prefix = strings.get(0);
        for (String string : strings) {
            prefix = commonPrefix(prefix, string);
            if (prefix.isEmpty()) {
                break;
            }
        }

        return prefix;
    }

    @SuppressWarnings("ConstantValue")
    public static boolean equals(String s1, String s2) {
        if (s1 != null) {
            return s1.equals(s2);
        }

        if (s2 != null) {
            return s2.equals(s1);
        }

        return true;
    }

    @SuppressWarnings("ConstantValue")
    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 != null) {
            return s1.equalsIgnoreCase(s2);
        }

        if (s2 != null) {
            return s2.equalsIgnoreCase(s1);
        }

        return true;
    }

    public static boolean isAlphaNumeric(String s) {
        return isOnly(s, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
    }

    public static boolean isAlphaNumericUpper(String s) {
        return isOnly(s, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    }

    public static boolean isAlphaNumericLower(String s) {
        return isOnly(s, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    public static boolean isAlpha(String s) {
        return isOnly(s, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    public static boolean isAlphaLower(String s) {
        return isOnly(s, "abcdefghijklmnopqrstuvwxyz");
    }

    public static boolean isAlphaUpper(String s) {
        return isOnly(s, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    public static boolean isNumeric(String s) {
        return isOnly(s, "0123456789");
    }

    public static boolean isNumber(String s) {
        if (isEmpty(s)) {
            return false;
        }

        var len = s.length();
        var i = 0;

        var hasDigits = false;
        var hasDot = false;
        var hasExp = false;

        if (s.charAt(i) == '+' || s.charAt(i) == '-') {
            i++;
        }

        if (i >= len) {
            return false;
        }

        while (i < len) {
            char c = s.charAt(i);

            if (c >= '0' && c <= '9') {
                hasDigits = true;

            } else if (c == '.') {
                if (hasDot || hasExp) {
                    return false;
                }
                hasDot = true;

            } else if (c == 'e' || c == 'E') {
                if (hasExp || !hasDigits) {
                    return false;
                }

                hasExp = true;
                hasDigits = false;

                i++;

                if (i < len && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
                    i++;
                }

                continue;

            } else {
                return false;
            }

            i++;
        }

        return hasDigits;
    }

    public static boolean isHex(String s) {
        if (isEmpty(s)) {
            return false;
        }

        if (s.length() % 2 != 0) {
            return false;
        }

        return isOnly(s, "0123456789ABCDEFabcdef");
    }

    public static boolean isHexLower(String s) {
        if (isEmpty(s)) {
            return false;
        }

        if (s.length() % 2 != 0) {
            return false;
        }

        return isOnly(s, "0123456789abcdef");
    }

    public static boolean isHexUpper(String s) {
        if (isEmpty(s)) {
            return false;
        }

        if (s.length() % 2 != 0) {
            return false;
        }

        return isOnly(s, "0123456789ABCDEF");
    }

    public static boolean isBase64(String s) {
        if (isEmpty(s)) {
            return false;
        }

        if (s.length() % 4 != 0) {
            return false;
        }

        return isOnly(s, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=");
    }

    public static boolean isBase64UrlSafe(String s) {
        if (isEmpty(s)) {
            return false;
        }

        if (s.length() % 4 != 0) {
            return false;
        }

        return isOnly(s, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_");
    }

    public static boolean isOnly(String s, String alphabet) {
        if (isEmpty(s)) {
            return false;
        }

        var allowed = new HashSet<>();
        for (char c : alphabet.toCharArray()) {
            allowed.add(c);
        }

        for (char c : s.toCharArray()) {
            if (!allowed.contains(c)) {
                return false;
            }
        }

        return true;
    }

    public static String randomAlphaNum(int length) {
        return random(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
    }

    public static String randomAlphaNumUpper(int length) {
        return random(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    }

    public static String randomAlphaNumLower(int length) {
        return random(length, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    public static String random(int length, String alphabet) {
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
}