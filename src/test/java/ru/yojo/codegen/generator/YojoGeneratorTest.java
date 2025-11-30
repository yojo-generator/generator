package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.mapper.Helper;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {YojoGenerator.class, SchemaMapper.class, MessageMapper.class, Helper.class})
class YojoGeneratorTest {

    @Autowired
    private YojoGenerator yojoGenerator;

    @Test
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

        //  messages/ — для сообщений БЕЗ pathForGenerateMessage
        File messagesDir = new File(outputRoot, "messages");
        File commonDir = new File(outputRoot, "common");
        assertTrue(messagesDir.exists(), "messages dir must be generated");
        assertTrue(commonDir.exists(), "common dir must be generated");

        //  RequestDtoByRef — с pathForGenerateMessage → в io/github/somepath/
        File customPathDir = new File(outputRoot, "io/github/somepath");
        assertTrue(customPathDir.exists(), "Custom path dir must exist for pathForGenerateMessage");
        assertTrue(new File(customPathDir, "RequestDtoByRef.java").exists());

        //  RequestDtoByRefAndProperties — НЕТ pathForGenerateMessage → в messages/
        assertTrue(new File(messagesDir, "RequestDtoByRefAndProperties.java").exists());

        //  RequestDtoWithProperties — в messages/
        assertTrue(new File(messagesDir, "RequestDtoWithProperties.java").exists());

        //  RequestDtoWithDoubleInheritance — в messages/
        assertTrue(new File(messagesDir, "RequestDtoWithDoubleInheritance.java").exists());

        //  RequestDtoInheritanceFromSchema — в messages/
        assertTrue(new File(messagesDir, "RequestDtoInheritanceFromSchema.java").exists());

        //  RequestDtoSchema — в common/ (в корне, т.к. в test.yaml)
        assertTrue(new File(commonDir, "RequestDtoSchema.java").exists());

        //  SomeObject — в common/
        assertTrue(new File(commonDir, "SomeObject.java").exists());

        //  Убедимся, что папки test/ и test-spec/ НЕ создаются
        Assertions.assertFalse(new File(outputRoot, "test").exists());
        Assertions.assertFalse(new File(outputRoot, "test-spec").exists());
    }

    @Test
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
        assertTrue(new File(out, "messages/LightMeasured.java").exists());
        assertTrue(new File(out, "common/LightMeasuredPayload.java").exists());
    }

    @Test
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
    }

    @Test
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
    }

    @Test
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
        assertTrue(new File(out, "messages/LightMeasured.java").exists());
        assertTrue(new File(out, "common/LightMeasuredPayload.java").exists());
    }

    @Test
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
    }

    @Test
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
    }

    @Test
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

        list.add(specSlack);
        list.add(specGitter);
        list.add(specAsync);
        list.add(spec);

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(list);
        ctx.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));

        yojoGenerator.generateAll(ctx);

        File out = new File("src/test/resources/example/testGenerate/slack/");
    }
}
