package ru.croc.yojo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.croc.yojo.generator.codegen.JavaGenerator;
import ru.croc.yojo.generator.parser.YamlParser;
import ru.croc.yojo.generator.record.AsyncApiSpec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class YojoGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void testGenerateFromOfficialExample() throws Exception {
        // Подготовка
        String yamlPath = "examples/asyncapi_official_example.yaml";
        Path outputPath = tempDir.resolve("generated");
        String packageName = "ru.croc.test.pojo";

        // Создаем генератор
        YojoGenerator generator = new YojoGenerator(packageName);

        // Выполняем генерацию
        generator.generate(yamlPath, outputPath.toString());

        // Проверяем, что файлы были созданы
        assertTrue(Files.exists(outputPath), "Output directory should exist");
        
        // Получаем список сгенерированных файлов
        var files = Files.list(outputPath).toArray(Path[]::new);
        assertTrue(files.length > 0, "Should have generated at least one file");
    }

    @Test
    void testYamlParsing() throws IOException {
        // Подготовка
        YamlParser parser = new YamlParser();
        InputStream yamlStream = getClass().getClassLoader().getResourceAsStream("examples/asyncapi_official_example.yaml");

        // Выполняем парсинг
        AsyncApiSpec spec = parser.parseAsyncApiSpec(yamlStream);

        // Проверяем результаты
        assertNotNull(spec, "Spec should not be null");
        assertNotNull(spec.asyncapi(), "AsyncAPI version should not be null");
        assertTrue(spec.components().size() > 0, "Should have at least one component");
    }

    @Test
    void testJavaGeneration() throws IOException {
        // Подготовка
        YamlParser parser = new YamlParser();
        JavaGenerator generator = new JavaGenerator("ru.croc.test.pojo");
        InputStream yamlStream = getClass().getClassLoader().getResourceAsStream("examples/asyncapi_official_example.yaml");

        // Выполняем парсинг и генерацию
        AsyncApiSpec spec = parser.parseAsyncApiSpec(yamlStream);
        var generatedClasses = generator.generate(spec);

        // Проверяем результаты
        assertNotNull(generatedClasses, "Generated classes should not be null");
        assertTrue(generatedClasses.size() > 0, "Should have generated at least one class");
        
        // Проверяем, что каждый сгенерированный класс имеет имя и содержимое
        for (var generatedClass : generatedClasses) {
            assertNotNull(generatedClass.className(), "Class name should not be null");
            assertNotNull(generatedClass.content(), "Class content should not be null");
            assertFalse(generatedClass.content().isEmpty(), "Class content should not be empty");
        }
    }
}