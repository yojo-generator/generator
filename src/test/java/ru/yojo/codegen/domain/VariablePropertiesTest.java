package ru.yojo.codegen.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for field source generation in {@link VariableProperties}.
 */
class VariablePropertiesTest {

    /**
     * Issue #73: two annotations with the same simple name must not produce conflicting imports.
     */
    @Test
    void fieldAnnotationsWithClashingSimpleNamesUseEnclosingType() {
        VariableProperties properties = VariableProperties.builder()
                .name("content")
                .type("String")
                .addFieldAnnotation("lombok.NonNull")
                .addFieldAnnotation("lombok.EqualsAndHashCode.Exclude")
                .addFieldAnnotation("lombok.ToString.Exclude")
                .build();

        assertThat(properties.toWrite())
                .contains("@EqualsAndHashCode.Exclude")
                .contains("@ToString.Exclude")
                .contains("@NonNull")
                .doesNotContain("@Exclude");

        assertThat(properties.getRequiredImports())
                .contains("lombok.EqualsAndHashCode;")
                .contains("lombok.ToString;")
                .contains("lombok.NonNull;");
    }

    /**
     * When the clashing annotations have no enclosing type, fully qualified names are used
     * and no imports are added.
     */
    @Test
    void fieldAnnotationsWithoutEnclosingTypeFallBackToFullyQualifiedName() {
        VariableProperties properties = VariableProperties.builder()
                .name("value")
                .type("String")
                .addFieldAnnotation("lombok.NonNull")
                .addFieldAnnotation("org.jspecify.annotations.NonNull")
                .build();

        assertThat(properties.toWrite())
                .contains("@lombok.NonNull")
                .contains("@org.jspecify.annotations.NonNull");

        assertThat(properties.getRequiredImports())
                .doesNotContain("lombok.NonNull;")
                .doesNotContain("org.jspecify.annotations.NonNull;");
    }

    /**
     * A single annotation keeps its simple name and import; decorative {@code @} and {@code ;}
     * are ignored.
     */
    @Test
    void singleFieldAnnotationIsImportedAndSimplified() {
        VariableProperties properties = VariableProperties.builder()
                .name("value")
                .type("String")
                .addFieldAnnotation("@lombok.NonNull;")
                .build();

        assertThat(properties.toWrite()).contains("@NonNull");
        assertThat(properties.getRequiredImports()).contains("lombok.NonNull;");
    }
}
