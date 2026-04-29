package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test discriminator.yaml without Lombok.
 */
public class DiscriminatorWithoutLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return false;
    }

    @Override
    protected String getSpecName() {
        return "discriminator.yaml";
    }

    @Override
    protected String getPackageLocation() {
        return "discriminator";
    }

    @Test
    void testDiscriminatorWithoutLombok() throws IOException {
        generateAndCompare();
    }

    @Test
    void testJsonTypeIdAnnotationPresent() throws IOException {
        // Get expected output directory
        Path expectedDir = Path.of(getExpectedOutputDirectory());
        
        // Check that Cat.java has @JsonTypeId on petType field
        Path catPath = expectedDir.resolve("common/Cat.java");
        String catContent = Files.readString(catPath);
        assert catContent.contains("@JsonTypeId") : "Cat.java should have @JsonTypeId annotation";
        assert catContent.contains("import com.fasterxml.jackson.annotation.JsonTypeId") : 
            "Cat.java should import JsonTypeId";

        // Check that Dog.java has @JsonTypeId on petType field
        Path dogPath = expectedDir.resolve("common/Dog.java");
        String dogContent = Files.readString(dogPath);
        assert dogContent.contains("@JsonTypeId") : "Dog.java should have @JsonTypeId annotation";
        assert dogContent.contains("import com.fasterxml.jackson.annotation.JsonTypeId") : 
            "Dog.java should import JsonTypeId";

        // Check that Pet.java does NOT have @JsonTypeId
        Path petPath = expectedDir.resolve("common/Pet.java");
        String petContent = Files.readString(petPath);
        assert !petContent.contains("@JsonTypeId") : "Pet.java should NOT have @JsonTypeId annotation";
    }
}
