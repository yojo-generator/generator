package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test generation without Lombok for the main test.yaml contract.
 */
public class TestContractWithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
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
    void testMainContractWithoutLombok() throws IOException {
        generateAndCompare();
    }
}
