package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

/**
 * Tests ALL contract specs with {@code splitModels=false} and Lombok enabled.
 * Verifies that all generated classes are placed in a single flat directory
 * (no {@code common/} and {@code messages/} subdirectories) when splitModels is disabled.
 */
public class AllContractsNoSplitTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
    }

    @Override
    protected String getSpecName() {
        return ""; // not used — each test specifies its own spec
    }

    @Override
    protected String getPackageLocation() {
        return ""; // not used — each test specifies its own package
    }

    // --- Test methods for each spec ---

    @Test
    void testMainContract() throws IOException {
        runTest("test.yaml", "example.testGenerate.test", "test");
    }

    @Test
    void testAsyncApiV3() throws IOException {
        runTest("async-api-official-v3.0.yaml", "asyncapi", "asyncapi");
    }

    @Test
    void testGitter() throws IOException {
        runTest("gitter-streaming-async-api-v3.0.yaml", "gitter", "gitter");
    }

    @Test
    void testSlack() throws IOException {
        runTest("slack-real-time-async-api-v3.0.yaml", "slack", "slack");
    }

    @Test
    void testSpecFromIssue() throws IOException {
        runTest("spec-from-issue.yaml", "specFromIssue", "specfromissue");
    }

    @Test
    void testOneMore() throws IOException {
        runTest("one-more.yaml", "oneMore", "onemore");
    }

    @Test
    void testDiscriminator() throws IOException {
        runTest("discriminator.yaml", "discriminator", "discriminator");
    }

    @Test
    void testTestCreateApp() throws IOException {
        runTest("test-create-app.yaml", "testCreateApp", "testcreateapp");
    }

    @Test
    void testBuilder() throws IOException {
        runTest("builder-test.yaml", "example.testGenerate.builder", "builder");
    }

    /**
     * Runs generation for a single spec with {@code splitModels=false}
     * and compares the output against the expected flat directory.
     */
    private void runTest(String specName, String packageLocation, String specKey) throws IOException {
        Path outputDir = tempOutputDir.resolve(specKey);
        Files.createDirectories(outputDir);

        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName(specName);
        spec.setInputDirectory(getInputDirectory());
        spec.setOutputDirectory(outputDir.toString());
        spec.setPackageLocation(packageLocation);
        spec.setSplitModels(false);

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(
            useLombok(),
            true,
            new Accessors(true, true, true)
        ));

        yojoGenerator.generateAll(ctx);

        String lombokDir = useLombok() ? "with-lombok" : "without-lombok";
        Path expectedDir = Path.of("src/test/resources/example/expected/" + lombokDir + "/nosplit-" + specKey);

        if (Files.exists(expectedDir)) {
            compareDirectories(expectedDir, outputDir);
        }
    }
}
