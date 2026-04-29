package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test Gitter streaming spec with Lombok.
 */
public class GitterWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
    }

    @Override
    protected String getSpecName() {
        return "gitter-streaming-async-api-v3.0.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "gitter";
    }

    @Test
    void testGitterWithLombok() throws IOException {
        generateAndCompare();
    }
}
