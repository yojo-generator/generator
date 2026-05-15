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
     * Configuration for {@code @Builder} (and related annotations: {@code @Singular}, {@code @Builder.Default}).
     * When Lombok is disabled but builder is enabled, a manual builder class is generated instead.
     */
    private BuilderProperties builder;

    /**
     * Whether {@code @Value} (immutable DTO) should be used instead of {@code @Data}.
     * When enabled: class is annotated with @Value, fields become final, setters are skipped.
     * Mutually exclusive with {@code @Data}.
     */
    private boolean value;

    /**
     * Whether {@code @With} (wither methods for immutable modification) should be generated.
     * Commonly paired with {@code @Value}.
     */
    private boolean with;

    /**
     * Whether {@code @Getter} should be generated on the class (standalone, without @Data).
     * When @Data is already enabled, this is redundant but harmless.
     */
    private boolean getter;

    /**
     * Whether {@code @Setter} should be generated on the class (standalone, without @Data).
     * Ignored when {@code @Value} is enabled (immutable).
     */
    private boolean setter;

    /**
     * Whether {@code @ToString} should be generated on the class (standalone, without @Data).
     * When @Data is already enabled, this is redundant but harmless.
     */
    private boolean toString;

    /**
     * Whether {@code @RequiredArgsConstructor} should be generated.
     * Generates a constructor with parameters for all final / @NonNull fields.
     */
    private boolean requiredArgsConstructor;

    /**
     * Whether {@code @Slf4j} logger field should be generated.
     * Adds: {@code private static final Logger log = LoggerFactory.getLogger(ClassName.class);}
     */
    private boolean slf4j;

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
     *   <li>{@code noArgsConstructor}</li>
     *   <li>{@code accessors} (reference copy)</li>
     *   <li>{@code equalsAndHashCode} (reference copy)</li>
     * </ul>
     *
     * @param lombokProperties source configuration
     * @return new instance with all properties copied
     */
    public static LombokProperties newLombokProperties(LombokProperties lombokProperties) {
        LombokProperties copy = new LombokProperties(
                lombokProperties.enableLombok,
                lombokProperties.allArgsConstructor,
                lombokProperties.noArgsConstructor,
                lombokProperties.getAccessors()
        );
        copy.equalsAndHashCode = lombokProperties.equalsAndHashCode;
        copy.builder = lombokProperties.builder;
        copy.value = lombokProperties.value;
        copy.with = lombokProperties.with;
        copy.getter = lombokProperties.getter;
        copy.setter = lombokProperties.setter;
        copy.toString = lombokProperties.toString;
        copy.requiredArgsConstructor = lombokProperties.requiredArgsConstructor;
        copy.slf4j = lombokProperties.slf4j;
        return copy;
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
     * Returns the {@code @Builder} configuration.
     *
     * @return config or {@code null} if not set
     */
    public BuilderProperties getBuilder() {
        return builder;
    }

    /**
     * Sets the {@code @Builder} configuration.
     *
     * @param builder config (may be {@code null})
     */
    public void setBuilder(BuilderProperties builder) {
        this.builder = builder;
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
     * Constructs a Lombok configuration with explicit values.
     *
     * @param enableLombok       whether Lombok is enabled
     * @param allArgsConstructor whether {@code @AllArgsConstructor} is enabled
     * @param noArgsConstructor whether {@code @NoArgsConstructor} is enabled
     * @param accessors          accessor configuration (may be {@code null})
     */
    public LombokProperties(boolean enableLombok, boolean allArgsConstructor, boolean noArgsConstructor, Accessors accessors) {
        this.enableLombok = enableLombok;
        this.allArgsConstructor = allArgsConstructor;
        this.noArgsConstructor = noArgsConstructor;
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

    // ============================================================
    // New Lombok annotations (4.5.0)
    // ============================================================

    /**
     * Returns whether {@code @Value} (immutable DTO) should replace {@code @Data}.
     *
     * @return {@code true} if @Value mode is active
     */
    public boolean isValue() {
        return value;
    }

    /**
     * Enables or disables {@code @Value} mode.
     *
     * @param value {@code true} to use @Value instead of @Data
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * Returns whether {@code @With} (wither methods) should be generated.
     *
     * @return {@code true} if @With is enabled
     */
    public boolean isWith() {
        return with;
    }

    /**
     * Enables or disables {@code @With}.
     *
     * @param with {@code true} to generate @With
     */
    public void setWith(boolean with) {
        this.with = with;
    }

    /**
     * Returns whether {@code @Getter} should be generated on the class (standalone).
     *
     * @return {@code true} if @Getter is enabled
     */
    public boolean isGetter() {
        return getter;
    }

    /**
     * Enables or disables standalone {@code @Getter}.
     *
     * @param getter {@code true} to generate @Getter
     */
    public void setGetter(boolean getter) {
        this.getter = getter;
    }

    /**
     * Returns whether {@code @Setter} should be generated on the class (standalone).
     *
     * @return {@code true} if @Setter is enabled
     */
    public boolean isSetter() {
        return setter;
    }

    /**
     * Enables or disables standalone {@code @Setter}.
     * Ignored when {@code @Value} is enabled.
     *
     * @param setter {@code true} to generate @Setter
     */
    public void setSetter(boolean setter) {
        this.setter = setter;
    }

    /**
     * Returns whether {@code @ToString} should be generated on the class (standalone).
     *
     * @return {@code true} if @ToString is enabled
     */
    public boolean isToString() {
        return toString;
    }

    /**
     * Enables or disables standalone {@code @ToString}.
     *
     * @param toString {@code true} to generate @ToString
     */
    public void setToString(boolean toString) {
        this.toString = toString;
    }

    /**
     * Returns whether {@code @RequiredArgsConstructor} should be generated.
     *
     * @return {@code true} if @RequiredArgsConstructor is enabled
     */
    public boolean isRequiredArgsConstructor() {
        return requiredArgsConstructor;
    }

    /**
     * Enables or disables {@code @RequiredArgsConstructor}.
     *
     * @param requiredArgsConstructor {@code true} to generate @RequiredArgsConstructor
     */
    public void setRequiredArgsConstructor(boolean requiredArgsConstructor) {
        this.requiredArgsConstructor = requiredArgsConstructor;
    }

    /**
     * Returns whether {@code @Slf4j} logger field should be generated.
     *
     * @return {@code true} if @Slf4j is enabled
     */
    public boolean isSlf4j() {
        return slf4j;
    }

    /**
     * Enables or disables {@code @Slf4j}.
     *
     * @param slf4j {@code true} to generate @Slf4j
     */
    public void setSlf4j(boolean slf4j) {
        this.slf4j = slf4j;
    }
}