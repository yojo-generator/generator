package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.generator.YojoGenerator;

import java.io.File;

/**
 * Utility test to generate expected files for comparison tests.
 * Run this test to update expected files in src/test/resources/example/expected/
 */
public class GenerateExpectedFilesTest {

    private YojoGenerator yojoGenerator = new YojoGenerator();

    @Test
    void generateWithoutLombok() throws java.io.IOException {
        generateForSpec("test.yaml", "example.testGenerate.test", false);
        generateForSpec("async-api-official-v3.0.yaml", "asyncapi", false);
        generateForSpec("gitter-streaming-async-api-v3.0.yaml", "gitter", false);
        generateForSpec("slack-real-time-async-api-v3.0.yaml", "slack", false);
        generateForSpec("spec-from-issue.yaml", "specFromIssue", false);
        generateForSpec("one-more.yaml", "oneMore", false);
        generateForSpec("discriminator.yaml", "discriminator", false);
        generateForSpec("test-create-app.yaml", "testCreateApp", false);
    }

    @Test
    void generateWithLombok() throws java.io.IOException {
        generateForSpec("test.yaml", "example.testGenerate.test", true);
        generateForSpec("async-api-official-v3.0.yaml", "asyncapi", true);
        generateForSpec("gitter-streaming-async-api-v3.0.yaml", "gitter", true);
        generateForSpec("slack-real-time-async-api-v3.0.yaml", "slack", true);
        generateForSpec("spec-from-issue.yaml", "specFromIssue", true);
        generateForSpec("one-more.yaml", "oneMore", true);
        generateForSpec("discriminator.yaml", "discriminator", true);
        generateForSpec("test-create-app.yaml", "testCreateApp", true);
    }

    private void generateForSpec(String specName, String packageLocation, boolean useLombok) throws java.io.IOException {
        String lombokDir = useLombok ? "with-lombok" : "without-lombok";
        
        // Extract expected directory name from package
        String expectedDir = packageLocation.contains(".") 
            ? packageLocation.substring(packageLocation.lastIndexOf(".") + 1)
            : packageLocation;
        
        String outputDir = "src/test/resources/example/expected/" + lombokDir + "/" + expectedDir;
        
        System.out.println("Generating " + specName + " (Lombok: " + useLombok + ") -> " + outputDir);
        
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName(specName);
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory(outputDir);
        spec.setPackageLocation(packageLocation);

        YojoContext context = new YojoContext();
        context.setSpecificationProperties(java.util.Collections.singletonList(spec));
        context.setLombokProperties(new LombokProperties(
            useLombok,
            true,
            new Accessors(true, true, true)
        ));

        yojoGenerator.generateAll(context);
        
        System.out.println("Generated files in: " + outputDir);
        listGeneratedFiles(new File(outputDir));
    }
    
    private void listGeneratedFiles(File dir) {
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                listGeneratedFiles(f);
            } else if (f.getName().endsWith(".java")) {
                System.out.println("  - " + f.getAbsolutePath());
            }
        }
    }
}
