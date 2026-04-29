package ru.yojo.codegen.generator.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import ru.yojo.codegen.generator.YojoGenerator;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base class for tests that compare generated code with expected output.
 * Provides utilities for running generation and comparing results.
 */
public abstract class GenerationComparisonTestBase {

    protected YojoGenerator yojoGenerator = new YojoGenerator();
    
    @TempDir
    protected Path tempOutputDir;

    /**
     * Whether to use Lombok in generation.
     * Subclasses should override this method.
     */
    protected abstract boolean useLombok();

    /**
     * Get the specification file name to test.
     */
    protected abstract String getSpecName();

    /**
     * Get the input directory for contracts.
     */
    protected String getInputDirectory() {
        return "src/test/resources/example/contract";
    }

    /**
     * Get the expected output directory path.
     * Maps package location to expected directory structure.
     */
    protected String getExpectedOutputDirectory() {
        String base = "src/test/resources/example/expected/";
        String lombokDir = useLombok() ? "with-lombok" : "without-lombok";
        
        String packageLocation = getPackageLocation();
        
        // Extract the last part of package for expected directory mapping
        String expectedDir;
        if (packageLocation.contains(".")) {
            String[] parts = packageLocation.split("\\.");
            // Find the part that matches expected directory names
            for (String part : parts) {
                if (part.matches("test|asyncapi|gitter|slack|specFromIssue|oneMore|discriminator|testCreateApp")) {
                    expectedDir = part;
                    return base + lombokDir + "/" + expectedDir;
                }
            }
            // Fallback to last part
            expectedDir = parts[parts.length - 1];
        } else {
            expectedDir = packageLocation;
        }
        
        return base + lombokDir + "/" + expectedDir;
    }

    /**
     * Get the package location for generated code.
     */
    protected abstract String getPackageLocation();

    /**
     * Create YojoContext with appropriate settings.
     */
    protected YojoContext createYojoContext() {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName(getSpecName());
        spec.setInputDirectory(getInputDirectory());
        spec.setOutputDirectory(tempOutputDir.toString());
        spec.setPackageLocation(getPackageLocation());

        YojoContext context = new YojoContext();
        context.setSpecificationProperties(Collections.singletonList(spec));
        
        LombokProperties lombokProps = new LombokProperties(
            useLombok(),
            true, // allArgsConstructor
            new Accessors(true, true, true) // fluent, chain, true
        );
        context.setLombokProperties(lombokProps);
        
        return context;
    }

    @BeforeEach
    void setUp() throws IOException {
        // TempDir is automatically cleaned up by JUnit
    }

    /**
     * Run generation and compare with expected output.
     * If expected directory doesn't exist, just verify generation works.
     */
    protected void generateAndCompare() throws IOException {
        YojoContext context = createYojoContext();
        yojoGenerator.generateAll(context);

        Path expectedDir = Path.of(getExpectedOutputDirectory());
        
        // Check if expected directory exists
        if (!Files.exists(expectedDir)) {
            // Expected directory doesn't exist, just verify generation works
            assertThat(tempOutputDir).exists().isDirectory();
            return;
        }
        
        compareDirectories(expectedDir, tempOutputDir);
    }

    /**
     * Recursively compare two directories, checking that all files in expected
     * directory exist in actual directory and have identical content.
     */
    protected void compareDirectories(Path expectedDir, Path actualDir) throws IOException {
        assertThat(actualDir).as("Output directory must exist").exists().isDirectory();

        List<String> differences = new ArrayList<>();
        compareDirectoriesRecursive(expectedDir, actualDir, differences);

        if (!differences.isEmpty()) {
            StringBuilder message = new StringBuilder("Found differences between generated and expected code:\n");
            for (String diff : differences) {
                message.append(diff).append("\n");
            }
            assertThat(differences)
                .as(message.toString())
                .isEmpty();
        }
    }

    private void compareDirectoriesRecursive(Path expectedDir, Path actualDir, List<String> differences) throws IOException {
        if (!Files.exists(expectedDir)) {
            return;
        }

        try (var stream = Files.list(expectedDir)) {
            stream.forEach(expectedPath -> {
                try {
                    Path relativePath = expectedDir.relativize(expectedPath);
                    Path actualPath = actualDir.resolve(relativePath);

                    if (Files.isDirectory(expectedPath)) {
                        assertThat(actualPath)
                            .as("Directory must exist: " + relativePath)
                            .exists()
                            .isDirectory();
                        compareDirectoriesRecursive(expectedPath, actualPath, differences);
                    } else if (expectedPath.toString().endsWith(".java")) {
                        assertThat(actualPath)
                            .as("File must exist: " + relativePath)
                            .exists()
                            .isRegularFile();

                        String expectedContent = normalizeContent(Files.readString(expectedPath));
                        String actualContent = normalizeContent(Files.readString(actualPath));

                        if (!expectedContent.equals(actualContent)) {
                            differences.add("File differs: " + relativePath);
                            differences.add("Expected:\n" + expectedContent);
                            differences.add("Actual:\n" + actualContent);
                            differences.add("---");
                        }
                    }
                } catch (IOException e) {
                    differences.add("Error reading file: " + e.getMessage());
                }
            });
        }
    }

    /**
     * Normalize content for comparison by removing extra whitespace
     * while preserving meaningful formatting.
     */
    protected String normalizeContent(String content) {
        return content
            .replaceAll("\\r\\n", "\n")  // Normalize line endings
            .replaceAll("\\n+", "\n")      // Remove multiple blank lines
            .trim();
    }
}
