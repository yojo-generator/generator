package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test test-create-app.yaml with Lombok.
 */
public class TestCreateAppWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
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
    void testCreateAppWithLombok() throws IOException {
        generateAndCompare();
    }
}
