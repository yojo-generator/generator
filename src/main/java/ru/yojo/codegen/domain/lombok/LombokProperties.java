package ru.yojo.codegen.domain.lombok;

import java.util.Map;
import java.util.Set;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

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
                lombokProperties.noArgsConstructor,
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
     * Builds Lombok annotations based on this configuration and appends them to the builder.
     * Also adds required imports to the imports set.
     *
     * @param requiredImports         set to collect imports
     * @param lombokAnnotationBuilder builder to append annotations to
     */
    public void buildLombokAnnotations(Set<String> requiredImports, StringBuilder lombokAnnotationBuilder) {
        if (noArgsConstructor()) {
            lombokAnnotationBuilder
                    .append(LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION)
                    .append(System.lineSeparator());
            requiredImports.add(LOMBOK_NO_ARGS_CONSTRUCTOR_IMPORT);
        }
        if (accessors != null && accessors.isEnable()) {
            String accessorsAnnotation = fetchAccessorsAnnotation();
            lombokAnnotationBuilder.append(accessorsAnnotation)
                    .append(System.lineSeparator());
            requiredImports.add(LOMBOK_ACCESSORS_IMPORT);
        }
        if (allArgsConstructor()) {
            lombokAnnotationBuilder.append(LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION)
                    .append(System.lineSeparator());
            requiredImports.add(LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT);
        }
        if (equalsAndHashCode != null && equalsAndHashCode.isEnable()) {
            requiredImports.add(LOMBOK_EQUALS_AND_HASH_CODE_IMPORT);
            if (Boolean.TRUE.equals(equalsAndHashCode.getCallSuper())) {
                lombokAnnotationBuilder.append(EQUALS_AND_HASH_CODE_CALL_SUPER_TRUE_ANNOTATION)
                        .append(System.lineSeparator());
            } else if (Boolean.FALSE.equals(equalsAndHashCode.getCallSuper())) {
                lombokAnnotationBuilder.append(EQUALS_AND_HASH_CODE_CALL_SUPER_FALSE_ANNOTATION)
                        .append(System.lineSeparator());
            } else {
                lombokAnnotationBuilder.append(EQUALS_AND_HASH_CODE_ANNOTATION)
                        .append(System.lineSeparator());
            }
        }
    }

    private String fetchAccessorsAnnotation() {
        if (accessors == null) {
            return LOMBOK_ACCESSORS_EMPTY_ANNOTATION;
        }
        boolean fluent = accessors.isFluent();
        boolean chain = accessors.isChain();

        if (fluent && chain) {
            return String.format(LOMBOK_ACCESSORS_ANNOTATION, "fluent = true, chain = true", "");
        } else if (fluent) {
            return String.format(LOMBOK_ACCESSORS_ANNOTATION, "fluent = true", "");
        } else if (chain) {
            return String.format(LOMBOK_ACCESSORS_ANNOTATION, "chain = true", "");
        }
        return LOMBOK_ACCESSORS_EMPTY_ANNOTATION;
    }

    /**
     * Populates Lombok configuration from a YAML map (the {@code lombok: {...}} section).
     * <p>
     * Reads:
     * <ul>
     *   <li>{@code enable} - global Lombok toggle</li>
     *   <li>{@code accessors} - {@code @Accessors} config</li>
     *   <li>{@code equalsAndHashCode} - {@code @EqualsAndHashCode} config</li>
     *   <li>{@code allArgsConstructor} - full-args constructor flag</li>
     *   <li>{@code noArgsConstructor} - no-args constructor flag</li>
     * </ul>
     *
     * @param lombokProps the {@code lombok} map from YAML (may be {@code null})
     */
    public void populateFromMap(Map<String, Object> lombokProps) {
        if (lombokProps == null) {
            return;
        }

        // Check if Lombok is explicitly disabled
        if (lombokProps.containsKey(ENABLE)) {
            String enableValue = getStringValueIfExistOrElseNull(ENABLE, lombokProps);
            if ("false".equals(enableValue)) {
                this.enableLombok = false;
                return; // Don't process other props if explicitly disabled
            }
        }

        // Populate accessors
        if (lombokProps.containsKey(ACCESSORS)) {
            Accessors acc = new Accessors(true, false, false);
            Map<String, Object> accessorsMap = castObjectToMap(lombokProps.get(ACCESSORS));
            if (accessorsMap.containsKey(FLUENT)) {
                acc.setFluent(Boolean.valueOf(accessorsMap.get(FLUENT).toString()));
            }
            if (accessorsMap.containsKey(CHAIN)) {
                acc.setChain(Boolean.valueOf(accessorsMap.get(CHAIN).toString()));
            }
            if (accessorsMap.containsKey(ENABLE)) {
                acc.setEnable(Boolean.valueOf(accessorsMap.get(ENABLE).toString()));
            }
            this.accessors = acc;
        }

        // Populate equalsAndHashCode
        if (lombokProps.containsKey(EQUALS_AND_HASH_CODE)) {
            EqualsAndHashCode eah = new EqualsAndHashCode();
            Map<String, Object> eahMap = castObjectToMap(lombokProps.get(EQUALS_AND_HASH_CODE));
            if (eahMap != null) {
                if (getStringValueIfExistOrElseNull(CALL_SUPER, eahMap) != null) {
                    eah.setCallSuper(Boolean.valueOf(getStringValueIfExistOrElseNull(CALL_SUPER, eahMap)));
                }
                eah.setEnable(true);
            }
            this.equalsAndHashCode = eah;
        }

        // Populate constructor flags
        if (lombokProps.containsKey(ALL_ARGS) || lombokProps.containsKey(NO_ARGS)) {
            String allArgs = getStringValueIfExistOrElseNull(ALL_ARGS, lombokProps);
            String noArgs = getStringValueIfExistOrElseNull(NO_ARGS, lombokProps);
            if (allArgs != null) {
                this.allArgsConstructor = Boolean.valueOf(allArgs);
            }
            if (noArgs != null) {
                this.noArgsConstructor = Boolean.valueOf(noArgs);
            }
        }
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