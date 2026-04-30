package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test one-more.yaml without Lombok.
 */
public class OneMoreWithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
    }

    @Override
    protected String getSpecName() {
        return "one-more.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "oneMore";
    }

    @Test
    void testOneMoreWithoutLombok() throws IOException {
        generateAndCompare();
    }
}
