package ru.yojo.codegen.generator;

import ru.yojo.codegen.domain.lombok.LombokProperties;

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
}
