package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test Slack real-time spec with Lombok.
 */
public class SlackWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
    }

    @Override
    protected String getSpecName() {
        return "slack-real-time-async-api-v3.0.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "slack";
    }

    @Test
    void testSlackWithLombok() throws IOException {
        generateAndCompare();
    }
}
