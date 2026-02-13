package ru.yojo.codegen.generator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.generator.YojoGenerator;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the @Email annotation support (Issue #33).
 */
public class EmailFormatTest {

    private static final String OUTPUT_BASE_DIR = "src/test/resources/email_test_output/";
    private YojoGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new YojoGenerator();
        cleanupOutputDir();
    }

    @AfterEach
    void tearDown() {
        cleanupOutputDir();
    }

    @AfterAll
    static void tearDownAll() {
        try {
            Path path = Paths.get(OUTPUT_BASE_DIR.replace("output", "input"));
            if (Files.exists(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    walk.sorted(Collections.reverseOrder())
                            .forEach(p -> {
                                try {
                                    Files.delete(p);
                                } catch (IOException e) {
                                    // Ignore
                                }
                            });
                }
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    private void cleanupOutputDir() {
        try {
            Path path = Paths.get(OUTPUT_BASE_DIR);
            if (Files.exists(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    walk.sorted(Collections.reverseOrder())
                            .forEach(p -> {
                                try {
                                    Files.delete(p);
                                } catch (IOException e) {
                                    // Ignore
                                }
                            });
                }
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    private void generateSpec(String specContent, String outputSubDir, String packageLocation, String springBootVersion) throws IOException {
        Path inputDir = Paths.get("src/test/resources/email_test_input/");
        Files.createDirectories(inputDir);
        Path specPath = inputDir.resolve("email_test_spec.yaml");
        Files.writeString(specPath, specContent);

        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("email_test_spec.yaml");
        spec.setInputDirectory(inputDir.toString());
        spec.setOutputDirectory(OUTPUT_BASE_DIR + outputSubDir);
        spec.setPackageLocation(packageLocation);

        YojoContext context = new YojoContext();
        context.setSpecificationProperties(Collections.singletonList(spec));
        context.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));
        context.setSpringBootVersion(springBootVersion);

        generator.generateAll(context);
    }

    private String readFile(String relativePath) throws IOException {
        Path path = Paths.get(OUTPUT_BASE_DIR + relativePath);
        assertTrue(Files.exists(path), "Generated file not found: " + path);
        return Files.readString(path);
    }

    private List<Path> collectAllJavaFiles() throws IOException {
        List<Path> javaFiles = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(OUTPUT_BASE_DIR))) {
            javaFiles.addAll(walk.filter(p -> p.toString().endsWith(".java")).collect(Collectors.toList()));
        }
        assertFalse(javaFiles.isEmpty(), "No .java files were generated for compilation test.");
        return javaFiles;
    }

    private void compileGeneratedCode() throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            List<String> options = new ArrayList<>();
            String classpath = System.getProperty("java.class.path") +
                               File.pathSeparator + Paths.get("src/test/resources").toAbsolutePath().toString();
            options.addAll(Arrays.asList("-cp", classpath));

            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromPaths(collectAllJavaFiles());
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);

            boolean success = task.call();
            if (!success) {
                StringBuilder sb = new StringBuilder("COMPILATION FAILED:\n");
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                        sb.append(String.format(
                                "[%s] %s:%d:%d: %s%n",
                                diagnostic.getKind(),
                                diagnostic.getSource() != null ? diagnostic.getSource().getName() : "unknown",
                                diagnostic.getLineNumber(),
                                diagnostic.getColumnNumber(),
                                diagnostic.getMessage(null)
                        ));
                    }
                }
                fail(sb.toString());
            }
        }
    }

    // --- Тесты ---

    @Test
    void shouldGenerateJakartaEmailAnnotationForSimpleEmailField() throws IOException {
        String spec = """
            asyncapi: 2.6.0
            info:
              title: Email Test API
              version: 1.0.0
            channels:
              test/channel:
                publish:
                  message:
                    $ref: '#/components/messages/TestMessage'
            components:
              messages:
                TestMessage:
                  payload:
                    $ref: '#/components/schemas/EmailPayload'
              schemas:
                EmailPayload:
                  type: object
                  properties:
                    contactEmail:
                      type: string
                      format: email
            """;

        generateSpec(spec, "simple", "email.test.simple", "3.0.0");

        String dtoClass = readFile("simple/messages/TestMessage.java");
        assertTrue(dtoClass.contains("@Email"), "Should contain @Email annotation");
        assertTrue(dtoClass.contains("import jakarta.validation.constraints.Email;"), "Should import jakarta Email");
        assertTrue(dtoClass.contains("private String contactEmail;"), "Field should be String");
    }

    @Test
    void shouldGenerateJavaxEmailAnnotationForSpringBoot2() throws IOException {
        String spec = """
            asyncapi: 2.6.0
            info:
              title: Email Test API
              version: 1.0.0
            channels:
              test/channel:
                publish:
                  message:
                    $ref: '#/components/messages/TestMessage'
            components:
              messages:
                TestMessage:
                  payload:
                    $ref: '#/components/schemas/EmailPayload'
              schemas:
                EmailPayload:
                  type: object
                  properties:
                    adminEmail:
                      type: string
                      format: email
            """;

        generateSpec(spec, "sb2", "email.test.sb2", "2.7.0");

        String dtoClass = readFile("sb2/messages/TestMessage.java");
        assertTrue(dtoClass.contains("@Email"), "Should contain @Email annotation");
        assertTrue(dtoClass.contains("import javax.validation.constraints.Email;"), "Should import javax Email");
    }

    @Test
    void shouldGenerateBothNotBlankAndEmailForRequiredEmailField() throws IOException {
        String spec = """
            asyncapi: 2.6.0
            info:
              title: Email Test API
              version: 1.0.0
            channels:
              test/channel:
                publish:
                  message:
                    $ref: '#/components/messages/TestMessage'
            components:
              messages:
                TestMessage:
                  payload:
                    $ref: '#/components/schemas/EmailPayload'
              schemas:
                EmailPayload:
                  type: object
                  required:
                    - userEmail
                  properties:
                    userEmail:
                      type: string
                      format: email
            """;

        generateSpec(spec, "required", "email.test.required", "3.0.0");

        String dtoClass = readFile("required/messages/TestMessage.java");
        assertTrue(dtoClass.contains("@NotBlank"), "Required email should have @NotBlank");
        assertTrue(dtoClass.contains("@Email"), "Required email should also have @Email");
    }

    @Test
    void shouldGenerateEmailAnnotationEvenIfNotRequired() throws IOException {
        String spec = """
            asyncapi: 2.6.0
            info:
              title: Email Test API
              version: 1.0.0
            channels:
              test/channel:
                publish:
                  message:
                    $ref: '#/components/messages/TestMessage'
            components:
              messages:
                TestMessage:
                  payload:
                    $ref: '#/components/schemas/EmailPayload'
              schemas:
                EmailPayload:
                  type: object
                  properties:
                    optionalEmail:
                      type: string
                      format: email
            """;

        generateSpec(spec, "optional", "email.test.optional", "3.0.0");

        String dtoClass = readFile("optional/messages/TestMessage.java");
        assertTrue(dtoClass.contains("@Email"), "Optional email should still have @Email");
        assertFalse(dtoClass.contains("@NotBlank"), "Should NOT have @NotBlank if not required");
    }

    @Test
    void allGeneratedCodeWithEmailAnnotationsMustCompile() throws IOException {
        String spec = """
            asyncapi: 2.6.0
            info:
              title: Compilation Test API
              version: 1.0.0
            channels:
              user/contact:
                publish:
                  message:
                    $ref: '#/components/messages/ContactMessage'
            components:
              messages:
                ContactMessage:
                  payload:
                    $ref: '#/components/schemas/ContactPayload'
              schemas:
                ContactPayload:
                  type: object
                  required:
                    - primaryEmail
                  properties:
                    primaryEmail:
                      type: string
                      format: email
                    backupEmail:
                      type: string
                      format: email
            """;

        generateSpec(spec, "compilation", "email.test.compilation", "3.2.0");

        compileGeneratedCode();
    }
}