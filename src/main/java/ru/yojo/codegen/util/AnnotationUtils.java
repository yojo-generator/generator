package ru.yojo.codegen.util;

/**
 * Utility class for working with annotation strings declared in a contract
 * ({@code x-class-annotation}, {@code x-field-annotation}).
 * <p>
 * An annotation entry may contain parameters, e.g.
 * {@code com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)}.
 * The type name and its parameters must be separated before the import is generated:
 * {@code import com.fasterxml.jackson.annotation.JsonIgnoreProperties;} and
 * {@code @JsonIgnoreProperties(ignoreUnknown = true)}.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public final class AnnotationUtils {

    private AnnotationUtils() {
    }

    /**
     * Returns the fully qualified type name of an annotation without the leading {@code @},
     * without parameters and without a trailing semicolon.
     *
     * @param annotation raw annotation entry (e.g. {@code "@com.example.Foo(value = 1);"})
     * @return fully qualified type name (e.g. {@code "com.example.Foo"})
     */
    public static String annotationName(String annotation) {
        String name = annotation == null ? "" : annotation.trim();
        while (name.startsWith("@")) {
            name = name.substring(1).trim();
        }
        int parametersStart = name.indexOf('(');
        if (parametersStart >= 0) {
            name = name.substring(0, parametersStart);
        }
        if (name.endsWith(";")) {
            name = name.substring(0, name.length() - 1);
        }
        return name.trim();
    }

    /**
     * Returns the parameters of an annotation, including the parentheses,
     * or an empty string when the annotation has none.
     *
     * @param annotation raw annotation entry (e.g. {@code "com.example.Foo(value = 1)"})
     * @return parameters (e.g. {@code "(value = 1)"}) or {@code ""}
     */
    public static String annotationParameters(String annotation) {
        String value = annotation == null ? "" : annotation.trim();
        while (value.startsWith("@")) {
            value = value.substring(1).trim();
        }
        int start = value.indexOf('(');
        if (start < 0) {
            return "";
        }
        int end = value.lastIndexOf(')');
        return end > start ? value.substring(start, end + 1) : value.substring(start);
    }

    /**
     * Returns the last segment of a fully qualified type name.
     *
     * @param typeName fully qualified or simple type name
     * @return simple name
     */
    public static String simpleName(String typeName) {
        int lastDot = typeName.lastIndexOf('.');
        return lastDot >= 0 ? typeName.substring(lastDot + 1) : typeName;
    }

    /**
     * Builds an import statement for an annotation entry.
     *
     * @param annotation raw annotation entry
     * @return import statement without the {@code import} keyword (e.g. {@code "com.example.Foo;"})
     */
    public static String toImport(String annotation) {
        return annotationName(annotation) + ";";
    }

    /**
     * Builds the annotation usage (simple name plus parameters).
     *
     * @param annotation raw annotation entry
     * @return annotation usage (e.g. {@code "@Foo(value = 1)"})
     */
    public static String toUsage(String annotation) {
        return "@" + simpleName(annotationName(annotation)) + annotationParameters(annotation);
    }
}
