package ru.yojo.codegen;

import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.generator.YojoGenerator;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import static ru.yojo.codegen.util.MapperUtil.isTrue;

public class YojoCLI {
    public static void main(String[] args) {
        if (args.length != 0) {
            YojoGenerator yojoGenerator = new YojoGenerator(new SchemaMapper(), new MessageMapper());
            boolean lombokEnabled = isTrue(Boolean.valueOf(args[2]));
            boolean allArgsConstr = false;
            boolean accessors = false;
            if (lombokEnabled) {
                allArgsConstr = isTrue(Boolean.valueOf(args.length > 3 ? args[3] : "false"));
                accessors = isTrue(Boolean.valueOf(args.length > 4 ? args[4] : "false"));
            }
            yojoGenerator.generate(args[0], "C:/Users/VMorozkin/IdeaProjects/github/yojo-generator/build/generated-sources/ru/yojo/codegen", "ru.yojo.codegen", new LombokProperties(lombokEnabled, allArgsConstr, accessors));
        } else {
            throw new RuntimeException("Arguments not found: Expected arguments: filePath, outputDirectory, lombokProperties - 3 boolean values(enable, allArgsConstr, accessors");
        }
    }
}
