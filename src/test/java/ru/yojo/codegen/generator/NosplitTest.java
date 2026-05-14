package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test generation with Lombok for builder-test.yaml contract with splitModels=false.
 * Verifies that all generated classes are placed in a single flat directory
 * (no common/ and messages/ subdirectories) when splitModels is disabled.
 */
public class NosplitTest extends GenerationComparisonTestBase {

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
        return "nosplit";
    }

    @Override
    protected YojoContext createYojoContext() {
        YojoContext ctx = super.createYojoContext();
        ctx.getSpecificationProperties().get(0).setSplitModels(false);
        return ctx;
    }

    @Test
    void testNosplitGenerationWithLombok() throws IOException {
        generateAndCompare();
    }
}
