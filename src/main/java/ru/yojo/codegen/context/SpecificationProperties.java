package ru.yojo.codegen.context;

public class SpecificationProperties {
    private String specName;
    private String inputDirectory;
    private String outputDirectory;
    private String packageLocation;

    public SpecificationProperties() {}

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getInputDirectory() {
        return inputDirectory;
    }

    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
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
}