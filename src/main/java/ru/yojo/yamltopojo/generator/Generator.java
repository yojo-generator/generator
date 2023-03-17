package ru.yojo.yamltopojo.generator;

import ru.yojo.yamltopojo.domain.LombokProperties;

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
     */
    void generate(String filePath, String outputDirectory, LombokProperties lombokProperties);
}
