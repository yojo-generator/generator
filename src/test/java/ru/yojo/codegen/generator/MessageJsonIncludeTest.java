package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.ValidationApi;
import ru.yojo.codegen.domain.lombok.Accessors;
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
 * Tests for class-level {@code x-json-include} on message payloads.
 * <p>
 * The attribute must produce the same class-level {@code @JsonInclude} as it does
 * for {@code components/schemas}, with and without Lombok.
 */
class MessageJsonIncludeTest {

    private static final String SPEC = "message-json-include.yaml";

    @TempDir
    Path outputDir;

    @Test
    void messageHasJsonIncludeWithLombok() throws IOException {
        generate(true);
        assertJsonInclude();
    }

    @Test
    void messageHasJsonIncludeWithoutLombok() throws IOException {
        generate(false);
        assertJsonInclude();
    }

    @Test
    void generatedCodeCompilesWithLombok() throws IOException {
        generate(true);
        compileGeneratedCode();
    }

    @Test
    void generatedCodeCompilesWithoutLombok() throws IOException {
        generate(false);
        compileGeneratedCode();
    }

    private void assertJsonInclude() throws IOException {
        assertThat(read("messages/OrderCreated.java"))
                .contains("import com.fasterxml.jackson.annotation.JsonInclude;")
                .contains("@JsonInclude(JsonInclude.Include.NON_NULL)\npublic class OrderCreated");

        assertThat(read("messages/OrderUpdated.java"))
                .as("x-json-include must be combined with x-class-annotation")
                .contains("import com.fasterxml.jackson.annotation.JsonInclude;")
                .contains("@JsonIgnoreProperties(ignoreUnknown = true)")
                .contains("@JsonInclude(JsonInclude.Include.NON_EMPTY)\npublic class OrderUpdated");

        assertThat(read("messages/OrderDeleted.java"))
                .as("message without x-json-include must not get @JsonInclude")
                .doesNotContain("@JsonInclude")
                .doesNotContain("import com.fasterxml.jackson.annotation.JsonInclude;");
    }

    private void compileGeneratedCode() throws IOException {
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
        spec.setPackageLocation("example.messageJsonInclude");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(useLombok, true, true,
                new Accessors(false, false, false)));
        ctx.setValidationApi(ValidationApi.JAKARTA);
        new YojoGenerator().generateAll(ctx);
    }

    private String read(String relativePath) throws IOException {
        return Files.readString(outputDir.resolve(relativePath)).replace("\r\n", "\n");
    }
}
