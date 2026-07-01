package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Quick test to reproduce compilation error with array-of-boolean items
 * and enum containing 'ARRAY' value in the contract.
 */
class QuickProductOfferSyncTest {

    private static final String OUTPUT_DIR = "build/generated-product-offer-test/";

    @Test
    void testGenerateAndCompile() throws IOException {
        Path outputPath = Paths.get(OUTPUT_DIR);
        if (Files.exists(outputPath)) {
            try (Stream<Path> walk = Files.walk(outputPath)) {
                walk.sorted((a, b) -> -a.compareTo(b))
                        .forEach(p -> {
                            try { Files.deleteIfExists(p); } catch (IOException ignore) {}
                        });
            }
        }

        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("prod-offer-issue-contract.yaml");
        spec.setInputDirectory(".");
        spec.setOutputDirectory(OUTPUT_DIR);
        spec.setPackageLocation("com.example.generated");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));

        YojoGenerator gen = new YojoGenerator();
        try {
            gen.generateAll(ctx);
            System.out.println("=== GENERATION SUCCESSFUL ===");
        } catch (Exception e) {
            System.err.println("=== GENERATION FAILED ===");
            e.printStackTrace();
            fail("Generation failed: " + e.getMessage());
        }

        // Collect all generated files
        List<Path> javaFiles = new ArrayList<>();
        if (Files.exists(outputPath)) {
            try (Stream<Path> walk = Files.walk(outputPath)) {
                javaFiles.addAll(walk.filter(p -> p.toString().endsWith(".java")).toList());
            }
        }

        System.out.println("Generated " + javaFiles.size() + " Java files:");
        for (Path f : javaFiles) {
            System.out.println("  - " + f);
        }

        if (javaFiles.isEmpty()) {
            fail("No Java files were generated");
        }

        // Try to compile
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromPaths(javaFiles);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        boolean compiled = task.call();

        if (!compiled) {
            StringBuilder sb = new StringBuilder("COMPILATION FAILED:\n");
            for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics()) {
                sb.append(String.format("[%s] %s:%d:%d: %s%n",
                        d.getKind(),
                        d.getSource() != null ? d.getSource().getName() : "unknown",
                        d.getLineNumber(),
                        d.getColumnNumber(),
                        d.getMessage(null)));
            }
            fail(sb.toString());
        }

        assertTrue(compiled, "All generated code must compile without errors");

        // Cleanup
        try (Stream<Path> walk = Files.walk(outputPath)) {
            walk.sorted((a, b) -> -a.compareTo(b))
                    .forEach(p -> {
                        try { Files.deleteIfExists(p); } catch (IOException ignore) {}
                    });
        }
    }
}
