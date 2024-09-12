package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.mapper.Helper;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.io.File;
import java.io.IOException;

@SpringBootTest(classes = {YojoGenerator.class, SchemaMapper.class, MessageMapper.class, Helper.class})
class YojoGeneratorTest {

    @Autowired
    private YojoGenerator yojoGenerator;

    @Test
    @SuppressWarnings("all")
    void generateTest() {
        yojoGenerator.generate("src/test/resources/example/contract/test.yaml",
                "src/test/resources/example/testGenerate/",
                "example.testGenerate",
                new LombokProperties(false,
                        true,
                        new Accessors(true, true, true)));
        File file = new File("src/test/resources/example/testGenerate/test");
        Assertions.assertTrue(file.listFiles().length != 0);
        //cleanUp
        Assertions.assertTrue(FileSystemUtils.deleteRecursively(file));
    }

    @Test
    void generateAllWithSpringBootVersion() throws IOException {
        YojoContext yojoContext = new YojoContext();
        yojoContext.setSpringBootVersion("2.0.0");
        yojoContext.setDirectory("src/test/resources/example");
        yojoContext.setOutputDirectory("src/test/resources/example/testGenerate/");
        yojoContext.setPackageLocation("example.testGenerate");
        yojoContext.setLombokProperties(new LombokProperties(false,
                true,
                new Accessors(true, true, true)));

        yojoGenerator.generateAll(yojoContext);

        File file = new File("src/test/resources/example/testGenerate/");
        Assertions.assertTrue(file.exists());
        //cleanUp
        Assertions.assertTrue(FileSystemUtils.deleteRecursively(file));
    }

    @Test
    void generateAll() throws IOException {
        yojoGenerator.generateAll("src/test/resources/example",
                "src/test/resources/example/testGenerate/",
                "example.testGenerate",
                new LombokProperties(false,
                true,
                new Accessors(true, true, true)));
        File file = new File("src/test/resources/example/testGenerate");
        Assertions.assertTrue(file.exists());
        //cleanUp
        Assertions.assertTrue(FileSystemUtils.deleteRecursively(file));
    }
}
