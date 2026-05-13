package ru.yojo.codegen.generator;

import ru.yojo.codegen.generator.YojoGenerator;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.util.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;

/**
 * Utility to update expected files for all tests.
 * Run this to regenerate expected files based on current code generation.
 * 
 * Usage: Run with: java -cp build/classes/java/main:build/classes/java/test:$(grep -v '^#' build.gradle | grep -oP '(?<=implementation ').*(?=')' | tr '\n' ':') ru.yojo.codegen.generator.UpdateExpectedFiles
 * Or simply run from IDE.
 */
public class UpdateExpectedFiles {
    
    private static final Logger LOG = new Logger(UpdateExpectedFiles.class);
    
    public static void main(String[] args) throws IOException {
        LOG.info("Updating expected files for all tests...");
        
        YojoGenerator generator = new YojoGenerator();
        
        TestConfig[] configs = {
            // Discriminator tests
            new TestConfig("discriminator.yaml", "discriminator", true, "with-lombok/discriminator"),
            new TestConfig("discriminator.yaml", "discriminator", false, "without-lombok/discriminator"),
            
            // Test (main contract) tests
            new TestConfig("test.yaml", "example.testGenerate.test", true, "with-lombok/test"),
            new TestConfig("test.yaml", "example.testGenerate.test", false, "without-lombok/test"),
            
            // Gitter tests
            new TestConfig("gitter-streaming-async-api-v3.0.yaml", "gitter", true, "with-lombok/gitter"),
            new TestConfig("gitter-streaming-async-api-v3.0.yaml", "gitter", false, "without-lombok/gitter"),
            
            // Slack tests
            new TestConfig("slack-real-time-async-api-v3.0.yaml", "slack", true, "with-lombok/slack"),
            new TestConfig("slack-real-time-async-api-v3.0.yaml", "slack", false, "without-lombok/slack"),
            
            // OneMore tests
            new TestConfig("one-more.yaml", "oneMore", true, "with-lombok/oneMore"),
            new TestConfig("one-more.yaml", "oneMore", false, "without-lombok/oneMore"),
            
            // TestCreateApp tests
            new TestConfig("test-create-app.yaml", "testCreateApp", true, "with-lombok/testCreateApp"),
            new TestConfig("test-create-app.yaml", "testCreateApp", false, "without-lombok/testCreateApp"),
            
            // AsyncAPI v3 tests
            new TestConfig("async-api-official-v3.0.yaml", "asyncapi", true, "with-lombok/asyncapi"),
            new TestConfig("async-api-official-v3.0.yaml", "asyncapi", false, "without-lombok/asyncapi"),
            
            // SpecFromIssue tests
            new TestConfig("spec-from-issue.yaml", "specFromIssue", true, "with-lombok/specFromIssue"),
            new TestConfig("spec-from-issue.yaml", "specFromIssue", false, "without-lombok/specFromIssue"),
        };
        
        for (TestConfig config : configs) {
            LOG.info("Processing: " + config.specName + " (lombok: " + config.lombok + ")");
            
            // Create temp directory
            Path tempDir = Files.createTempDirectory("yojo-update-expected");
            
            try {
                // Setup context
                SpecificationProperties spec = new SpecificationProperties();
                spec.setSpecName(config.specName);
                spec.setInputDirectory("src/test/resources/example/contract");
                spec.setOutputDirectory(tempDir.toString());
                spec.setPackageLocation(config.packageLocation);
                
                YojoContext context = new YojoContext();
                context.setSpecificationProperties(Collections.singletonList(spec));
                
                LombokProperties lombokProps = new LombokProperties(
                    config.lombok,
                    true, // allArgsConstructor
                    new Accessors(true, true, true)
                );
                context.setLombokProperties(lombokProps);
                
                // Generate
                generator.generateAll(context);
                
                // Copy to expected directory
                Path expectedDir = Paths.get("src/test/resources/example/expected", config.expectedPath);
                
                if (Files.exists(expectedDir)) {
                    deleteRecursive(expectedDir);
                }
                Files.createDirectories(expectedDir.getParent());
                
                copyDirectory(tempDir, expectedDir);
                
                LOG.info("  Updated: " + expectedDir);
                
            } catch (Exception e) {
                LOG.error("  Failed to process " + config.specName + ": " + e.getMessage());
                e.printStackTrace();
            } finally {
                deleteRecursive(tempDir);
            }
        }
        
        LOG.info("\nExpected files update completed!");
        LOG.info("Now run: ./gradlew test");
    }
    
    private static void copyDirectory(Path source, Path target) throws IOException {
        Files.walk(source).forEach(src -> {
            try {
                Path dest = target.resolve(source.relativize(src));
                if (Files.isDirectory(src)) {
                    Files.createDirectories(dest);
                } else {
                    Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    private static void deleteRecursive(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(p -> {
                    try { Files.delete(p); } catch (IOException e) { /* ignore */ }
                });
        }
    }
    
    private static class TestConfig {
        final String specName;
        final String packageLocation;
        final boolean lombok;
        final String expectedPath;
        
        TestConfig(String specName, String packageLocation, boolean lombok, String expectedPath) {
            this.specName = specName;
            this.packageLocation = packageLocation;
            this.lombok = lombok;
            this.expectedPath = expectedPath;
        }
    }
}
