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
 * Tests for {@code default} of enum fields.
 * <p>
 * A bare enum constant ({@code default: NOT_EXECUTED}) must be qualified with the enum class name,
 * both for inline enums and for enums referenced via {@code $ref}, in schemas and in messages.
 * Values that are already qualified (simple or fully qualified class name) must be kept as is.
 */
class EnumDefaultTest {

    private static final String SPEC = "enum-default.yaml";

    @TempDir
    Path outputDir;

    @ParameterizedTest(name = "lombok={0}")
    @ValueSource(booleans = {true, false})
    void schemaEnumDefaultIsQualified(boolean useLombok) throws IOException {
        generate(useLombok);

        String user = read("common/User.java");
        assertThat(user)
                .contains("Current account status")
                .contains("private UserStatus accountStatus = UserStatus.ACTIVE;")
                .contains("private ExecutionStatus executionStatus = ExecutionStatus.NOT_EXECUTED;")
                .contains("private ExecutionStatus shortStatus = ExecutionStatus.EXECUTED;")
                .contains("private ExecutionStatus fqnStatus = example.enumDefault.common.ExecutionStatus.EXECUTED;")
                .contains("private ExecutionStatus noDefaultStatus;")
                .contains("private UserInlineStatus inlineStatus = UserInlineStatus.SECOND;")
                .doesNotContain("ExecutionStatus.ExecutionStatus");
        assertThat(outputDir.resolve("common/UserAccountStatus.java")).doesNotExist();
    }

    @ParameterizedTest(name = "lombok={0}")
    @ValueSource(booleans = {true, false})
    void messageEnumDefaultIsQualified(boolean useLombok) throws IOException {
        generate(useLombok);

        String message = read("messages/UserMessage.java");
        assertThat(message)
                .contains("private UserStatus userStatus = UserStatus.BLOCKED;")
                .contains("private ExecutionStatus executionStatus = ExecutionStatus.NOT_EXECUTED;")
                .contains("private ExecutionStatus shortStatus = ExecutionStatus.EXECUTED;")
                .contains("private ExecutionStatus fqnStatus = example.enumDefault.common.ExecutionStatus.EXECUTED;")
                .contains("private UserMessageInlineStatus inlineStatus = UserMessageInlineStatus.FIRST;")
                .doesNotContain("ExecutionStatus.ExecutionStatus")
                .doesNotContain("@Valid");
    }

    @ParameterizedTest(name = "lombok={0}")
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
        spec.setPackageLocation("example.enumDefault");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(useLombok, true, true,
                new Accessors(false, false, false)));
        ctx.setValidationApi(ValidationApi.JAKARTA);
        new YojoGenerator().generateAll(ctx);
    }

    private String read(String relativePath) throws IOException {
        return Files.readString(outputDir.resolve(relativePath));
    }
}
