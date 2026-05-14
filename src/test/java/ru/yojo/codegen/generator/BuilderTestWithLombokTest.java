package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test generation with Lombok for builder-test.yaml contract.
 * Verifies that {@code @Builder}, {@code @Singular}, and {@code @Builder.Default} annotations
 * are correctly generated when Lombok is enabled.
 */
public class BuilderTestWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
    }

    @Override
    protected String getSpecName() {
        return "builder-test.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "example.testGenerate.builder";
    }

    @Test
    void testBuilderContractWithLombok() throws IOException {
        generateAndCompare();
    }
}
