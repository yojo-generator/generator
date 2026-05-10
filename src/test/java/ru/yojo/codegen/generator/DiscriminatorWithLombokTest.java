package ru.yojo.codegen.generator;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.generator.base.GenerationComparisonTestBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test discriminator.yaml with Lombok.
 */
public class DiscriminatorWithLombokTest extends GenerationComparisonTestBase {

    @Override
    protected boolean useLombok() {
        return true;
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
    void testDiscriminatorWithLombok() throws IOException {
        generateAndCompare();
    }

    @Test
    void testJsonTypeIdAnnotationPresent() throws IOException {
        // Get expected output directory
        Path expectedDir = Paths.get(getExpectedOutputDirectory());
        
        // Check that Pet.java has @JsonTypeId on petType field (BASE class - Variant 1)
        Path petPath = expectedDir.resolve("common/Pet.java");
        String petContent = Files.readString(petPath);
        assertTrue(petContent.contains("@JsonTypeId"), "Pet.java should have @JsonTypeId annotation (base class)");
        assertTrue(petContent.contains("import com.fasterxml.jackson.annotation.JsonTypeId"),
                "Pet.java should import JsonTypeId");
        
        // Check that Cat.java does NOT have @JsonTypeId (inherited from Pet)
        Path catPath = expectedDir.resolve("common/Cat.java");
        String catContent = Files.readString(catPath);
        assertFalse(catContent.contains("@JsonTypeId"), "Cat.java should NOT have @JsonTypeId annotation (inherited from Pet)");
        
        // Check that Dog.java does NOT have @JsonTypeId (inherited from Pet)
        Path dogPath = expectedDir.resolve("common/Dog.java");
        String dogContent = Files.readString(dogPath);
        assertFalse(dogContent.contains("@JsonTypeId"), "Dog.java should NOT have @JsonTypeId annotation (inherited from Pet)");
    }
}
