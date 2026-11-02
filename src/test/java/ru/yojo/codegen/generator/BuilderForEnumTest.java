package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.ValidationApi;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.BuilderProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Regression tests for {@code @Builder} generation on enums (PR #80).
 * <p>
 * Lombok rejects {@code @Builder} on enums:
 * {@code @Builder is only supported on classes, records, constructors, and methods}.
 * Enabling the builder pattern globally ({@code lombok.builder.enabled = true}) must therefore
 * not add {@code @Builder} to generated enums, while DTOs must keep it.
 */
class BuilderForEnumTest {

    private static final String SPEC = "builder-enum.yaml";

    @TempDir
    Path outputDir;

    @Test
    void enumHasNoBuilderWhenBuilderEnabledWithLombok() throws IOException {
        generate(true);

        String statusEnum = read("common/StatusEnum.java");
        assertThat(statusEnum)
                .as("plain enum must not be annotated with @Builder")
                .contains("public enum StatusEnum")
                .doesNotContain("@Builder");

        String orderStatus = read("common/OrderStatus.java");
        assertThat(orderStatus)
                .as("enum with x-enumValues must not be annotated with @Builder")
                .contains("public enum OrderStatus")
                .doesNotContain("@Builder");

        String message = read("messages/StatusMessage.java");
        assertThat(message)
                .as("DTO must keep the builder pattern")
                .contains("@Builder")
                .contains("public class StatusMessage");
    }

    @Test
    void enumHasNoBuilderWhenBuilderEnabledWithoutLombok() throws IOException {
        generate(false);

        String statusEnum = read("common/StatusEnum.java");
        assertThat(statusEnum)
                .contains("public enum StatusEnum")
                .doesNotContain("@Builder")
                .doesNotContain("public static class Builder");
    }

    @Test
    void generatedCodeCompiles() throws IOException {
        generate(true);

        List<Path> javaFiles;
        try (Stream<Path> walk = Files.walk(outputDir)) {
            javaFiles = walk.filter(p -> p.toString().endsWith(".java")).toList();
        }
        assertThat(javaFiles).isNotEmpty();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromPaths(javaFiles);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        if (!task.call()) {
            StringBuilder sb = new StringBuilder("COMPILATION FAILED:\n");
            diagnostics.getDiagnostics().forEach(d -> sb.append(String.format("[%s] %s:%d: %s%n",
                    d.getKind(),
                    d.getSource() != null ? d.getSource().getName() : "unknown",
                    d.getLineNumber(),
                    d.getMessage(null))));
            fail(sb.toString());
        }
    }

    private void generate(boolean useLombok) throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName(SPEC);
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory(outputDir.toString());
        spec.setPackageLocation("example.builderEnum");

        LombokProperties lombokProperties = new LombokProperties(useLombok, true, true,
                new Accessors(false, false, false));
        lombokProperties.setBuilder(new BuilderProperties(true, true, true));

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(lombokProperties);
        ctx.setValidationApi(ValidationApi.JAKARTA);
        new YojoGenerator().generateAll(ctx);
    }

    private String read(String relativePath) throws IOException {
        return Files.readString(outputDir.resolve(relativePath));
    }
}
