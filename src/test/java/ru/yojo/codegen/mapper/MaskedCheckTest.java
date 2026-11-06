package ru.yojo.codegen.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.exception.SchemaFillException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for reading {@code x-masked} values in {@link AbstractMapper}.
 */
class MaskedCheckTest {

    /**
     * {@code true} enables the default mask.
     */
    @ParameterizedTest
    @ValueSource(strings = {"true", "TRUE", " true "})
    void trueEnablesDefaultMask(String value) {
        VariableProperties properties = new VariableProperties();

        AbstractMapper.fillMasked(properties, "Schema", "field", value);

        assertThat(properties.isMasked()).isTrue();
        assertThat(properties.getMaskMethod()).isNull();
    }

    /**
     * {@code false} keeps the value as is.
     */
    @ParameterizedTest
    @ValueSource(strings = {"false", "FALSE", " false "})
    void falseDisablesMask(String value) {
        VariableProperties properties = new VariableProperties();

        AbstractMapper.fillMasked(properties, "Schema", "field", value);

        assertThat(properties.isMasked()).isFalse();
        assertThat(properties.getMaskMethod()).isNull();
    }

    /**
     * A fully qualified static method enables a custom mask; spaces around it are removed.
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "com.example.Masker.mask",
            "  com.example.Masker.mask  ",
            "Masker.mask",
            "com.example.Outer$Inner.mask_value"
    })
    void fullyQualifiedMethodEnablesCustomMask(String value) {
        VariableProperties properties = new VariableProperties();

        AbstractMapper.fillMasked(properties, "Schema", "field", value);

        assertThat(properties.isMasked()).isTrue();
        assertThat(properties.getMaskMethod()).isEqualTo(value.trim());
    }

    /**
     * Any other value stops the generation.
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "yes",
            "mask",
            "com.example.Masker.mask()",
            "com.example.Masker::mask",
            "com.example.Masker.",
            ".mask",
            "com.example.Masker.mask(this.field)",
            "[com.example.Masker.mask]"
    })
    void wrongValueIsRejected(String value) {
        assertThatThrownBy(() -> AbstractMapper.fillMasked(new VariableProperties(), "Schema", "field", value))
                .isInstanceOf(SchemaFillException.class)
                .hasMessageContaining("x-masked")
                .hasMessageContaining("field")
                .hasMessageContaining("Schema");
    }

    /**
     * {@code x-masked} on the field level of a collection is fine.
     */
    @Test
    void maskOnCollectionFieldIsAccepted() {
        Map<String, Object> field = Map.of(
                "type", "array",
                "items", Map.of("type", "string"),
                "x-masked", true);

        assertThatCode(() -> AbstractMapper.checkNoMaskInItems("Schema", "phones", field))
                .doesNotThrowAnyException();
    }

    /**
     * {@code x-masked} inside {@code items} (also of a nested collection) stops the generation.
     */
    @Test
    void maskInsideItemsIsRejected() {
        Map<String, Object> items = Map.of(
                "type", "array",
                "items", Map.of("type", "string"),
                "x-masked", true);
        Map<String, Object> nestedItems = Map.of(
                "type", "array",
                "items", Map.of("type", "array", "items", Map.of("type", "string", "x-masked", true)));

        for (Map<String, Object> field : List.of(Map.<String, Object>of("type", "array", "items", items), nestedItems)) {
            assertThatThrownBy(() -> AbstractMapper.checkNoMaskInItems("Schema", "phones", field))
                    .isInstanceOf(SchemaFillException.class)
                    .hasMessageContaining("x-masked")
                    .hasMessageContaining("items")
                    .hasMessageContaining("phones")
                    .hasMessageContaining("Schema");
        }
    }
}
