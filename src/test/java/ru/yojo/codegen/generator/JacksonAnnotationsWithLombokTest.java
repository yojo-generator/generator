package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test jackson-annotations.yaml contract with Lombok.
 * Verifies Jackson field annotations:
 * <ul>
 *   <li>{@code @JsonProperty} for {@code x-json-property} and {@code x-json-naming: SNAKE_CASE}</li>
 *   <li>{@code @JsonFormat} for {@code x-json-format}</li>
 *   <li>{@code @JsonIgnore} for {@code x-json-ignore}</li>
 *   <li>{@code @JsonInclude} at class level for {@code x-json-include}</li>
 * </ul>
 */
public class JacksonAnnotationsWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
    }

    @Override
    protected String getSpecName() {
        return "jackson-annotations.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "jackson";
    }

    @Override
    protected String getInputDirectory() {
        return "src/test/resources/example/contract";
    }

    @Override
    protected String getExpectedOutputDirectory() {
        return "src/test/resources/example/expected/with-lombok/jackson";
    }

    @Test
    void testJacksonAnnotationsWithLombok() throws IOException {
        generateAndCompare();
    }
}
