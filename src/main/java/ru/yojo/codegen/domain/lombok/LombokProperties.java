package ru.yojo.codegen.domain.lombok;

/**
 * Encapsulates Lombok configuration for a generated class.
 * Controls which annotations (e.g., {@code @Data}, {@code @NoArgsConstructor}) are applied.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class LombokProperties {

    /**
     * Whether Lombok is enabled globally for this DTO.
     * If {@code false}, no Lombok annotations are generated.
     */
    private boolean enableLombok;

    /**
     * Whether {@code @AllArgsConstructor} should be generated.
     */
    private boolean allArgsConstructor;

    /**
     * Whether {@code @NoArgsConstructor} should be generated.
     * Default: {@code true}.
     */
    private boolean noArgsConstructor = true;

    /**
     * Configuration for {@code @Accessors} (fluent/chained setters).
     */
    private Accessors accessors;

    /**
     * Configuration for {@code @EqualsAndHashCode}.
     */
    private EqualsAndHashCode equalsAndHashCode;

    /**
     * Constructs an empty Lombok configuration (defaults apply).
     */
    public LombokProperties() {
    }

    /**
     * Creates a shallow copy of the given {@code LombokProperties}, preserving:
     * <ul>
     *   <li>{@code enableLombok}</li>
     *   <li>{@code allArgsConstructor}</li>
     *   <li>{@code accessors} (reference copy)</li>
     * </ul>
     * Note: {@code equalsAndHashCode} and {@code noArgsConstructor} are NOT copied — use with caution.
     *
     * @param lombokProperties source configuration
     * @return new instance with selected properties copied
     */
    public static LombokProperties newLombokProperties(LombokProperties lombokProperties) {
        return new LombokProperties(
                lombokProperties.enableLombok,
                lombokProperties.allArgsConstructor,
                lombokProperties.getAccessors()
        );
    }

    /**
     * Returns the {@code @EqualsAndHashCode} configuration.
     *
     * @return config or {@code null} if not set
     */
    public EqualsAndHashCode getEqualsAndHashCode() {
        return equalsAndHashCode;
    }

    /**
     * Sets the {@code @EqualsAndHashCode} configuration.
     *
     * @param equalsAndHashCode config (may be {@code null})
     */
    public void setEqualsAndHashCode(EqualsAndHashCode equalsAndHashCode) {
        this.equalsAndHashCode = equalsAndHashCode;
    }

    /**
     * Constructs a Lombok configuration with explicit values.
     *
     * @param enableLombok       whether Lombok is enabled
     * @param allArgsConstructor whether {@code @AllArgsConstructor} is enabled
     * @param accessors          accessor configuration (may be {@code null})
     */
    public LombokProperties(boolean enableLombok, boolean allArgsConstructor, Accessors accessors) {
        this.enableLombok = enableLombok;
        this.allArgsConstructor = allArgsConstructor;
        this.accessors = accessors;
    }

    /**
     * Sets the {@code @Accessors} configuration.
     *
     * @param accessors config (may be {@code null})
     */
    public void setAccessors(Accessors accessors) {
        this.accessors = accessors;
    }

    /**
     * Returns the {@code @Accessors} configuration.
     *
     * @return config or {@code null} if not set
     */
    public Accessors getAccessors() {
        return accessors;
    }

    /**
     * Returns whether Lombok is enabled for this DTO.
     *
     * @return {@code true} if Lombok annotations should be generated
     */
    public boolean enableLombok() {
        return enableLombok;
    }

    /**
     * Enables or disables Lombok globally for this DTO.
     *
     * @param enableLombok {@code true} to generate Lombok annotations
     */
    public void setEnableLombok(boolean enableLombok) {
        this.enableLombok = enableLombok;
    }

    /**
     * Returns whether {@code @AllArgsConstructor} should be generated.
     *
     * @return {@code true} if full-args constructor is enabled
     */
    public boolean allArgsConstructor() {
        return allArgsConstructor;
    }

    /**
     * Enables or disables {@code @AllArgsConstructor}.
     *
     * @param allArgsConstructor {@code true} to generate full-args constructor
     */
    public void setAllArgsConstructor(boolean allArgsConstructor) {
        this.allArgsConstructor = allArgsConstructor;
    }

    /**
     * Returns whether {@code @NoArgsConstructor} should be generated.
     *
     * @return {@code true} if no-args constructor is enabled (default: {@code true})
     */
    public boolean noArgsConstructor() {
        return noArgsConstructor;
    }

    /**
     * Enables or disables {@code @NoArgsConstructor}.
     *
     * @param noArgsConstructor {@code true} to generate no-args constructor
     */
    public void setNoArgsConstructor(boolean noArgsConstructor) {
        this.noArgsConstructor = noArgsConstructor;
    }
}