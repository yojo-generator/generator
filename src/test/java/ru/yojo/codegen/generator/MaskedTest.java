package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.ValidationApi;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.exception.SchemaFillException;
import ru.yojo.codegen.generator.support.TestMasker;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@code x-masked}: the generated {@code toString()} shows a masked value
 * instead of the field value.
 * <ul>
 *   <li>{@code x-masked: true}: the default mask {@code ***}, {@code null} is shown as is</li>
 *   <li>{@code x-masked: <method>}: the result of the mask method ({@link TestMasker#mask(Object)})</li>
 *   <li>{@code x-masked: false}: the value is shown as is</li>
 * </ul>
 * With Lombok the masked field gets {@code @ToString.Exclude} and a private method
 * with {@code @ToString.Include}; without Lombok the manual {@code toString()} masks the value.
 * {@code x-masked} inside {@code items} stops the generation.
 */
class MaskedTest {

    private static final String SPEC = "x-masked.yaml";
    private static final String PACKAGE = "example.masked";
    private static final String MASK_CALL = "ru.yojo.codegen.generator.support.TestMasker.mask";
    private static final String DEFAULT_MASK = "***";

    @TempDir
    Path outputDir;

    @TempDir
    Path classesDir;

    @Test
    void lombokSchemaExcludesMaskedFieldsAndAddsMaskMethods() throws IOException {
        generate(true);

        String user = read("common/MaskedUser.java");
        assertThat(user)
                .contains("import lombok.ToString;")
                .contains("@ToString.Exclude" + System.lineSeparator() + "    private String password;")
                .contains("@ToString.Exclude" + System.lineSeparator() + "    private UUID token;")
                .contains("@ToString.Exclude" + System.lineSeparator() + "    private Integer age;")
                .contains("@ToString.Exclude" + System.lineSeparator() + "    private MaskedUserStatus status;")
                .contains("@ToString.Include(name = \"password\")")
                .contains("private String toStringMaskedPassword() {")
                .contains("return this.password == null ? null : \"" + DEFAULT_MASK + "\";")
                .contains("@ToString.Include(name = \"token\")")
                .contains("return " + MASK_CALL + "(this.token);")
                .contains("return this.age == null ? null : \"" + DEFAULT_MASK + "\";")
                .as("enum field is masked as any other field")
                .contains("return this.status == null ? null : \"" + DEFAULT_MASK + "\";");
        assertThat(user)
                .as("fields without mask and with x-masked: false stay as is")
                .contains("    private String login;")
                .contains("    private String nickname;")
                .doesNotContain("toStringMaskedLogin")
                .doesNotContain("toStringMaskedNickname");
        assertThat(user)
                .as("field excluded by x-field-annotation needs no mask with Lombok")
                .doesNotContain("toStringMaskedSecretNote");
    }

    @Test
    void lombokMessageExcludesMaskedFieldsAndAddsMaskMethods() throws IOException {
        generate(true);

        String event = read("messages/MaskedEvent.java");
        assertThat(event)
                .contains("import lombok.ToString;")
                .contains("@ToString.Exclude" + System.lineSeparator() + "    private String body;")
                .contains("@ToString.Exclude" + System.lineSeparator() + "    private List<String> phones;")
                .contains("return this.body == null ? null : \"" + DEFAULT_MASK + "\";")
                .contains("return " + MASK_CALL + "(this.phones);");

        String plain = read("messages/PlainEvent.java");
        assertThat(plain)
                .as("message without masks stays as is")
                .doesNotContain("ToString");
    }

    @Test
    void manualToStringMasksValues() throws IOException {
        generate(false);

        String user = read("common/MaskedUser.java");
        assertThat(user)
                .contains("\"login=\" + login")
                .contains("\"nickname=\" + nickname")
                .contains("\"password=\" + (this.password == null ? null : \"" + DEFAULT_MASK + "\")")
                .contains("\"token=\" + " + MASK_CALL + "(this.token)")
                .contains("\"age=\" + (this.age == null ? null : \"" + DEFAULT_MASK + "\")")
                .contains("\"status=\" + (this.status == null ? null : \"" + DEFAULT_MASK + "\")")
                .as("manual toString() does not read lombok.ToString.Exclude, so the mask is applied")
                .contains("\"secretNote=\" + (this.secretNote == null ? null : \"" + DEFAULT_MASK + "\")");

        String event = read("messages/MaskedEvent.java");
        assertThat(event)
                .as("message without Lombok gets toString() when a field is masked")
                .contains("public String toString() {")
                .contains("\"eventId=\" + eventId")
                .contains("\"body=\" + (this.body == null ? null : \"" + DEFAULT_MASK + "\")")
                .contains("\"phones=\" + " + MASK_CALL + "(this.phones)");

        String plain = read("messages/PlainEvent.java");
        assertThat(plain)
                .as("message without masks and without Lombok still has no toString()")
                .doesNotContain("toString()");
    }

    @Test
    void lombokToStringShowsMaskedValues() throws Exception {
        generate(true);
        ClassLoader classLoader = compile();

        Object user = newMaskedUser(classLoader);
        assertThat(user.toString())
                .as("Lombok shows the masked fields after the other fields")
                .isEqualTo("MaskedUser(login=john, nickname=jj, password=" + DEFAULT_MASK
                        + ", token=" + TestMasker.MASK + ", age=" + DEFAULT_MASK + ", status=" + DEFAULT_MASK + ")");

        Object event = newMaskedEvent(classLoader);
        assertThat(event.toString())
                .isEqualTo("MaskedEvent(eventId=event-1, body=" + DEFAULT_MASK + ", phones=" + TestMasker.MASK + ")");
    }

    @Test
    void manualToStringShowsMaskedValues() throws Exception {
        generate(false);
        ClassLoader classLoader = compile();

        Object user = newMaskedUser(classLoader);
        assertThat(user.toString())
                .isEqualTo("MaskedUser{login=john, nickname=jj, password=" + DEFAULT_MASK
                        + ", token=" + TestMasker.MASK + ", age=" + DEFAULT_MASK + ", status=" + DEFAULT_MASK
                        + ", secretNote=" + DEFAULT_MASK + "}");

        Object event = newMaskedEvent(classLoader);
        assertThat(event.toString())
                .isEqualTo("MaskedEvent{eventId=event-1, body=" + DEFAULT_MASK + ", phones=" + TestMasker.MASK + "}");
    }

    @Test
    void lombokToStringShowsNullValues() throws Exception {
        generate(true);
        ClassLoader classLoader = compile();

        Object user = classLoader.loadClass(PACKAGE + ".common.MaskedUser").getConstructor().newInstance();
        assertThat(user.toString())
                .as("the default mask shows null as is, the mask method gets null")
                .isEqualTo("MaskedUser(login=null, nickname=null, password=null, token=null, age=null, status=null)");
    }

    @Test
    void manualToStringShowsNullValues() throws Exception {
        generate(false);
        ClassLoader classLoader = compile();

        Object user = classLoader.loadClass(PACKAGE + ".common.MaskedUser").getConstructor().newInstance();
        assertThat(user.toString())
                .isEqualTo("MaskedUser{login=null, nickname=null, password=null, token=null, age=null, status=null, secretNote=null}");
    }

    @Test
    void maskInsideSchemaItemsStopsGeneration() {
        assertThatThrownBy(() -> generate("x-masked-in-items-schema.yaml", true))
                .isInstanceOf(SchemaFillException.class)
                .hasMessageContaining("x-masked")
                .hasMessageContaining("items")
                .hasMessageContaining("phones")
                .hasMessageContaining("MaskedPhones");
    }

    @Test
    void maskInsideMessageItemsStopsGeneration() {
        assertThatThrownBy(() -> generate("x-masked-in-items-message.yaml", false))
                .isInstanceOf(SchemaFillException.class)
                .hasMessageContaining("x-masked")
                .hasMessageContaining("items")
                .hasMessageContaining("phones");
    }

    private Object newMaskedUser(ClassLoader classLoader) throws Exception {
        Class<?> type = classLoader.loadClass(PACKAGE + ".common.MaskedUser");
        Object user = type.getConstructor().newInstance();
        type.getMethod("setLogin", String.class).invoke(user, "john");
        type.getMethod("setNickname", String.class).invoke(user, "jj");
        type.getMethod("setPassword", String.class).invoke(user, "secret-password");
        type.getMethod("setToken", UUID.class).invoke(user, UUID.randomUUID());
        type.getMethod("setAge", Integer.class).invoke(user, 42);
        Class<?> statusType = classLoader.loadClass(PACKAGE + ".common.MaskedUserStatus");
        type.getMethod("setStatus", statusType).invoke(user, statusType.getEnumConstants()[0]);
        type.getMethod("setSecretNote", String.class).invoke(user, "secret-note");
        return user;
    }

    private Object newMaskedEvent(ClassLoader classLoader) throws Exception {
        Class<?> type = classLoader.loadClass(PACKAGE + ".messages.MaskedEvent");
        Object event = type.getConstructor().newInstance();
        type.getMethod("setEventId", String.class).invoke(event, "event-1");
        type.getMethod("setBody", String.class).invoke(event, "secret body");
        type.getMethod("setPhones", List.class).invoke(event, List.of("+10000000000"));
        return event;
    }

    private void generate(boolean useLombok) throws IOException {
        generate(SPEC, useLombok);
    }

    private void generate(String specName, boolean useLombok) throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName(specName);
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory(outputDir.toString());
        spec.setPackageLocation(PACKAGE);

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(useLombok, false, new Accessors(false, false, false)));
        ctx.setValidationApi(ValidationApi.JAKARTA);
        new YojoGenerator().generateAll(ctx);
    }

    /**
     * Compiles the generated sources (Lombok runs as an annotation processor from the test classpath)
     * and returns a class loader for the compiled classes.
     */
    private ClassLoader compile() throws IOException {
        List<Path> javaFiles;
        try (Stream<Path> walk = Files.walk(outputDir)) {
            javaFiles = walk.filter(p -> p.toString().endsWith(".java")).toList();
        }
        assertThat(javaFiles).isNotEmpty();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromPaths(javaFiles);
        List<String> options = List.of(
                "-d", classesDir.toString(),
                "-classpath", System.getProperty("java.class.path"));

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        if (!task.call()) {
            StringBuilder sb = new StringBuilder("COMPILATION FAILED:\n");
            diagnostics.getDiagnostics().forEach(d -> sb.append(String.format("[%s] %s:%d: %s%n",
                    d.getKind(),
                    d.getSource() != null ? d.getSource().getName() : "unknown",
                    d.getLineNumber(),
                    d.getMessage(null))));
            fail(sb.toString());
        }
        return new URLClassLoader(new URL[]{classesDir.toUri().toURL()}, getClass().getClassLoader());
    }

    private String read(String relativePath) throws IOException {
        return Files.readString(outputDir.resolve(relativePath));
    }
}
