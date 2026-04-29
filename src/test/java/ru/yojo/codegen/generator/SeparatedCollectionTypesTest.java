package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test collectionTypes.yaml from separated contracts.
 */
public class SeparatedCollectionTypesTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
    }

    @Override
    protected String getSpecName() {
        return "collectionTypes.yaml";
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
    void testCollectionTypes() throws IOException {
        YojoContext context = createYojoContext();
        yojoGenerator.generateAll(context);
        
        Path outputDir = Path.of(tempOutputDir.toString());
        assertThat(outputDir).exists().isDirectory();
    }
}
