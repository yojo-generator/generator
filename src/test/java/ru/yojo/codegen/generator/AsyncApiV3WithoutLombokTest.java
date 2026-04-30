package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test AsyncAPI v3.0 official spec without Lombok.
 */
public class AsyncApiV3WithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
    }

    @Override
    protected String getSpecName() {
        return "async-api-official-v3.0.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "asyncapi";
    }

    @Test
    void testAsyncApiV3WithoutLombok() throws IOException {
        generateAndCompare();
    }
}
