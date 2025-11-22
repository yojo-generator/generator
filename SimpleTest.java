import ru.croc.yojo.generator.codegen.JavaGenerator;
import ru.croc.yojo.generator.parser.YamlParser;
import ru.croc.yojo.generator.record.AsyncApiSpec;

import java.io.InputStream;

public class SimpleTest {
    public static void main(String[] args) {
        try {
            // Подготовка
            YamlParser parser = new YamlParser();
            JavaGenerator generator = new JavaGenerator("ru.croc.test.pojo");
            InputStream yamlStream = SimpleTest.class.getClassLoader().getResourceAsStream("examples/asyncapi_official_example.yaml");

            if (yamlStream == null) {
                System.out.println("YAML file not found in classpath");
                return;
            }

            // Выполняем парсинг и генерацию
            AsyncApiSpec spec = parser.parseAsyncApiSpec(yamlStream);
            var generatedClasses = generator.generate(spec);

            System.out.println("Parsed spec with " + spec.components().size() + " components");
            System.out.println("Generated " + generatedClasses.size() + " classes");

            // Проверяем результаты
            if (generatedClasses.size() > 0) {
                System.out.println("SUCCESS: Generator works correctly");
                for (var generatedClass : generatedClasses) {
                    System.out.println("Generated class: " + generatedClass.className());
                }
            } else {
                System.out.println("FAILURE: No classes were generated");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}