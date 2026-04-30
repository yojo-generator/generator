package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test test-create-app.yaml without Lombok.
 */
public class TestCreateAppWithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
    }

    @Override
    protected String getSpecName() {
        return "test-create-app.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "testCreateApp";
    }

    @Test
    void testCreateAppWithoutLombok() throws IOException {
        generateAndCompare();
    }
}
