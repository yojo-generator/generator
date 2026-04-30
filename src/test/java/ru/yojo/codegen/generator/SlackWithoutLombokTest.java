package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test Slack real-time spec without Lombok.
 */
public class SlackWithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
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
    void testSlackWithoutLombok() throws IOException {
        generateAndCompare();
    }
}
