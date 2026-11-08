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

    /**
     * {@code x-masked} with a mask method: the field knows the mask call and the name of the method
     * that gives the masked value to Lombok {@code toString()}.
     */
    @Test
    void maskMethodBuildsMaskCallAndMethodName() {
        VariableProperties properties = VariableProperties.builder()
                .name("password")
                .type("String")
                .maskMethod("com.example.Masker.mask")
                .build();

        assertThat(properties.isMasked()).isTrue();
        assertThat(properties.maskedValue()).isEqualTo("com.example.Masker.mask(this.password)");
        assertThat(properties.toStringMaskMethodName()).isEqualTo("toStringMaskedPassword");
        assertThat(properties.isExcludedFromLombokToString()).isFalse();
    }

    /**
     * {@code x-masked: true}: the default mask keeps {@code null} as is.
     */
    @Test
    void defaultMaskBuildsNullSafeExpression() {
        VariableProperties properties = VariableProperties.builder()
                .name("password")
                .type("String")
                .masked(true)
                .build();

        assertThat(properties.isMasked()).isTrue();
        assertThat(properties.getMaskMethod()).isNull();
        assertThat(properties.maskedValue()).isEqualTo("this.password == null ? null : \"***\"");
    }

    /**
     * {@code masked(...)} sets the whole mask mode: it resets the mask method set before.
     */
    @Test
    void maskedResetsMaskMethod() {
        VariableProperties defaultMask = VariableProperties.builder()
                .name("password")
                .type("String")
                .maskMethod("com.example.Masker.mask")
                .masked(true)
                .build();
        VariableProperties noMask = VariableProperties.builder()
                .name("password")
                .type("String")
                .maskMethod("com.example.Masker.mask")
                .masked(false)
                .build();

        assertThat(defaultMask.isMasked()).isTrue();
        assertThat(defaultMask.getMaskMethod()).isNull();
        assertThat(noMask.isMasked()).isFalse();
        assertThat(noMask.getMaskMethod()).isNull();
    }

    /**
     * A field without {@code x-masked} and an enum constant are not masked.
     */
    @Test
    void maskIsNotAppliedWithoutValueAndToEnumConstants() {
        VariableProperties plainField = VariableProperties.builder()
                .name("login")
                .type("String")
                .build();
        VariableProperties enumConstant = VariableProperties.builder()
                .name("status")
                .type("Status")
                .masked(true)
                .build();
        enumConstant.setEnum(true);

        assertThat(plainField.isMasked()).isFalse();
        assertThat(enumConstant.isMasked()).isFalse();
    }

    /**
     * {@code lombok.ToString.Exclude} from {@code x-field-annotation} is found with any decoration.
     */
    @Test
    void toStringExcludeFromFieldAnnotationIsFound() {
        VariableProperties properties = VariableProperties.builder()
                .name("secret")
                .type("String")
                .addFieldAnnotation("@lombok.ToString.Exclude;")
                .masked(true)
                .build();

        assertThat(properties.isExcludedFromLombokToString()).isTrue();
        assertThat(properties.isMasked()).isTrue();
    }
}
