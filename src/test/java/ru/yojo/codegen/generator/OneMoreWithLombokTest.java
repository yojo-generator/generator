package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test one-more.yaml with Lombok.
 */
public class OneMoreWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
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
    void testOneMoreWithLombok() throws IOException {
        generateAndCompare();
    }
}
