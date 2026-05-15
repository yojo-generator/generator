package ru.yojo.codegen.context;

import ru.yojo.codegen.domain.lombok.LombokProperties;

/**
 * Holds configuration for a single AsyncAPI specification to be processed.
 * Used by {@link ru.yojo.codegen.generator.YojoGenerator} in multi-spec scenarios.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class SpecificationProperties {

    /**
     * Filename of the specification (e.g., {@code "test.yaml"}).
     */
    private String specName;

    /**
     * Absolute or relative path to the directory containing the specification file.
     * Used as the base for resolving relative {@code $ref} paths.
     */
    private String inputDirectory;

    /**
     * Target output directory where generated Java files will be written.
     * Subdirectories {@code messages/} and {@code common/} will be created under it.
     */
    private String outputDirectory;

    /**
     * Base Java package name (e.g., {@code "com.example.api"}).
     * Generated classes go into {@code <packageLocation>.messages} and {@code <packageLocation>.common}.
     */
    private String packageLocation;

    private boolean splitModels = true;

    /**
     * Per-spec Lombok configuration (full override of global Lombok when set).
     * When non-null, this completely replaces the global Lombok configuration
     * for this specification only. Fine-grained per-schema overrides remain
     * available via YAML {@code x-lombok}.
     */
    private LombokProperties lombokProperties;

    /**
     * Constructs an empty specification properties instance.
     */
    public SpecificationProperties() {
    }

    /**
     * Returns the specification filename (e.g., {@code "test.yaml"}).
     *
     * @return specification name
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * Sets the specification filename.
     *
     * @param specName filename, e.g., {@code "test.yaml"}
     */
    public void setSpecName(String specName) {
        this.specName = specName;
    }

    /**
     * Returns the input directory path (where the YAML file resides).
     *
     * @return input directory path
     */
    public String getInputDirectory() {
        return inputDirectory;
    }

    /**
     * Sets the input directory path.
     *
     * @param inputDirectory path to input directory
     */
    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    /**
     * Returns the output directory path (where Java files will be generated).
     *
     * @return output directory path
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * Sets the output directory path.
     *
     * @param outputDirectory path to output directory
     */
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Returns the base Java package location.
     *
     * @return base package, e.g., {@code "com.example.api"}
     */
    public String getPackageLocation() {
        return packageLocation;
    }

    /**
     * Sets the base Java package location.
     *
     * @param packageLocation package name (without trailing dot or subpackages)
     */
    public void setPackageLocation(String packageLocation) {
        this.packageLocation = packageLocation;
    }

    /**
     * Returns whether split-model mode is enabled.
     *
     * @return true if DTOs should be split into a separate common package
     */
    public boolean isSplitModels() {
        return splitModels;
    }

    /**
     * Enables or disables split-model mode.
     *
     * @param splitModels split mode flag
     */
    public void setSplitModels(boolean splitModels) {
        this.splitModels = splitModels;
    }

    /**
     * Returns the per-spec Lombok configuration, if set.
     *
     * @return Lombok config or {@code null} to use global configuration
     */
    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

    /**
     * Sets the per-spec Lombok configuration.
     * When set, this fully overrides the global Lombok configuration for this specification.
     *
     * @param lombokProperties Lombok config (may be {@code null} to fall back to global)
     */
    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }
}