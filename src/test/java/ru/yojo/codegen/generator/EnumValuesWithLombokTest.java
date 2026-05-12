package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test enum-values.yaml contract with Lombok.
 * Verifies that enums with x-enumValues generate:
 * <ul>
 *   <li>{@code @JsonValue} on the getter</li>
 *   <li>{@code @JsonCreator} static {@code fromValue()} method</li>
 *   <li>{@code UNKNOWN_DEFAULT_YOJO} fallback constant when {@code x-enumDefault: true}</li>
 * </ul>
 */
public class EnumValuesWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
    }

    @Override
    protected String getSpecName() {
        return "enum-values.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "enumvalues";
    }

    @Test
    void testEnumValuesWithLombok() throws IOException {
        generateAndCompare();
    }
}
