package ru.yojo.codegen.generator;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
 * Regression tests for {@code x-remove-schema} on a message payload referencing a schema via {@code $ref}.
 * <p>
 * The referenced schema must not be generated, and its fields must be present in the message class
 * (previously the message class was written empty).
 */
class RemoveSchemaTest {

    private static final String SPEC = "remove-schema.yaml";

    @TempDir
    Path outputDir;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void messageContainsSchemaFieldsAndSchemaIsNotGenerated(boolean useLombok) throws IOException {
        generate(useLombok);

        assertThat(outputDir.resolve("common/UserDto.java"))
                .as("schema marked with x-remove-schema must not be generated")
                .doesNotExist();

        String message = Files.readString(outputDir.resolve("messages/UserCreated.java"));
        assertThat(message)
                .contains("public class UserCreated")
                .contains("private String id;")
                .contains("private Integer age;");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void generatedCodeCompiles(boolean useLombok) throws IOException {
        generate(useLombok);

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
        spec.setPackageLocation("example.removeSchema");

        LombokProperties lombokProperties = new LombokProperties(useLombok, true, true,
                new Accessors(false, false, false));

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(lombokProperties);
        ctx.setValidationApi(ValidationApi.JAKARTA);
        new YojoGenerator().generateAll(ctx);
    }
}
