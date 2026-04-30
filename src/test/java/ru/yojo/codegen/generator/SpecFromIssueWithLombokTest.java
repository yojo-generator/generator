package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;

/**
 * Test spec-from-issue with Lombok.
 */
public class SpecFromIssueWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
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
    void testSpecFromIssueWithLombok() throws IOException {
        generateAndCompare();
    }
}
