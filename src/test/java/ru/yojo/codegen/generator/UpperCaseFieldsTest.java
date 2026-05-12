package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.ValidationApi;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test that uppercase field names in YAML contract are preserved as-is in generated Java code.
 * <p>
 * Expected behavior:
 * <pre>
 *   "IIN"       → "IIN"       (ALL CAPS preserved)
 *   "IPAddress" → "IPAddress" (PascalCase preserved)
 *   "KPP"       → "KPP"       (ALL CAPS preserved)
 *   "StatusCode" → "StatusCode" (PascalCase preserved)
 *   "URL"       → "URL"       (ALL CAPS preserved)
 *   "firstName" → "firstName" (camelCase preserved)
 *   "user-name" → "userName"  (kebab-case → camelCase)
 * </pre>
 */
class UpperCaseFieldsTest {

    @TempDir
    Path tempOutputDir;

    private void generate() throws IOException {
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName("upperCaseFields.yaml");
        spec.setInputDirectory("src/test/resources/example/contract");
        spec.setOutputDirectory(tempOutputDir.toString());
        spec.setPackageLocation("example.upperCaseFields");

        YojoContext ctx = new YojoContext();
        ctx.setSpecificationProperties(Collections.singletonList(spec));
        ctx.setLombokProperties(new LombokProperties(false, false, new Accessors(false, false, false)));
        ctx.setValidationApi(ValidationApi.JAKARTA);

        new YojoGenerator().generateAll(ctx);
    }

    private String readGenerated(String relativePath) throws IOException {
        Path path = tempOutputDir.resolve(relativePath);
        assertTrue(Files.exists(path), "File not found: " + path);
        return Files.readString(path);
    }

    @Test
    void upperCaseFieldNamesPreservedAsIs() throws IOException {
        generate();

        String schema = readGenerated("common/UpperCaseSchema.java");

        // ALL CAPS field names preserved as-is
        assertThat(schema)
                .contains("private String IIN;")
                .contains("private String KPP;")
                .contains("private String URL;");

        // PascalCase field names preserved as-is
        assertThat(schema)
                .contains("private String IPAddress;")
                .contains("private Integer StatusCode;");

        // camelCase field names preserved as-is
        assertThat(schema).contains("private String firstName;");

        // kebab-case → camelCase conversion
        assertThat(schema).contains("private String userName;");
    }

    @Test
    void upperCaseGettersAndSetters() throws IOException {
        generate();

        String schema = readGenerated("common/UpperCaseSchema.java");

        // Getters for ALL CAPS fields
        assertThat(schema)
                .contains("public String getIIN()")
                .contains("public String getKPP()")
                .contains("public String getURL()");

        // Getters for PascalCase fields
        assertThat(schema)
                .contains("public String getIPAddress()")
                .contains("public Integer getStatusCode()");

        // Getters for camelCase fields
        assertThat(schema).contains("public String getFirstName()");

        // Setters for ALL CAPS fields
        assertThat(schema)
                .contains("public void setIIN(String IIN)")
                .contains("public void setKPP(String KPP)")
                .contains("public void setURL(String URL)");

        // Setters for PascalCase fields
        assertThat(schema)
                .contains("public void setIPAddress(String IPAddress)")
                .contains("public void setStatusCode(Integer StatusCode)");
    }
}
