package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test generation with Lombok for the main test.yaml contract.
 */
public class TestContractWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
    }

    @Override
    protected String getSpecName() {
        return "test.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "example.testGenerate.test";
    }

    @Test
    void testMainContractWithLombok() throws IOException {
        generateAndCompare();
    }
}
