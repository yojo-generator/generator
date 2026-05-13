package ru.yojo.codegen.generator;

import org.junit.jupiter.api.*;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.ValidationApi;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for backward-compatible x- prefixed attribute support.
 * <p>
 * Verifies that new x- prefixed attribute names (e.g. {@code x-realization}, {@code x-digits})
 * produce the same generated code as the deprecated old names.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class XAttributesTest {

    private final YojoGenerator yojoGenerator = new YojoGenerator();
    private static final String BASE_DIR = "src/test/resources/example/testGenerate/xattr/";

    @BeforeEach
    void setUp() throws IOException {
        // Clean output directory before each test
        Path path = Paths.get(BASE_DIR);
        if (Files.exists(path)) {
            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted((a, b) -> -a.compareTo(b))
                        .forEach(p -> {
                            try { Files.deleteIfExists(p); } catch (IOException ignore) {}
                        });
            }
        }
    }

    private void generate() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("x-attributes.yaml");
        spec.setInputDirectory("src/test/resources/example/contract/x-attr");
        spec.setOutputDirectory(BASE_DIR);
        spec.setPackageLocation("example.testGenerate.xattr");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));
        ctx.setValidationApi(ValidationApi.JAKARTA);
        yojoGenerator.generateAll(ctx);
    }

    private String readFile(String relativePath) throws IOException {
        return Files.readString(Paths.get(BASE_DIR + relativePath));
    }

    @Test
    @Order(1)
    void xDigitsProducesCorrectAnnotation() throws IOException {
        generate();

        String nv = readFile("common/NumericValues.java");
        // x-digits: "integer=5, fraction=0" → @Digits(integer=5, fraction=0)
        assertThat(nv)
                .contains("@Digits(integer = 5, fraction = 0)")
                .contains("private Long quantity;");
    }

    @Test
    @Order(2)
    void xRealizationOnItems() throws IOException {
        generate();

        String ce = readFile("common/CollectionExample.java");
        assertThat(ce)
                .contains("private List<Long> ids = new ArrayList<>();")
                .contains("private Set<String> names = new HashSet<>();")
                .contains("import java.util.ArrayList;")
                .contains("import java.util.HashSet;");
    }

    @Test
    @Order(3)
    void xRealizationOnMap() throws IOException {
        generate();

        String me = readFile("common/MapExample.java");
        assertThat(me)
                .contains("import java.util.LinkedHashMap;")
                .contains("private Map<String, Integer> scores = new LinkedHashMap<>();");
    }

    @Test
    @Order(4)
    void xLombokDisablesLombok() throws IOException {
        generate();

        String nv = readFile("common/NumericValues.java");
        // x-lombok: enable=false → no @Data/@Getter etc.
        assertThat(nv)
                .doesNotContain("@Data")
                .doesNotContain("@Getter");
    }

    @Test
    @Order(5)
    void xValidationGroupsOnSchema() throws IOException {
        generate();

        String rs = readFile("common/RequestDtoSchema.java");
        // x-validation-groups + x-validation-groups-imports + x-validate-by-groups
        assertThat(rs)
                .contains("@NotNull(groups = {Create.class})")
                .contains("import com.example.Validation;")
                .contains("private Integer integerValidationField;");
    }

    @Test
    @Order(6)
    void xExtendsWithXFromClass() throws IOException {
        generate();

        String dc = readFile("common/DerivedClass.java");
        assertThat(dc)
                .contains("extends BaseClass")
                .contains("private String derivedField;");
        // BaseClass is in the same .common package
        assertThat(dc)
                .contains("import example.testGenerate.xattr.common.BaseClass;");
    }

    @Test
    @Order(7)
    void xMethodsWithXDefinition() throws IOException {
        generate();

        String iface = readFile("common/InterfaceWithMethods.java");
        assertThat(iface)
                .contains("void doSomething(String input);");
    }

    @Test
    @Order(8)
    void allGeneratedCodeCompiles() throws IOException {
        generate();

        // Collect all generated .java files
        // Exclude RequestDtoSchema which references com.example.Validation/Create (not on test classpath)
        List<Path> javaFiles;
        try (Stream<Path> walk = Files.walk(Paths.get(BASE_DIR))) {
            javaFiles = walk
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> !p.getFileName().toString().contains("RequestDtoSchema"))
                    .toList();
        }

        assertThat(javaFiles).isNotEmpty();

        // Verify compilation
        javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
        javax.tools.DiagnosticCollector<javax.tools.JavaFileObject> diagnostics = new javax.tools.DiagnosticCollector<>();
        javax.tools.StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends javax.tools.JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromPaths(javaFiles);

        javax.tools.JavaCompiler.CompilationTask task =
                compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        boolean compiled = task.call();

        if (!compiled) {
            StringBuilder sb = new StringBuilder("COMPILATION FAILED:\n");
            for (javax.tools.Diagnostic<? extends javax.tools.JavaFileObject> d : diagnostics.getDiagnostics()) {
                sb.append(String.format("[%s] %s:%d:%d: %s%n",
                        d.getKind(),
                        d.getSource() != null ? d.getSource().getName() : "unknown",
                        d.getLineNumber(),
                        d.getColumnNumber(),
                        d.getMessage(null)));
            }
            Assertions.fail(sb.toString());
        }

        assertTrue(compiled, "All generated code with x- attributes must compile");
    }
}
