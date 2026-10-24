package ru.yojo.codegen.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for an enum schema declared with a primitive {@code type} (Issue #76).
 * <p>
 * A schema like {@code type: string, enum: [...]} must still produce a Java enum class,
 * because messages reference it by {@code $ref} and cannot compile without it.
 */
class EnumWithStringTypeTest {

    private static final String BASE_DIR = "src/test/resources/example/testGenerate/enumstringtype/";

    private final YojoGenerator yojoGenerator = new YojoGenerator();

    @BeforeEach
    void setUp() throws IOException {
        deleteRecursively(Paths.get(BASE_DIR));
    }

    private void generate() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("enum-string-type.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory(BASE_DIR);
        spec.setPackageLocation("example.testGenerate.enumstringtype");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));
        ctx.setValidationApi(ValidationApi.JAKARTA);
        yojoGenerator.generateAll(ctx);
    }

    @Test
    void enumWithStringTypeIsGenerated() throws IOException {
        generate();

        String enumClass = Files.readString(Paths.get(BASE_DIR + "common/MyLovelyEnum.java"));
        assertThat(enumClass)
                .contains("public enum MyLovelyEnum")
                .contains("FIRST_CASE")
                .contains("SECOND_CASE");

        String message = Files.readString(Paths.get(BASE_DIR + "messages/MyLovelyMessage.java"));
        assertThat(message)
                .contains("private MyLovelyEnum enumField;")
                .contains("import example.testGenerate.enumstringtype.common.MyLovelyEnum;");
    }

    @Test
    void generatedCodeCompiles() throws IOException {
        generate();

        List<Path> javaFiles;
        try (Stream<Path> walk = Files.walk(Paths.get(BASE_DIR))) {
            javaFiles = walk
                    .filter(p -> p.toString().endsWith(".java"))
                    .toList();
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

    private static void deleteRecursively(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted((a, b) -> -a.compareTo(b))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignore) {
                        }
                    });
        }
    }
}
