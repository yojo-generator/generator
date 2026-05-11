package ru.yojo.codegen.domain.message;

import org.junit.jupiter.api.Test;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link MessageBuilder}.
 * <p>
 * Verifies that {@link MessageBuilder#build()} correctly transfers all fields
 * and validates required properties.
 */
class MessageBuilderTest {

    private static final String MESSAGE_NAME = "UserSignedUp";
    private static final String MESSAGE_PACKAGE = "com.example.api.messages;";

    // ——— Minimum valid build ——— //

    @Test
    void buildWithOnlyRequiredFields() {
        Message message = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .build();

        assertThat(message.getMessageName()).isEqualTo(MESSAGE_NAME);
        assertThat(message.getMessagePackageName()).isEqualTo(MESSAGE_PACKAGE);
        assertThat(message.getSummary()).isNull();
        assertThat(message.getFillParameters()).isNull();
        assertThat(message.getLombokProperties()).isNull();
        assertThat(message.getExtendsFrom()).isNull();
        assertThat(message.getImplementsFrom()).isEmpty();
        assertThat(message.getImportSet()).isEmpty();
        assertThat(message.getClassAnnotations()).isEmpty();
        assertThat(message.getPathForGenerateMessage()).isNull();
    }

    // ——— All fields populated ——— //

    @Test
    void buildWithAllFields() {
        LombokProperties lombok = new LombokProperties(
                true, true, new Accessors(true, true, true));
        FillParameters fill = new FillParameters();

        Message message = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .summary("Test summary")
                .fillParameters(fill)
                .commonPackageName("com.example.api.common;")
                .lombokProperties(lombok)
                .extendsFrom("BaseMessage")
                .addImplementsFrom("Serializable")
                .addImplementsFrom("Comparable")
                .addImport("java.util.List;")
                .addImport("java.util.Map;")
                .addClassAnnotation("@com.example.MyAnnotation")
                .addClassAnnotation("@com.example.Another")
                .pathForGenerateMessage("io.github.events")
                .build();

        assertThat(message.getMessageName()).isEqualTo(MESSAGE_NAME);
        assertThat(message.getMessagePackageName()).isEqualTo(MESSAGE_PACKAGE);
        assertThat(message.getSummary()).isEqualTo("Test summary");
        assertThat(message.getFillParameters()).isSameAs(fill);
        assertThat(message.getLombokProperties()).isSameAs(lombok);
        assertThat(message.getExtendsFrom()).isEqualTo("BaseMessage");
        assertThat(message.getImplementsFrom()).containsExactlyInAnyOrder("Serializable", "Comparable");
        assertThat(message.getImportSet()).containsExactlyInAnyOrder("java.util.List;", "java.util.Map;");
        assertThat(message.getClassAnnotations())
                .containsExactlyInAnyOrder("@com.example.MyAnnotation", "@com.example.Another");
        assertThat(message.getPathForGenerateMessage()).isEqualTo("io.github.events");
    }

    // ——— ImportSet transfer (regression guard) ——— //

    @Test
    void importSetIsTransferredToMessage() {
        Message message = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .addImport("com.example.SomeType;")
                .build();

        assertThat(message.getImportSet())
                .as("importSet must be transferred from builder to message in build()")
                .containsExactly("com.example.SomeType;");
    }

    @Test
    void multipleImportsAccumulate() {
        Message message = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .addImport("a;")
                .addImport("b;")
                .build();

        assertThat(message.getImportSet()).hasSize(2);
    }

    @Test
    void emptyImportSetYieldsEmptySetOnMessage() {
        Message message = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .build();

        assertThat(message.getImportSet()).isEmpty();
    }

    // ——— Empty collections ——— //

    @Test
    void emptyCollectionsDoNotCauseIssues() {
        Message message = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .build();

        assertThat(message.getImplementsFrom()).isEmpty();
        assertThat(message.getImportSet()).isEmpty();
        assertThat(message.getClassAnnotations()).isEmpty();
    }

    // ——— extends/implements helpers ——— //

    @Test
    void hasExtendsFromReturnsFalseWhenNotSet() {
        MessageBuilder builder = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE);

        assertThat(builder.hasExtendsFrom()).isFalse();
        assertThat(builder.getExtendsFrom()).isNull();
    }

    @Test
    void hasExtendsFromReturnsTrueWhenSet() {
        MessageBuilder builder = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .extendsFrom("BaseMessage");

        assertThat(builder.hasExtendsFrom()).isTrue();
        assertThat(builder.getExtendsFrom()).isEqualTo("BaseMessage");
    }

    @Test
    void hasImplementsFromReturnsFalseWhenNotSet() {
        MessageBuilder builder = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE);

        assertThat(builder.hasImplementsFrom()).isFalse();
        assertThat(builder.getImplementsFrom()).isEmpty();
    }

    @Test
    void hasImplementsFromReturnsTrueAfterAdding() {
        MessageBuilder builder = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .addImplementsFrom("Serializable");

        assertThat(builder.hasImplementsFrom()).isTrue();
        assertThat(builder.getImplementsFrom()).containsExactly("Serializable");
    }

    // ——— Custom path ——— //

    @Test
    void customPathIsSetAndTransferred() {
        Message message = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .pathForGenerateMessage("custom.path")
                .build();

        assertThat(message.getPathForGenerateMessage()).isEqualTo("custom.path");
    }

    // ——— Validation: required fields ——— //

    @Test
    void buildWithoutNameThrows() {
        assertThatThrownBy(() -> new MessageBuilder()
                .messagePackageName(MESSAGE_PACKAGE)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messageName");
    }

    @Test
    void buildWithoutPackageNameThrows() {
        assertThatThrownBy(() -> new MessageBuilder()
                .name(MESSAGE_NAME)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messagePackageName");
    }

    @Test
    void buildWithBlankNameThrows() {
        assertThatThrownBy(() -> new MessageBuilder()
                .name("   ")
                .messagePackageName(MESSAGE_PACKAGE)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messageName");
    }

    @Test
    void buildWithBlankPackageNameThrows() {
        assertThatThrownBy(() -> new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName("   ")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messagePackageName");
    }

    // ——— Fluent API returns this ——— //

    @Test
    void fluentApiReturnsBuilder() {
        MessageBuilder builder = new MessageBuilder();

        assertThat(builder.name("X")).isSameAs(builder);
        assertThat(builder.messagePackageName("p;")).isSameAs(builder);
        assertThat(builder.summary("s")).isSameAs(builder);
        assertThat(builder.commonPackageName("c;")).isSameAs(builder);
        assertThat(builder.extendsFrom("E")).isSameAs(builder);
        assertThat(builder.addImplementsFrom("I")).isSameAs(builder);
        assertThat(builder.addImport("i;")).isSameAs(builder);
        assertThat(builder.addClassAnnotation("@A")).isSameAs(builder);
        assertThat(builder.pathForGenerateMessage("p")).isSameAs(builder);
    }

    // ——— static builder() method ——— //

    @Test
    void staticBuilderMethodReturnsNewBuilder() {
        assertThat(Message.builder()).isNotNull().isInstanceOf(MessageBuilder.class);
    }

    @Test
    void staticBuilderMethodReturnsFreshBuilderEachTime() {
        assertThat(Message.builder()).isNotSameAs(Message.builder());
    }

    // ——— Built messages are independent ——— //

    @Test
    void builtMessagesAreIndependent() {
        MessageBuilder builder = new MessageBuilder()
                .name(MESSAGE_NAME)
                .messagePackageName(MESSAGE_PACKAGE)
                .addImport("shared.Import;");

        Message first = builder.build();
        Message second = builder.build();
        first.getImportSet().add("first.Only;");

        assertThat(second.getImportSet())
                .as("mutating one message must not affect another")
                .containsExactly("shared.Import;");
    }
}
