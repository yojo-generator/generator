package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test Gitter streaming spec without Lombok.
 */
public class GitterWithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
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
    void testGitterWithoutLombok() throws IOException {
        generateAndCompare();
    }
}
