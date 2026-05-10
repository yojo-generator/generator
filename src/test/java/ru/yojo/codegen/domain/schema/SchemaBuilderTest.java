package ru.yojo.codegen.domain.schema;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link SchemaBuilder}.
 * <p>
 * Verifies that {@link SchemaBuilder#build()} correctly transfers all fields
 * and validates required properties.
 */
class SchemaBuilderTest {

    private static final String SCHEMA_NAME = "MyClass";
    private static final String PACKAGE_NAME = "com.example;";

    // ——— Minimum valid build ——— //

    @Test
    void buildWithOnlyRequiredFields() {
        Schema schema = new SchemaBuilder()
                .name(SCHEMA_NAME)
                .packageName(PACKAGE_NAME)
                .build();

        assertThat(schema.getSchemaName()).isEqualTo(SCHEMA_NAME);
        assertThat(schema.getPackageName()).isEqualTo(PACKAGE_NAME);
        assertThat(schema.getDescription()).isNull();
        assertThat(schema.getLombokProperties()).isNull();
        assertThat(schema.getFillParameters()).isNull();
        assertThat(schema.getExtendsFrom()).isNull();
        assertThat(schema.getImplementsFrom()).isEmpty();
        assertThat(schema.getImportSet()).isEmpty();
        assertThat(schema.isInterface()).isFalse();
        assertThat(schema.getMethods()).isEmpty();
        assertThat(schema.getInterfaceImports()).isEmpty();
        assertThat(schema.getClassAnnotations()).isEmpty();
        assertThat(schema.getDiscriminator()).isNull();
        assertThat(schema.getDiscriminatorField()).isNull();
        assertThat(schema.getSubtypes()).isEmpty();
        assertThat(schema.getUniqueSubtypes()).isEmpty();
        assertThat(schema.getSubtypeDiscriminatorValues()).isEmpty();
    }

    // ——— All fields populated ——— //

    @Test
    void buildWithAllFields() {
        LombokProperties lombok = new LombokProperties(
                true, true, new Accessors(true, true, true));
        FillParameters fill = new FillParameters();
        Map<String, Object> methods = Map.of("foo", Map.of("description", "method"));

        Schema schema = new SchemaBuilder()
                .name(SCHEMA_NAME)
                .packageName(PACKAGE_NAME)
                .description("Test description")
                .lombokProperties(lombok)
                .fillParameters(fill)
                .extendsFrom("BaseEntity")
                .addImplementsFrom("Serializable")
                .addImplementsFrom("Comparable")
                .addImport("java.util.List;")
                .addImport("java.util.Map;")
                .isInterface(true)
                .methods(methods)
                .addMethod("bar", Map.of("description", "extra"))
                .interfaceImports(Set.of("java.util.function.Function;"))
                .addClassAnnotation("@com.example.MyAnnotation")
                .addClassAnnotation("@com.example.Another")
                .discriminator("petType")
                .discriminatorField("petType")
                .build();

        assertThat(schema.getSchemaName()).isEqualTo(SCHEMA_NAME);
        assertThat(schema.getPackageName()).isEqualTo(PACKAGE_NAME);
        assertThat(schema.getDescription()).isEqualTo("Test description");
        assertThat(schema.getLombokProperties()).isSameAs(lombok);
        assertThat(schema.getFillParameters()).isSameAs(fill);
        assertThat(schema.getExtendsFrom()).isEqualTo("BaseEntity");
        assertThat(schema.getImplementsFrom()).containsExactlyInAnyOrder("Serializable", "Comparable");
        assertThat(schema.getImportSet()).containsExactlyInAnyOrder("java.util.List;", "java.util.Map;");
        assertThat(schema.isInterface()).isTrue();
        assertThat(schema.getMethods()).containsKeys("foo", "bar");
        assertThat(schema.getInterfaceImports()).containsExactly("java.util.function.Function;");
        assertThat(schema.getClassAnnotations())
                .containsExactlyInAnyOrder("@com.example.MyAnnotation", "@com.example.Another");
        assertThat(schema.getDiscriminator()).isEqualTo("petType");
        assertThat(schema.getDiscriminatorField()).isEqualTo("petType");
    }

    // ——— ImportSet regression guard (the bug we fixed) ——— //

    @Test
    void importSetIsTransferredToSchema() {
        Schema schema = new SchemaBuilder()
                .name(SCHEMA_NAME)
                .packageName(PACKAGE_NAME)
                .addImport("com.example.SomeType;")
                .build();

        assertThat(schema.getImportSet())
                .as("importSet must be transferred from builder to schema in build()")
                .containsExactly("com.example.SomeType;");
    }

    @Test
    void multipleImportsAccumulate() {
        Schema schema = new SchemaBuilder()
                .name(SCHEMA_NAME)
                .packageName(PACKAGE_NAME)
                .addImport("a;")
                .addImport("b;")
                .addImport("c;")
                .build();

        assertThat(schema.getImportSet()).hasSize(3);
    }

    @Test
    void emptyImportSetYieldsEmptySetOnSchema() {
        Schema schema = new SchemaBuilder()
                .name(SCHEMA_NAME)
                .packageName(PACKAGE_NAME)
                .build();

        assertThat(schema.getImportSet()).isEmpty();
    }

    // ——— Empty collections are handled ——— //

    @Test
    void emptyCollectionsDoNotCauseIssues() {
        Schema schema = new SchemaBuilder()
                .name(SCHEMA_NAME)
                .packageName(PACKAGE_NAME)
                .build();

        assertThat(schema.getImplementsFrom()).isEmpty();
        assertThat(schema.getImportSet()).isEmpty();
        assertThat(schema.getMethods()).isEmpty();
        assertThat(schema.getInterfaceImports()).isEmpty();
        assertThat(schema.getClassAnnotations()).isEmpty();
        assertThat(schema.getSubtypes()).isEmpty();
        assertThat(schema.getUniqueSubtypes()).isEmpty();
        assertThat(schema.getSubtypeDiscriminatorValues()).isEmpty();
    }

    // ——— Interface mode ——— //

    @Test
    void interfaceModeWithMethods() {
        Map<String, Object> methodDef = Map.of("definition", "void foo();");

        Schema schema = new SchemaBuilder()
                .name("MyInterface")
                .packageName(PACKAGE_NAME)
                .isInterface(true)
                .addMethod("foo", methodDef)
                .interfaceImports(Set.of("java.util.List;"))
                .build();

        assertThat(schema.isInterface()).isTrue();
        assertThat(schema.getMethods()).containsEntry("foo", methodDef);
        assertThat(schema.getInterfaceImports()).containsExactly("java.util.List;");
    }

    // ——— Subtypes with discriminator values ——— //

    @Test
    void subtypesWithCustomDiscriminatorValues() {
        Schema schema = new SchemaBuilder()
                .name("Pet")
                .packageName(PACKAGE_NAME)
                .discriminator("petType")
                .build();

        schema.addSubtype("Cat", "feline");
        schema.addSubtype("Dog");

        assertThat(schema.getDiscriminator()).isEqualTo("petType");
        assertThat(schema.getSubtypes()).containsExactly("Cat", "Dog");
        assertThat(schema.getUniqueSubtypes()).containsExactlyInAnyOrder("Cat", "Dog");
        assertThat(schema.getSubtypeDiscriminatorValue("Cat")).isEqualTo("feline");
        assertThat(schema.getSubtypeDiscriminatorValue("Dog")).isEqualTo("Dog");
    }

    // ——— Validation: required fields ——— //

    @Test
    void buildWithoutNameThrows() {
        assertThatThrownBy(() -> new SchemaBuilder()
                .packageName(PACKAGE_NAME)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("schemaName");
    }

    @Test
    void buildWithoutPackageNameThrows() {
        assertThatThrownBy(() -> new SchemaBuilder()
                .name(SCHEMA_NAME)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("packageName");
    }

    @Test
    void buildWithBlankNameThrows() {
        assertThatThrownBy(() -> new SchemaBuilder()
                .name("   ")
                .packageName(PACKAGE_NAME)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("schemaName");
    }

    @Test
    void buildWithBlankPackageNameThrows() {
        assertThatThrownBy(() -> new SchemaBuilder()
                .name(SCHEMA_NAME)
                .packageName("   ")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("packageName");
    }

    // ——— Fluent API returns this ——— //

    @Test
    void fluentApiReturnsBuilder() {
        SchemaBuilder builder = new SchemaBuilder();

        assertThat(builder.name("X")).isSameAs(builder);
        assertThat(builder.packageName("p;")).isSameAs(builder);
        assertThat(builder.description("d")).isSameAs(builder);
        assertThat(builder.extendsFrom("E")).isSameAs(builder);
        assertThat(builder.addImplementsFrom("I")).isSameAs(builder);
        assertThat(builder.addImport("i;")).isSameAs(builder);
        assertThat(builder.addClassAnnotation("@A")).isSameAs(builder);
        assertThat(builder.isInterface(true)).isSameAs(builder);
        assertThat(builder.discriminator("d")).isSameAs(builder);
        assertThat(builder.discriminatorField("d")).isSameAs(builder);
    }

    // ——— static builder() method ——— //

    @Test
    void staticBuilderMethodReturnsNewBuilder() {
        assertThat(Schema.builder()).isNotNull().isInstanceOf(SchemaBuilder.class);
    }

    @Test
    void staticBuilderMethodReturnsFreshBuilderEachTime() {
        assertThat(Schema.builder()).isNotSameAs(Schema.builder());
    }

    // ——— Multiple schemas do not share mutable state ——— //

    @Test
    void builtSchemasAreIndependent() {
        SchemaBuilder builder = new SchemaBuilder()
                .name(SCHEMA_NAME)
                .packageName(PACKAGE_NAME)
                .addImport("shared.Import;");

        Schema first = builder.build();
        Schema second = builder.build();
        first.getImportSet().add("first.Only;");

        assertThat(second.getImportSet())
                .as("mutating one schema must not affect another")
                .containsExactly("shared.Import;");
    }
}
