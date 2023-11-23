package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.io.File;

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
                new LombokProperties(true,
                        true,
                        new LombokProperties.Accessors(true, true, true)));
        Assertions.assertTrue(new File("src/test/resources/testGenerate/test").listFiles().length != 0);
    }
}
