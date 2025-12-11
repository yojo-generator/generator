package ru.yojo.codegen;

import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.generator.YojoGenerator;

import java.util.Collections;

import static ru.yojo.codegen.util.MapperUtil.isTrue;

/**
 * Command-line interface for the Yojo AsyncAPI-to-Java DTO generator.
 * <p>
 * Supports both single-file and directory-based specification input.
 * Generates Java classes for messages and schemas into the specified output directory.
 *
 * <h2>Usage</h2>
 * <pre>
 * java -cp yojo.jar ru.yojo.codegen.YojoCLI \
 *   &lt;inputFileOrDir&gt; \
 *   &lt;outputDir&gt; \
 *   &lt;packageLocation&gt; \
 *   [lombokEnabled=false] \
 *   [allArgs=false] \
 *   [accessors=false]
 * </pre>
 *
 * <h3>Arguments</h3>
 * <ul>
 *   <li>{@code inputFileOrDir} — path to AsyncAPI YAML file or directory containing specs</li>
 *   <li>{@code outputDir} — base output directory (e.g., {@code src/main/java})</li>
 *   <li>{@code packageLocation} — base Java package (e.g., {@code com.example.api})</li>
 *   <li>{@code lombokEnabled} — enable Lombok annotations (default: {@code false})</li>
 *   <li>{@code allArgs} — generate {@code @AllArgsConstructor} (default: {@code false})</li>
 *   <li>{@code noArgs} — generate {@code @NoArgsConstructor} (default: {@code false})</li>
 *   <li>{@code accessors} — enable {@code @Accessors(fluent = true, chain = true)} (default: {@code false})</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class YojoCLI {

    /**
     * Entry point for command-line execution.
     *
     * @param args command-line arguments (see class-level Javadoc for format)
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java -cp ... YojoCLI <inputFileOrDir> <outputDir> <packageLocation> [lombokEnabled=false] [allArgs=false] [noArgs=false] [enumAllArgs=false] [enumNoArgs=false] [accessors=false]");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputDir = args[1];
        String packageLocation = args[2];

        boolean lombokEnabled = args.length > 3 && isTrue(Boolean.valueOf(args[3]));
        boolean allArgs = args.length > 4 && isTrue(Boolean.valueOf(args[4]));
        boolean noArgs = args.length > 5 && isTrue(Boolean.valueOf(args[5]));
        boolean enumAllArgs = args.length > 6 && isTrue(Boolean.valueOf(args[6]));
        boolean enumNoArgs = args.length > 7 && isTrue(Boolean.valueOf(args[7]));
        boolean accessorsEnabled = args.length > 8 && isTrue(Boolean.valueOf(args[8]));

        // --- Context setup ---
        YojoContext context = new YojoContext();

        // Resolve input: file → use its parent dir + infer spec name; directory → use as-is
        boolean isFile = !new java.io.File(inputPath).isDirectory();
        String inputDir;
        String specName;
        if (isFile) {
            java.io.File inputFile = new java.io.File(inputPath);
            inputDir = inputFile.getParentFile().getAbsolutePath();
            specName = inputFile.getName().replaceFirst("\\.[^.]+$", ""); // strip extension
        } else {
            inputDir = inputPath;
            specName = "cli-spec";
        }

        // Prepare specification definition
        SpecificationProperties spec = new SpecificationProperties();
        spec.setSpecName(specName);
        spec.setInputDirectory(inputDir);
        spec.setOutputDirectory(outputDir);
        spec.setPackageLocation(packageLocation);

        context.setSpecificationProperties(Collections.singletonList(spec));
        context.setSpringBootVersion("3.x.x"); // fixed default; could be made configurable
        context.setLombokProperties(new LombokProperties(
                lombokEnabled,
                allArgs,
                noArgs,
                enumAllArgs,
                enumNoArgs,
                new Accessors(accessorsEnabled, accessorsEnabled, accessorsEnabled)
        ));

        // --- Run generator ---
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