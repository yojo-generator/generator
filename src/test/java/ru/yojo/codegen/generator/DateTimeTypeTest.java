package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yojo.codegen.YojoCLI;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.DateTimeType;
import ru.yojo.codegen.domain.ValidationApi;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for the configurable Java type of {@code format: date-time} ({@link YojoContext#setDateTimeType}).
 */
class DateTimeTypeTest {

    private static final Path INPUT_DIR = Path.of("src/test/resources/example/contract");
    private static final String SPEC = "date-time-type.yaml";

    @TempDir
    Path outputDir;

    @Test
    void offsetDateTimeByDefault() throws IOException {
        generate(null, true);

        assertThat(read("common/Event.java"))
                .contains("import java.time.OffsetDateTime;")
                .contains("private OffsetDateTime createdAt;")
                .contains("private List<OffsetDateTime> history;")
                .contains("private Set<OffsetDateTime> checkpoints;")
                .contains("private LocalDate birthDate;")
                .contains("private Map<OffsetDateTime, String> byTime;")
                .doesNotContain("Instant");

        assertThat(read("messages/EventMessage.java"))
                .contains("import java.time.OffsetDateTime;")
                .contains("private OffsetDateTime occurredAt;")
                .doesNotContain("Instant");
    }

    @Test
    void offsetDateTimeWhenConfiguredExplicitly() throws IOException {
        generate(DateTimeType.OFFSET_DATE_TIME, true);

        assertThat(read("common/Event.java"))
                .contains("private OffsetDateTime createdAt;")
                .doesNotContain("Instant");
    }

    @Test
    void instantWhenConfiguredWithLombok() throws IOException {
        generate(DateTimeType.INSTANT, true);

        assertThat(read("common/Event.java"))
                .contains("import java.time.Instant;")
                .contains("private Instant createdAt;")
                .contains("private List<Instant> history;")
                .contains("private Set<Instant> checkpoints;")
                .contains("private LocalDate birthDate;")
                .contains("private Map<Instant, String> byTime;")
                .doesNotContain("OffsetDateTime")
                .doesNotContain("common.Instant");

        assertThat(read("messages/EventMessage.java"))
                .contains("import java.time.Instant;")
                .contains("private Instant occurredAt;")
                .doesNotContain("OffsetDateTime");
    }

    @Test
    void instantWhenConfiguredWithoutLombok() throws IOException {
        generate(DateTimeType.INSTANT, false);

        assertThat(read("common/Event.java"))
                .contains("import java.time.Instant;")
                .contains("private Instant createdAt;")
                .contains("public Instant getCreatedAt()")
                .contains("public void setHistory(List<Instant> history)")
                .doesNotContain("OffsetDateTime");

        assertThat(read("messages/EventMessage.java"))
                .contains("private Instant occurredAt;")
                .doesNotContain("OffsetDateTime");
    }

    @Test
    void generatedCodeWithInstantCompilesWithLombok() throws IOException {
        generate(DateTimeType.INSTANT, true);
        compileGenerated();
    }

    @Test
    void generatedCodeWithInstantCompilesWithoutLombok() throws IOException {
        generate(DateTimeType.INSTANT, false);
        compileGenerated();
    }

    @Test
    void cliDefaultsToOffsetDateTimeWithLegacyArguments() throws IOException, InterruptedException {
        generateFromCli(INPUT_DIR.resolve(SPEC));
        assertThat(read("common/Event.java")).contains("private OffsetDateTime createdAt;");
        assertMapTypes("OffsetDateTime");
        compileGenerated();
    }

    @Test
    void cliStillAcceptsDirectoryContainingCliSpec() throws IOException, InterruptedException {
        Path inputDir = Files.createDirectory(outputDir.resolve("input"));
        Files.copy(INPUT_DIR.resolve(SPEC), inputDir.resolve("cli-spec"));
        generateFromCli(inputDir, "false", "false", "false");
        assertThat(read("common/Event.java")).contains("private OffsetDateTime createdAt;");
    }

    @Test
    void cliAcceptsInstantAsAnOptionalLastArgument() throws IOException, InterruptedException {
        generateFromCli(INPUT_DIR.resolve(SPEC), "false", "false", "false", "INSTANT");
        assertThat(read("common/Event.java")).contains("private Instant createdAt;");
        assertMapTypes("Instant");
        compileGenerated();
    }

    private void generateFromCli(Path input, String... optionalArgs) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>(List.of(
                Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                "-cp", System.getProperty("java.class.path"),
                YojoCLI.class.getName(),
                input.toString(),
                outputDir.toString(),
                "example.dateTimeType"
        ));
        command.addAll(List.of(optionalArgs));
        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertThat(process.waitFor()).as("YojoCLI output:%n%s", output).isZero();
    }

    private void assertMapTypes(String dateTimeClass) throws IOException {
        assertThat(read("common/Event.java"))
                .contains("private Map<%s, %s> timeToTime;".formatted(dateTimeClass, dateTimeClass));
        assertThat(read("common/MapValues.java"))
                .contains("import java.time.%s;".formatted(dateTimeClass))
                .contains("private Map<String, %s> scalar;".formatted(dateTimeClass))
                .contains("private Map<UUID, %s> byId;".formatted(dateTimeClass));
        assertThat(read("common/MapCollectionValues.java"))
                .contains("import java.time.%s;".formatted(dateTimeClass))
                .contains("private Map<String, List<%s>> lists;".formatted(dateTimeClass))
                .contains("private Map<UUID, Set<%s>> sets;".formatted(dateTimeClass))
                .contains("private Map<String, List<%s>> listsWithRef;".formatted(dateTimeClass))
                .contains("private Map<UUID, Set<%s>> setsWithRef;".formatted(dateTimeClass))
                .doesNotContain("import example.dateTimeType.common.Event;");
    }

    private void generate(DateTimeType dateTimeType, boolean useLombok) throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName(SPEC);
        spec.setInputDirectory(INPUT_DIR.toString());
        spec.setOutputDirectory(outputDir.toString());
        spec.setPackageLocation("example.dateTimeType");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(useLombok, true, true,
                new Accessors(false, false, false)));
        ctx.setValidationApi(ValidationApi.JAKARTA);
        ctx.setDateTimeType(dateTimeType);
        new YojoGenerator().generateAll(ctx);
    }

    private void compileGenerated() throws IOException {
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

    private String read(String relativePath) throws IOException {
        return Files.readString(outputDir.resolve(relativePath));
    }
}
