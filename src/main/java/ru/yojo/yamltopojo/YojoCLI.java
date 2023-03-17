package ru.yojo.yamltopojo;

import ru.yojo.yamltopojo.domain.LombokProperties;
import ru.yojo.yamltopojo.generator.YojoGenerator;
import ru.yojo.yamltopojo.mapper.SchemaMapper;

public class YojoCLI {
    public static void main(String[] args) {
        YojoGenerator yojoGenerator = new YojoGenerator(new SchemaMapper());

        yojoGenerator.generate(args[0], args[1], new LombokProperties(Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4])));
    }
}
