package ru.yojo.codegen.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.lang.System.lineSeparator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yojo.codegen.constants.Dictionary.LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION;
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

        String expected = expectedResult.trim() +
                        lineSeparator();

        MapperUtil.buildLombokAnnotations(lombokProperties, new HashSet<>(), actualResultStringBuilder);

        assertThat(actualResultStringBuilder.toString()).contains(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "true, true, true, @NoArgsConstructor @AllArgsConstructor",
            "true, true, false, @AllArgsConstructor",
            "true, false, true, @NoArgsConstructor",
            "true, false, false, "
    })
    void buildLombokAnnotationsTestEnum(boolean enableLombok, boolean allArgsEnabled, boolean noArgsEnabled, String expectedResult) {
        LombokProperties lombokProperties = new LombokProperties(enableLombok, allArgsEnabled, noArgsEnabled, new Accessors(false, false, false));

        StringBuilder actualResultStringBuilder = new StringBuilder();

        String trim = expectedResult != null ? expectedResult.trim() : "";
        List<String> expected = List.of("");
        if (!trim.isEmpty()) {
            expected = Arrays.asList(trim.split(" "));
        }
        MapperUtil.buildLombokAnnotations(lombokProperties, new HashSet<>(), actualResultStringBuilder);
        List<String> actual = Arrays.asList(actualResultStringBuilder.toString().split(lineSeparator()));
        assertTrue(actual.containsAll(expected));
        assertTrue(expected.containsAll(actual));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void buildLombokAnnotationsTestAccessorsDisable() {
        Accessors accessors = new Accessors(false, true, true);
        LombokProperties lombokProperties = new LombokProperties(true, false, true, accessors);

        StringBuilder actualResultStringBuilder = new StringBuilder();
        String expected = LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION + lineSeparator();
        MapperUtil.buildLombokAnnotations(lombokProperties, new HashSet<>(), actualResultStringBuilder);

        assertThat(actualResultStringBuilder.toString()).hasToString(expected);
    }

    @Test
    void buildLombokAnnotationsTestAccessorsDisableWithAllArgsEnabled() {
        Accessors accessors = new Accessors(false, true, true);
        LombokProperties lombokProperties = new LombokProperties(true, true, false, accessors);

        StringBuilder actualResultStringBuilder = new StringBuilder();
        String expected = LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION + lineSeparator();
        MapperUtil.buildLombokAnnotations(lombokProperties, new HashSet<>(), actualResultStringBuilder);

        assertThat(actualResultStringBuilder.toString()).hasToString(expected);
    }
}
