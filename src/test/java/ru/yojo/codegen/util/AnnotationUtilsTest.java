package ru.yojo.codegen.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AnnotationUtils}.
 */
class AnnotationUtilsTest {

    /**
     * Issue #74: annotation parameters must not be imported together with the type name.
     */
    @Test
    void parametersAreSeparatedFromTypeName() {
        String annotation = "com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)";

        assertThat(AnnotationUtils.annotationName(annotation))
                .isEqualTo("com.fasterxml.jackson.annotation.JsonIgnoreProperties");
        assertThat(AnnotationUtils.annotationParameters(annotation))
                .isEqualTo("(ignoreUnknown = true)");
        assertThat(AnnotationUtils.toImport(annotation))
                .isEqualTo("com.fasterxml.jackson.annotation.JsonIgnoreProperties;");
        assertThat(AnnotationUtils.toUsage(annotation))
                .isEqualTo("@JsonIgnoreProperties(ignoreUnknown = true)");
    }

    /**
     * Nested annotations keep only the innermost simple name and the parameters.
     */
    @Test
    void nestedAnnotationWithParametersUsesSimpleName() {
        String annotation = "com.example.Outer.Inner(value = \"x\")";

        assertThat(AnnotationUtils.annotationName(annotation)).isEqualTo("com.example.Outer.Inner");
        assertThat(AnnotationUtils.toImport(annotation)).isEqualTo("com.example.Outer.Inner;");
        assertThat(AnnotationUtils.toUsage(annotation)).isEqualTo("@Inner(value = \"x\")");
    }

    /**
     * Leading {@code @} and trailing {@code ;} are accepted and normalized.
     */
    @Test
    void decoratorsAreStripped() {
        assertThat(AnnotationUtils.annotationName("@lombok.NonNull;")).isEqualTo("lombok.NonNull");
        assertThat(AnnotationUtils.annotationParameters("@lombok.NonNull;")).isEmpty();
        assertThat(AnnotationUtils.toUsage("@lombok.NonNull;")).isEqualTo("@NonNull");
        assertThat(AnnotationUtils.toImport("@lombok.NonNull;")).isEqualTo("lombok.NonNull;");
    }

    /**
     * Annotations without parameters are rendered and imported as before.
     */
    @Test
    void annotationWithoutParametersIsUnchanged() {
        assertThat(AnnotationUtils.annotationParameters("lombok.Builder")).isEmpty();
        assertThat(AnnotationUtils.toUsage("lombok.Builder")).isEqualTo("@Builder");
        assertThat(AnnotationUtils.toImport("lombok.Builder")).isEqualTo("lombok.Builder;");
    }
}
