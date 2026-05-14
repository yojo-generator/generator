package ru.yojo.codegen.domain.lombok;

/**
 * Represents Lombok's {@code @Builder} configuration.
 * Controls whether a builder pattern is generated and whether
 * {@code @Singular} and {@code @Builder.Default} are applied.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class BuilderProperties {

    /**
     * Whether {@code @Builder} (or manual builder, if Lombok is disabled)
     * should be generated for this class. Default: {@code false}.
     */
    private boolean enable;

    /**
     * Whether {@code @Singular} should be applied to collection fields
     * when {@link #enable} is {@code true}. The singular name is derived
     * automatically (e.g., {@code "items" → "item"}).
     * Default: {@code true} (if builder is enabled).
     */
    private boolean singular = true;

    /**
     * Whether {@code @Builder.Default} should be applied to fields
     * that have a default value when {@link #enable} is {@code true}.
     * Default: {@code true} (if builder is enabled).
     */
    private boolean builderDefault = true;

    /**
     * Constructs a Builder configuration with default values.
     */
    public BuilderProperties() {
    }

    /**
     * Constructs a Builder configuration with explicit values.
     *
     * @param enable         whether to generate builder
     * @param singular       whether to apply {@code @Singular} to collections
     * @param builderDefault whether to apply {@code @Builder.Default} to fields with defaults
     */
    public BuilderProperties(boolean enable, boolean singular, boolean builderDefault) {
        this.enable = enable;
        this.singular = singular;
        this.builderDefault = builderDefault;
    }

    /**
     * Returns whether the builder pattern is enabled.
     *
     * @return {@code true} if builder should be generated
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Enables or disables builder generation.
     *
     * @param enable {@code true} to generate builder
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Returns whether {@code @Singular} should be applied to collection fields.
     *
     * @return {@code true} if singular collection methods should be generated
     */
    public boolean isSingular() {
        return singular;
    }

    /**
     * Enables or disables {@code @Singular} on collection fields.
     *
     * @param singular {@code true} to apply singular collection methods
     */
    public void setSingular(boolean singular) {
        this.singular = singular;
    }

    /**
     * Returns whether {@code @Builder.Default} should be applied to fields with default values.
     *
     * @return {@code true} if builder defaults should be applied
     */
    public boolean isBuilderDefault() {
        return builderDefault;
    }

    /**
     * Enables or disables {@code @Builder.Default} on fields with default values.
     *
     * @param builderDefault {@code true} to apply builder defaults
     */
    public void setBuilderDefault(boolean builderDefault) {
        this.builderDefault = builderDefault;
    }
}
