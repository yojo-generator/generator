package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test AsyncAPI v3.0 official spec with Lombok.
 */
public class AsyncApiV3WithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
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
    void testAsyncApiV3WithLombok() throws IOException {
        generateAndCompare();
    }
}
