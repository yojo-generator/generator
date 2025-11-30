package ru.yojo.codegen;

import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.generator.YojoGenerator;
import ru.yojo.codegen.mapper.Helper;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.util.Collections;

import static ru.yojo.codegen.util.MapperUtil.isTrue;

@SuppressWarnings("all")
public class YojoCLI {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java -cp ... YojoCLI <inputFileOrDir> <outputDir> <packageLocation> [lombokEnabled=false] [allArgs=false] [accessors=false]");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputDir = args[1];
        String packageLocation = args[2];

        boolean lombokEnabled = args.length > 3 && isTrue(Boolean.valueOf(args[3]));
        boolean allArgs = args.length > 4 && isTrue(Boolean.valueOf(args[4]));
        boolean accessorsEnabled = args.length > 5 && isTrue(Boolean.valueOf(args[5]));

        // --- Настройка контекста ---
        YojoContext context = new YojoContext();

        // Формируем spec: если inputPath — файл, обернём его в папку (т.к. inputDirectory — директория)
        // Для CLI поддержим простой режим: один входной файл → временная папка с ним, или сразу папка
        boolean isFile = !new java.io.File(inputPath).isDirectory();
        String inputDir;
        String specName;
        if (isFile) {
            // Оборачиваем файл в временную директорию
            java.io.File inputFile = new java.io.File(inputPath);
            inputDir = inputFile.getParentFile().getAbsolutePath();
            specName = inputFile.getName().replaceFirst("\\.[^.]+$", "");
        } else {
            inputDir = inputPath;
            specName = "cli-spec";
        }

        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName(specName);
        spec.setInputDirectory(inputDir);
        spec.setOutputDirectory(outputDir);
        spec.setPackageLocation(packageLocation);

        context.setSpecificationProperties(Collections.singletonList(spec));
        context.setSpringBootVersion("3.2.0"); // или передавать как аргумент
        context.setLombokProperties(new LombokProperties(
                lombokEnabled,
                allArgs,
                new Accessors(accessorsEnabled, accessorsEnabled, accessorsEnabled)
        ));

        // --- Запуск ---
        try {
            YojoGenerator yojoGenerator = new YojoGenerator();

            yojoGenerator.generateAll(context);
            System.out.println(" Generation completed successfully.");
        } catch (Exception e) {
            System.err.println("  Generation failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}