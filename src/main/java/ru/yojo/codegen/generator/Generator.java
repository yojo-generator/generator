package ru.yojo.codegen.generator;

import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.io.IOException;

/**
 * Generate pojos from Yaml File
 */
public interface Generator {

    /**
     * Core method of generator
     * Must generate pojos using Yaml File
     *
     * @param filePath         - path to yaml file
     * @param outputDirectory  - output directory for pojos
     * @param lombokProperties - lombok annotation properties
     * @param packageLocation  - location of generated pojo
     */
    void generate(String filePath, String outputDirectory, String packageLocation, LombokProperties lombokProperties);

    /**
     * Method for generating from all yaml files in directory
     *
     * @param directory        - which directory use for search contracts
     * @param outputDirectory  - directory to output generated classes
     * @param packageLocation  - package, which will write in each generated class
     * @param lombokProperties - properies of lombok
     */
    void generateAll(String directory, String outputDirectory, String packageLocation, LombokProperties lombokProperties) throws IOException;
}
