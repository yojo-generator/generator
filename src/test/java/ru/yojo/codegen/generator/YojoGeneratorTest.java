package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.MessageImplementationProperties;
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
        yojoGenerator.generate("src/test/resources/example.yaml",
                "src/test/resources/testSrc/",
                "ru.yojo.codegen",
                new LombokProperties(true,
                        true,
                        new LombokProperties.Accessors(true, false, true)),
                new MessageImplementationProperties("ru.yojo.codegen", "SomeClass"));
        Assertions.assertTrue(new File("src/test/resources/testSrc/example").listFiles().length != 0);
    }
}
