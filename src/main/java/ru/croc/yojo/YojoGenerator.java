package ru.croc.yojo;

import ru.croc.yojo.generator.codegen.JavaGenerator;
import ru.croc.yojo.generator.parser.YamlParser;
import ru.croc.yojo.generator.record.AsyncApiSpec;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Основной класс генератора YOJO (YAML to POJO generator).
 * Объединяет парсер YAML и генератор Java классов.
 */
public class YojoGenerator {

    private final YamlParser yamlParser;
    private final JavaGenerator javaGenerator;

    public YojoGenerator(String packageName) {
        this.yamlParser = new YamlParser();
        this.javaGenerator = new JavaGenerator(packageName);
    }

    /**
     * Генерирует Java классы из AsyncAPI YAML файла.
     *
     * @param yamlPath путь к YAML файлу
     * @param outputPath путь для вывода сгенерированных классов
     * @throws Exception в случае ошибки
     */
    public void generate(String yamlPath, String outputPath) throws Exception {
        try (InputStream yamlStream = Files.newInputStream(Paths.get(yamlPath))) {
            AsyncApiSpec spec = yamlParser.parseAsyncApiSpec(yamlStream);
            var generatedClasses = javaGenerator.generate(spec);
            
            Path outputDir = Paths.get(outputPath);
            Files.createDirectories(outputDir);
            
            for (var generatedClass : generatedClasses) {
                Path classPath = outputDir.resolve(generatedClass.className() + ".java");
                Files.write(classPath, generatedClass.content().getBytes());
            }
        }
    }

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java -jar yojo-generator.jar <yaml-file> <output-directory> <package-name>");
            System.out.println("Example: java -jar yojo-generator.jar asyncapi.yaml ./generated ru.croc.pojo");
            return;
        }

        String yamlFile = args[0];
        String outputDir = args[1];
        String packageName = args[2];

        try {
            YojoGenerator generator = new YojoGenerator(packageName);
            generator.generate(yamlFile, outputDir);
            System.out.println("Generation completed successfully!");
        } catch (Exception e) {
            System.err.println("Error during generation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}