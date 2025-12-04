package ru.yojo.codegen.context;

import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.List;

/**
 * Top-level context for multi-specification code generation runs.
 * Holds shared configuration (e.g., Lombok, Spring Boot version) and a list of individual specs to process.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class YojoContext {

    /**
     * Base input directory (deprecated/legacy — prefer {@link SpecificationProperties#getInputDirectory()}).
     */
    private String directory;

    /**
     * Default output directory (deprecated/legacy — prefer {@link SpecificationProperties#getOutputDirectory()}).
     */
    private String outputDirectory;

    /**
     * Default base package (deprecated/legacy — prefer {@link SpecificationProperties#getPackageLocation()}).
     */
    private String packageLocation;

    /**
     * Global Lombok configuration applied to all generated schemas/messages unless overridden per-spec.
     */
    private LombokProperties lombokProperties;

    /**
     * Spring Boot version (e.g., {@code "3.x.x"}) used to select jakarta vs javax validation imports.
     */
    private String springBootVersion;

    /**
     * List of specification definitions to process (main entry point for multi-file generation).
     */
    private List<SpecificationProperties> specificationProperties;

    /**
     * Returns the legacy base input directory (if used).
     *
     * @return directory path or {@code null}
     * @deprecated use {@link SpecificationProperties#getInputDirectory()} instead
     */
    @Deprecated
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets the legacy base input directory.
     *
     * @param directory input path
     * @deprecated
     */
    @Deprecated
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Returns the default output directory (if used).
     *
     * @return output path or {@code null}
     * @deprecated use {@link SpecificationProperties#getOutputDirectory()} instead
     */
    @Deprecated
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * Sets the default output directory.
     *
     * @param outputDirectory output path
     * @deprecated
     */
    @Deprecated
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Returns the default base package (if used).
     *
     * @return package name or {@code null}
     * @deprecated use {@link SpecificationProperties#getPackageLocation()} instead
     */
    @Deprecated
    public String getPackageLocation() {
        return packageLocation;
    }

    /**
     * Sets the default base package.
     *
     * @param packageLocation base package
     * @deprecated
     */
    @Deprecated
    public void setPackageLocation(String packageLocation) {
        this.packageLocation = packageLocation;
    }

    /**
     * Returns the global Lombok configuration.
     *
     * @return lombok settings or {@code null} if not set
     */
    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

    /**
     * Sets the global Lombok configuration.
     *
     * @param lombokProperties lombok config to apply by default
     */
    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    /**
     * Returns the Spring Boot version string.
     *
     * @return version (e.g., {@code "2.7.0"} or {@code "3.x.x"})
     */
    public String getSpringBootVersion() {
        return springBootVersion;
    }

    /**
     * Sets the Spring Boot version (used to resolve validation annotation imports).
     *
     * @param springBootVersion version string
     */
    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }

    /**
     * Returns the list of specification definitions to be processed.
     *
     * @return list of specs (must be non-empty for generation)
     */
    public List<SpecificationProperties> getSpecificationProperties() {
        return specificationProperties;
    }

    /**
     * Sets the list of specification definitions.
     *
     * @param specificationProperties list of specs (each describes one AsyncAPI file)
     */
    public void setSpecificationProperties(List<SpecificationProperties> specificationProperties) {
        this.specificationProperties = specificationProperties;
    }
}