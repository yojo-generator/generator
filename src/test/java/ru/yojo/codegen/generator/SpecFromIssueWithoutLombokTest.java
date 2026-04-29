package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test spec-from-issue without Lombok.
 */
public class SpecFromIssueWithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
    }

    @Override
    protected String getSpecName() {
        return "spec-from-issue.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "specFromIssue";
    }

    @Test
    void testSpecFromIssueWithoutLombok() throws IOException {
        generateAndCompare();
    }
}
