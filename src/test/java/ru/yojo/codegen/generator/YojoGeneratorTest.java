package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.io.File;
import java.io.IOException;

@SpringBootTest(classes = {YojoGenerator.class, SchemaMapper.class, MessageMapper.class})
public class YojoGeneratorTest {

    @Autowired
    private YojoGenerator yojoGenerator;

    @Test
    @SuppressWarnings("all")
    public void generateTest() {
        yojoGenerator.generate("src/test/resources/test.yaml",
                "src/test/resources/testGenerate/",
                "testGenerate",
                new LombokProperties(false,
                        true,
                        new Accessors(true, true, true)));
        File file = new File("src/test/resources/testGenerate/test");
        Assertions.assertTrue(file.listFiles().length != 0);
        //cleanUp
        Assertions.assertTrue(FileSystemUtils.deleteRecursively(file));
    }

    @Test
    public void generateAll() throws IOException {
        yojoGenerator.generateAll("src/test/resources/", "src/test/resources/testGenerate/", "testGenerate", new LombokProperties(false,
                true,
                new Accessors(true, true, true)));
        File file = new File("src/test/resources/testGenerate/test");
        Assertions.assertTrue(file.exists());
        //cleanUp
        Assertions.assertTrue(FileSystemUtils.deleteRecursively(file));
    }
}
