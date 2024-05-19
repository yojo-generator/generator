package ru.yojo.codegen;

import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.generator.YojoGenerator;
import ru.yojo.codegen.mapper.Helper;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import static ru.yojo.codegen.util.MapperUtil.isTrue;

@SuppressWarnings("all")
public class YojoCLI {
    public static void main(String[] args) {
        if (args.length != 0) {
            Helper helper = new Helper();
            SchemaMapper schemaMapper = new SchemaMapper(helper);
            MessageMapper messageMapper = new MessageMapper(helper, schemaMapper);
            YojoGenerator yojoGenerator = new YojoGenerator(schemaMapper, messageMapper);
            boolean lombokEnabled = isTrue(Boolean.valueOf(args[2]));
            boolean allArgsConstr = false;
            boolean accessors = false;
            if (lombokEnabled) {
                allArgsConstr = isTrue(Boolean.valueOf(args.length > 3 ? args[3] : "false"));
                accessors = isTrue(Boolean.valueOf(args.length > 4 ? args[4] : "false"));
            }
            yojoGenerator.generate(args[0],
                    "build/generated-sources/ru/yojo/codegen",
                    "ru.yojo.codegen",
                    new LombokProperties(lombokEnabled, allArgsConstr,
                            new Accessors(true, true, true)));
        } else {
            throw new RuntimeException("Arguments not found: Expected arguments: filePath, outputDirectory, lombokProperties - 3 values(enable, allArgsConstr, accessors)");
        }
    }
}
