package ru.yojo.codegen.generator.support;

/**
 * Mask method for {@code x-masked} tests.
 * <p>
 * Generated test classes call it from {@code toString()}, so it must be on the test classpath.
 * Its mask differs from the default one, so the tests can tell them apart.
 */
public final class TestMasker {

    /**
     * Text that replaces any non-null value.
     */
    public static final String MASK = "<masked>";

    private TestMasker() {
    }

    /**
     * Masks a value.
     *
     * @param value any value
     * @return {@link #MASK}, or {@code "null"} for {@code null}
     */
    public static String mask(Object value) {
        return value == null ? "null" : MASK;
    }
}
