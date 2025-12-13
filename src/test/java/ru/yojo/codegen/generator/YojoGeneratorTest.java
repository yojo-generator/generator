package ru.yojo.codegen.generator;

import org.junit.jupiter.api.*;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class YojoGeneratorTest {

    private YojoGenerator yojoGenerator = new YojoGenerator();

    private static final List<String> OUTPUT_DIRECTORIES = List.of(
            "src/test/resources/example/testGenerate/",
            "src/test/resources/example/testGenerate/asyncapi/lombok/",
            "src/test/resources/example/testGenerate/gitter/lombok/",
            "src/test/resources/example/testGenerate/slack/lombok/",
            "src/test/resources/example/testGenerate/asyncapi/",
            "src/test/resources/example/testGenerate/gitter/",
            "src/test/resources/example/testGenerate/slack/",
            "src/test/resources/example/testGenerate/specFromIssue/",
            "src/test/resources/example/testGenerate/oneMore/"
    );

    @BeforeEach
    void cleanupBeforeTest() throws IOException {
        // Очищаем все выходные директории перед каждым тестом
        for (String dir : OUTPUT_DIRECTORIES) {
            Path path = Paths.get(dir);
            if (Files.exists(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    walk.sorted((a, b) -> -a.compareTo(b))
                            .forEach(p -> {
                                try {
                                    Files.deleteIfExists(p);
                                } catch (IOException e) {
                                    // Игнорируем ошибки удаления
                                }
                            });
                }
                Files.deleteIfExists(path);
            }
        }
    }

    @AfterAll
    static void cleanupAfterAll() throws IOException {
        for (String dir : OUTPUT_DIRECTORIES) {
            Path path = Paths.get(dir);
            if (Files.exists(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    walk.sorted((a, b) -> -a.compareTo(b))
                            .forEach(p -> {
                                try {
                                    Files.deleteIfExists(p);
                                } catch (IOException e) {
                                    // Игнорируем ошибки удаления
                                }
                            });
                }
                Files.deleteIfExists(path);
            }
        }

        Path parentDir = Paths.get("src/test/resources/example/testGenerate");
        if (Files.exists(parentDir)) {
            try (Stream<Path> stream = Files.list(parentDir)) {
                if (stream.findAny().isEmpty()) {
                    Files.deleteIfExists(parentDir);
                }
            }
        }
    }

    @Test
    @Order(1)
    void generateAllWithSingleSpecificationAndSpringBootVersionLombok() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("test.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory("src/test/resources/example/testGenerate/");
        spec.setPackageLocation("example.testGenerate");

        YojoContext yojoContext = new YojoContext();
        yojoContext.setSpringBootVersion("2.7.0");
        yojoContext.setSpecificationProperties(Collections.singletonList(spec));
        yojoContext.setLombokProperties(new LombokProperties(true, false, new Accessors(false, false, false)));

        yojoGenerator.generateAll(yojoContext);

        File outputRoot = new File("src/test/resources/example/testGenerate/");
        assertTrue(outputRoot.exists(), "Output directory must exist");

        File messagesDir = new File(outputRoot, "messages");
        File commonDir = new File(outputRoot, "common");
        assertTrue(messagesDir.exists(), "messages dir must be generated");
        assertTrue(commonDir.exists(), "common dir must be generated");

        File customPathDir = new File(outputRoot, "io/github/somepath");
        assertTrue(customPathDir.exists(), "Custom path dir must exist for pathForGenerateMessage");
        assertTrue(new File(customPathDir, "RequestDtoByRef.java").exists());

        assertTrue(new File(messagesDir, "RequestDtoByRefAndProperties.java").exists());
        assertTrue(new File(messagesDir, "RequestDtoWithProperties.java").exists());
        assertTrue(new File(messagesDir, "RequestDtoWithDoubleInheritance.java").exists());
        assertTrue(new File(messagesDir, "RequestDtoInheritanceFromSchema.java").exists());
        assertTrue(new File(commonDir, "RequestDtoSchema.java").exists());
        assertTrue(new File(commonDir, "SomeObject.java").exists());

        assertFalse(new File(outputRoot, "test").exists());
        assertFalse(new File(outputRoot, "test-spec").exists());
    }

    @Test
    @Order(2)
    void generateWithAsyncApiV3Lombok() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("async-api-official-v3.0.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory("src/test/resources/example/testGenerate/asyncapi/lombok/");
        spec.setPackageLocation("example.testGenerate.asyncapi.lombok");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(true, false, new Accessors(false, false, false)));

        yojoGenerator.generateAll(ctx);

        File out = new File("src/test/resources/example/testGenerate/asyncapi/lombok/");
        assertTrue(out.exists(), "Output directory must exist");
        assertTrue(new File(out, "messages/LightMeasured.java").exists());
        assertTrue(new File(out, "common/LightMeasuredPayload.java").exists());
    }

    @Test
    @Order(3)
    void generateWithAsyncApiV3GitterLombok() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("gitter-streaming-async-api-v3.0.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory("src/test/resources/example/testGenerate/gitter/lombok/");
        spec.setPackageLocation("example.testGenerate.gitter.lombok");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(true, false, new Accessors(false, false, false)));

        yojoGenerator.generateAll(ctx);

        File out = new File("src/test/resources/example/testGenerate/gitter/lombok/");
        assertTrue(out.exists(), "Output directory must exist");
    }

    @Test
    @Order(4)
    void generateWithAsyncApiV3SlackLombok() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("slack-real-time-async-api-v3.0.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory("src/test/resources/example/testGenerate/slack/lombok/");
        spec.setPackageLocation("example.testGenerate.slack.lombok");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(true, false, new Accessors(false, false, false)));

        yojoGenerator.generateAll(ctx);

        File out = new File("src/test/resources/example/testGenerate/slack/lombok/");
        assertTrue(out.exists(), "Output directory must exist");
    }

    @Test
    @Order(5)
    void generateWithAsyncApiV3() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("async-api-official-v3.0.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory("src/test/resources/example/testGenerate/asyncapi/");
        spec.setPackageLocation("example.testGenerate.asyncapi");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));

        yojoGenerator.generateAll(ctx);

        File out = new File("src/test/resources/example/testGenerate/asyncapi/");
        assertTrue(out.exists(), "Output directory must exist");
        assertTrue(new File(out, "messages/LightMeasured.java").exists());
        assertTrue(new File(out, "common/LightMeasuredPayload.java").exists());
    }

    @Test
    @Order(6)
    void generateWithAsyncApiV3Gitter() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("gitter-streaming-async-api-v3.0.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory("src/test/resources/example/testGenerate/gitter/");
        spec.setPackageLocation("example.testGenerate.gitter");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));

        yojoGenerator.generateAll(ctx);

        File out = new File("src/test/resources/example/testGenerate/gitter/");
        assertTrue(out.exists(), "Output directory must exist");
    }

    @Test
    @Order(7)
    void generateWithAsyncApiV3Slack() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("slack-real-time-async-api-v3.0.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory("src/test/resources/example/testGenerate/slack/");
        spec.setPackageLocation("example.testGenerate.slack");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));

        yojoGenerator.generateAll(ctx);

        File out = new File("src/test/resources/example/testGenerate/slack/");
        assertTrue(out.exists(), "Output directory must exist");
    }

    @Test
    @Order(8)
    void generateTest() throws IOException {
        List<SpecificationProperties> list = new ArrayList<>();
        SpecificationProperties specSlack = new SpecificationProperties();
        specSlack.setSpecName("slack-real-time-async-api-v3.0.yaml");
        specSlack.setInputDirectory("src/test/resources/example/contract");
        specSlack.setOutputDirectory("src/test/resources/example/testGenerate/slack/");
        specSlack.setPackageLocation("example.testGenerate.slack");

        SpecificationProperties specGitter = new SpecificationProperties();
        specGitter.setSpecName("gitter-streaming-async-api-v3.0.yaml");
        specGitter.setInputDirectory("src/test/resources/example/contract");
        specGitter.setOutputDirectory("src/test/resources/example/testGenerate/gitter/");
        specGitter.setPackageLocation("example.testGenerate.gitter");

        SpecificationProperties specAsync = new SpecificationProperties();
        specAsync.setSpecName("async-api-official-v3.0.yaml");
        specAsync.setInputDirectory("src/test/resources/example/contract");
        specAsync.setOutputDirectory("src/test/resources/example/testGenerate/asyncapi/");
        specAsync.setPackageLocation("example.testGenerate.asyncapi");

        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("test.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory("src/test/resources/example/testGenerate/");
        spec.setPackageLocation("example.testGenerate");

        SpecificationProperties specFromIssue = new SpecificationProperties();
        specFromIssue.setSpecName("spec-from-issue.yaml");
        specFromIssue.setInputDirectory("src/test/resources/example/contract");
        specFromIssue.setOutputDirectory("src/test/resources/example/testGenerate/specFromIssue/");
        specFromIssue.setPackageLocation("example.testGenerate.specFromIssue");

        SpecificationProperties oneMore = new SpecificationProperties();
        oneMore.setSpecName("one-more.yaml");
        oneMore.setInputDirectory("src/test/resources/example/contract");
        oneMore.setOutputDirectory("src/test/resources/example/testGenerate/oneMore/");
        oneMore.setPackageLocation("example.testGenerate.oneMore");

        list.add(oneMore);
        list.add(specFromIssue);
        list.add(specSlack);
        list.add(specGitter);
        list.add(specAsync);
        list.add(spec);

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(list);
        ctx.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));

        yojoGenerator.generateAll(ctx);

        assertTrue(new File("src/test/resources/example/testGenerate/slack/").exists());
        assertTrue(new File("src/test/resources/example/testGenerate/gitter/").exists());
        assertTrue(new File("src/test/resources/example/testGenerate/asyncapi/").exists());
        assertTrue(new File("src/test/resources/example/testGenerate/specFromIssue/").exists());
        assertTrue(new File("src/test/resources/example/testGenerate/oneMore/").exists());
        assertTrue(new File("src/test/resources/example/testGenerate/").exists());
    }

    @Test
    @Order(9)
    void testPatternEscaping() {
        VariableProperties vp = new VariableProperties();
        vp.setSpringBootVersion("3.2.0");
        vp.setPattern("^\\d+\\.\\d{2}$");  // ← исходный паттерн с одним \ (как в YAML)

        Set<String> annotations = vp.getAnnotationSet();
        assertThat(annotations).containsExactly("@Pattern(regexp = \"^\\\\d+\\\\.\\\\d{2}$\")");

        // Проверим, что импорт добавлен
        Set<String> imports = vp.getRequiredImports();
        assertThat(imports).contains("jakarta.validation.constraints.Pattern;");
    }
}