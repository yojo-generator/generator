package ru.yojo.codegen.context;

import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.List;


/**
 * Context for code generation properties
 */
public class YojoContext {
    private String directory;
    private String outputDirectory;
    private String packageLocation;
    private LombokProperties lombokProperties;
    private String springBootVersion;
    private List<SpecificationProperties> specificationProperties;
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getPackageLocation() {
        return packageLocation;
    }

    public void setPackageLocation(String packageLocation) {
        this.packageLocation = packageLocation;
    }

    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    public String getSpringBootVersion() {
        return springBootVersion;
    }

    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }

    public List<SpecificationProperties> getSpecificationProperties() {
        return specificationProperties;
    }

    public void setSpecificationProperties(List<SpecificationProperties> specificationProperties) {
        this.specificationProperties = specificationProperties;
    }

}
