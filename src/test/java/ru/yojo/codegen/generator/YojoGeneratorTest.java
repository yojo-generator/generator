package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.io.File;
import java.util.Objects;

@SpringBootTest(classes = {YojoGenerator.class, SchemaMapper.class})
public class YojoGeneratorTest {

    @Autowired
    private YojoGenerator yojoGenerator;

    @Test
    public void generateTest() {
        yojoGenerator.generate("src/test/resources/example.yaml", "src/test/resources/testSrc/", new LombokProperties(true, true, true));

        //clean after test
        for (File file : Objects.requireNonNull(new File("src/test/resources/testSrc/").listFiles()))
            if (!file.isDirectory())
                file.delete();
    }
}
