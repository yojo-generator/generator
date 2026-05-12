package ru.yojo.codegen.util;

/**
 * Strategy for converting names between different formats (YAML field names to Java identifiers).
 * <p>
 * Encapsulates naming conversion logic that was previously scattered in {@link MapperUtil}.
 * This class is stateless and thread-safe.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class NamingStrategy {

    /**
     * Converts a raw field name (e.g., {@code "user-name", "user_name"}) to valid Java field identifier (camelCase).
     * Respects keyword safety.
     *
     * @param rawName raw name from YAML
     * @return safe camelCase name
     */
    public String toValidJavaFieldName(String rawName) {
        if (rawName == null || rawName.isEmpty()) {
            return rawName;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperNext = false;
        for (int i = 0; i < rawName.length(); i++) {
            char c = rawName.charAt(i);
            if (c == '-' || c == '_' || c == ' ') {
                upperNext = true;
            } else if (upperNext) {
                sb.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                sb.append(c);
            }
        }
        return safeFieldName(sb.toString());
    }

    /**
     * Converts the first character to title case (e.g., {@code "userName"} → {@code "UserName"}).
     *
     * @param str input string
     * @return capitalized string
     */
    public String capitalize(String str) {
        final int strLen = length(str);
        if (strLen == 0) {
            return str;
        }
        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        return checkPoints(firstCodepoint, newCodePoint, str, new int[strLen], strLen);
    }

    /**
     * Converts the first character to lower case (e.g., {@code "UserName"} → {@code "userName"}).
     *
     * @param str input string
     * @return uncapitalized string
     */
    public String uncapitalize(String str) {
        final int strLen = length(str);
        if (strLen == 0) {
            return str;
        }
        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toLowerCase(firstCodepoint);
        return checkPoints(firstCodepoint, newCodePoint, str, new int[strLen], strLen);
    }

    /**
     * Extracts and capitalizes the schema name from an AsyncAPI {@code $ref} path.
     * <p>
     * Examples:
     * <ul>
     *   <li>{@code "#/components/schemas/User"} → {@code "User"}</li>
     *   <li>{@code "./file.yaml#/components/schemas/user"} → {@code "User"}</li>
     * </ul>
     *
     * @param ref {@code $ref} string
     * @return capitalized schema name
     */
    public String refReplace(String ref) {
        return ref.replaceAll(".+/", "");
    }

    private String checkPoints(int firstCodepoint, int newCodePoint, String str, int[] strLen, int strLen1) {
        if (firstCodepoint == newCodePoint) {
            return str;
        }
        final int[] newCodePoints = strLen;
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint;
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen1; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint;
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    private int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * Makes a field name safe by appending "Field" if it's a Java keyword.
     *
     * @param fieldName the field name to check
     * @return safe field name
     */
    private String safeFieldName(String fieldName) {
        // Java keywords that cannot be used as field names
        if (fieldName == null || fieldName.isEmpty()) {
            return fieldName;
        }
        String lower = fieldName.toLowerCase();
        if (isJavaKeyword(lower)) {
            return fieldName + "Field";
        }
        return fieldName;
    }

    /**
     * Checks if a string is a Java keyword.
     *
     * @param word word to check
     * @return true if it's a keyword
     */
    private boolean isJavaKeyword(String word) {
        return switch (word) {
            case "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
                 "continue", "default", "do", "double", "else", "enum", "extends", "false", "final",
                 "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int",
                 "interface", "long", "native", "new", "null", "package", "private", "protected", "public",
                 "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
                 "throw", "throws", "transient", "true", "try", "void", "volatile", "while" -> true;
            default -> false;
        };
    }
}
