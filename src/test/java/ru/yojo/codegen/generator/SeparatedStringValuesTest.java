package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test stringValues.yaml from separated contracts.
 */
public class SeparatedStringValuesTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
    }

    @Override
    protected String getSpecName() {
        return "stringValues.yaml";
    }

    @Override
    protected String getInputDirectory() {
        return "src/test/resources/example/contract/separated";
    }

    @Override
    protected String getPackageLocation() {
        return "separated";
    }

    @Test
    void testStringValues() throws IOException {
        YojoContext context = createYojoContext();
        yojoGenerator.generateAll(context);
        
        Path outputDir = Path.of(tempOutputDir.toString());
        assertThat(outputDir).exists().isDirectory();
        
        // Verify final field with default (x-final + default)
        Path stringValuesPath = outputDir.resolve("common/StringValues.java");
        assertThat(stringValuesPath).exists().isRegularFile();
        String content = Files.readString(stringValuesPath);
        
        // Final field with default: just field + getter, no setter
        assertThat(content).contains("private final String stringFinalValue = \"initial\";");
        assertThat(content).contains("public String getStringFinalValue()");
        assertThat(content).doesNotContain("setStringFinalValue");
        
        // Final field WITHOUT default: field + constructor param + getter, no setter
        assertThat(content).contains("private final String stringFinalWithoutDefault;");
        assertThat(content).contains("public StringValues(String stringFinalWithoutDefault)");
        assertThat(content).contains("this.stringFinalWithoutDefault = stringFinalWithoutDefault");
        assertThat(content).contains("public String getStringFinalWithoutDefault()");
        assertThat(content).doesNotContain("setStringFinalWithoutDefault");
    }
}
