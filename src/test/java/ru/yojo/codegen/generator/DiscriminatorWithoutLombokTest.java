package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test discriminator.yaml without Lombok.
 */
public class DiscriminatorWithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
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
    void testDiscriminatorWithoutLombok() throws IOException {
        generateAndCompare();
    }
}
