package ru.yojo.codegen.context;

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

    public boolean isSplitModels() {
        return splitModels;
    }

    public void setSplitModels(boolean splitModels) {
        this.splitModels = splitModels;
    }
}