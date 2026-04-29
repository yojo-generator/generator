package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test discriminator.yaml with Lombok.
 */
public class DiscriminatorWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
    }

    @Override
    protected String getSpecName() {
        return "discriminator.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "discriminator";
    }

    @Test
    void testDiscriminatorWithLombok() throws IOException {
        generateAndCompare();
    }
}
