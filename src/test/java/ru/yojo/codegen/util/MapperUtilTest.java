package ru.yojo.codegen.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.HashSet;

import static java.lang.System.lineSeparator;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.yojo.codegen.constants.Dictionary.LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION;

class MapperUtilTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true, '@Accessors(fluent = true, chain = true)'",
            "true, true, false, '@Accessors(fluent = true)'",
            "true, false, true, '@Accessors(chain = true)'",
            "true, false, false, '@Accessors'"
    })
    void buildLombokAnnotationsTest(boolean isAccessorsEnable, boolean isFluent, boolean isChain, String expectedResult) {
        Accessors accessors = new Accessors(isAccessorsEnable, isFluent, isChain);
        LombokProperties lombokProperties = new LombokProperties(true, false, accessors);

        StringBuilder actualResultStringBuilder = new StringBuilder();

        String expected = LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION +
                lineSeparator() +
                expectedResult.trim() +
                lineSeparator();

        MapperUtil.buildLombokAnnotations(lombokProperties, new HashSet<>(), actualResultStringBuilder);

        assertThat(actualResultStringBuilder.toString()).hasToString(expected);
    }

    @Test
    void buildLombokAnnotationsTestAccessorsDisable() {
        Accessors accessors = new Accessors(false, true, true);
        LombokProperties lombokProperties = new LombokProperties(true, false, accessors);

        StringBuilder actualResultStringBuilder = new StringBuilder();
        String expected = LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION + lineSeparator();
        MapperUtil.buildLombokAnnotations(lombokProperties, new HashSet<>(), actualResultStringBuilder);

        assertThat(actualResultStringBuilder.toString()).hasToString(expected);
    }
}
