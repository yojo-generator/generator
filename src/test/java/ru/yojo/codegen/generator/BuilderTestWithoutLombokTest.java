package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test generation WITHOUT Lombok for builder-test.yaml contract.
 * Verifies that a manual Builder class (static inner class with fluent setters,
 * singular adders, and build method) is correctly generated when Lombok is disabled
 * but builder is enabled.
 */
public class BuilderTestWithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
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
    void testBuilderContractWithoutLombok() throws IOException {
        generateAndCompare();
    }
}
