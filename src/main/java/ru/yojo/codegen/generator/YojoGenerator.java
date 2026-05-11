package ru.yojo.codegen.generator;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.message.Message;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.generator.code.MessageCodeGenerator;
import ru.yojo.codegen.generator.code.SchemaCodeGenerator;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;
import ru.yojo.codegen.parser.AsyncAPIParser;
import ru.yojo.codegen.parser.ParseResult;
import ru.yojo.codegen.util.Logger;

import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.LogUtils.*;

/**
 * AsyncAPI-to-Java DTO code generator YOJO.
 * <p>
 * Converts AsyncAPI 2.0/3.0 specification files (YAML) into:
 * <ul>
 *   <li>Message DTOs (under {@code .messages} package)</li>
 *   <li>Schema classes: DTOs, enums, interfaces (under {@code .common} package)</li>
 * </ul>
 * Supports:
 * <ul>
 *   <li>Full {@code $ref} resolution (including external files)</li>
 *   <li>Polymorphism ({@code oneOf}, {@code allOf}, {@code anyOf})</li>
 *   <li>Lombok, validation annotations (jakarta/javax), collection realizations</li>
 *   <li>Custom packages via {@code pathForGenerateMessage}</li>
 *   <li>AsyncAPI 3.0 {@code operations} + {@code channels} model</li>
 * </ul>
 * <p>
 * Entry point: {@link #generateAll(YojoContext)}.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class YojoGenerator {
    private static final Logger LOG = new Logger(YojoGenerator.class);

    /**
     * Initializes and prints startup banner.
     */
    public YojoGenerator() {
        printLogo();
    }

    /**
     * Main generation entry point.
     * <p>
     * Iterates over all specification definitions in {@code yojoContext} and generates Java code for each.
     *
     * @param yojoContext top-level generation configuration
     * @throws IOException              if any I/O error occurs during file loading or writing
     * @throws IllegalArgumentException if no specifications are configured
     */
    public void generateAll(YojoContext yojoContext) throws IOException {
        if (yojoContext.getSpecificationProperties() == null || yojoContext.getSpecificationProperties().isEmpty()) {
            throw new IllegalArgumentException("specificationProperties is required.");
        }
        for (SpecificationProperties spec : yojoContext.getSpecificationProperties()) {
            validate(spec);
            processSpecification(spec, yojoContext);
        }
    }

    /**
     * Validates that a specification definition contains all required fields.
     *
     * @param spec specification to validate
     * @throws IllegalArgumentException if any required field is missing
     */
    private void validate(SpecificationProperties spec) {
        if (spec.getSpecName() == null || spec.getSpecName().trim().isEmpty()) {
            throw new IllegalArgumentException("specName (e.g. 'test.yaml') is required.");
        }
        if (spec.getInputDirectory() == null || spec.getInputDirectory().trim().isEmpty()) {
            throw new IllegalArgumentException("inputDirectory required for spec: " + spec.getSpecName());
        }
        if (spec.getOutputDirectory() == null || spec.getOutputDirectory().trim().isEmpty()) {
            throw new IllegalArgumentException("outputDirectory required for spec: " + spec.getSpecName());
        }
        if (spec.getPackageLocation() == null || spec.getPackageLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("packageLocation required for spec: " + spec.getSpecName());
        }
    }

    /**
     * Processes a single AsyncAPI specification file.
     *
     * @param spec        specification definition
     * @param yojoContext shared context (Lombok, Spring Boot version, global config)
     * @throws IOException if spec file not found or cannot be read
     */
    private void processSpecification(SpecificationProperties spec, YojoContext yojoContext) throws IOException {
        SchemaMapper schemaMapper = new SchemaMapper();
        MessageMapper messageMapper = new MessageMapper(schemaMapper);
        Path inputDir = Paths.get(spec.getInputDirectory()).toAbsolutePath().normalize();
        String specFileName = spec.getSpecName().trim();
        Path specFilePath = inputDir.resolve(specFileName);
        if (!Files.exists(specFilePath)) {
            throw new IllegalArgumentException("Spec file not found: " + specFilePath);
        }

        // Parse specification (YAML loading, $ref resolution, message population)
        AsyncAPIParser parser = new AsyncAPIParser();
        ParseResult result = parser.parse(specFilePath, inputDir, spec.getPackageLocation());

        // Prepare generation context
        ProcessContext ctx = new ProcessContext(new Yaml().load(result.processedContent()));
        ctx.setFilePath(specFilePath.toString());
        ctx.setPackageLocation(spec.getPackageLocation());
        ctx.setLombokProperties(yojoContext.getLombokProperties());
        ctx.setSpringBootVersion(yojoContext.getSpringBootVersion());
        ctx.setNullableAnnotation(yojoContext.getNullableAnnotation());
        ctx.setOutputDirectory(spec.getOutputDirectory());
        ctx.setPathToWrite(spec.getOutputDirectory());
        boolean splitModels = spec.isSplitModels();
        ctx.setSplitModels(splitModels);

        String basePackage = spec.getPackageLocation();
        if (splitModels) {
            ctx.setMessagePackage(basePackage + ".messages;");
            ctx.setCommonPackage(basePackage + ".common;");
        } else {
            String unified = basePackage + ";";
            ctx.setMessagePackage(unified);
            ctx.setCommonPackage(unified);
        }
        ctx.setSchemasMap(result.schemas());
        ctx.setMessagesMap(result.messages());

        ctx.setExperimental(yojoContext.isExperimental());

        process(ctx, schemaMapper, messageMapper);
    }

    // ————————————————————————————————————————
    // Core processing
    // ————————————————————————————————————————

    /**
     * Main processing pipeline: generates schemas and messages from the parsed context.
     *
     * @param ctx           generation context with pre-populated schemas and messages
     * @param schemaMapper  schema mapper
     * @param messageMapper message mapper
     */
    private void process(ProcessContext ctx, SchemaMapper schemaMapper, MessageMapper messageMapper) {
        processMessages(ctx, messageMapper);
        processSchemas(ctx, schemaMapper);
        LOG.info(LOG_FINISH);
    }

    /**
     * Generates and writes all schema classes (DTOs, enums, interfaces).
     *
     * @param ctx          generation context
     * @param schemaMapper mapper
     */
    private void processSchemas(ProcessContext ctx, SchemaMapper schemaMapper) {
        LOG.info(LOG_DELIMETER);
        List<Schema> schemaList = schemaMapper.mapSchemasToObjects(ctx);
        if (!schemaList.isEmpty()) {
            LOG.info("START WRITING JAVA CLASS FROM SCHEMAS:");
            schemaList.forEach(schema -> LOG.info(schema.getSchemaName()));
            writeSchemas(ctx, schemaMapper);
            LOG.info(LOG_DELIMETER + ANSI_RESET);
        }
    }

    /**
     * Generates and writes all message DTOs.
     *
     * @param ctx           generation context
     * @param messageMapper mapper
     */
    private void processMessages(ProcessContext ctx, MessageMapper messageMapper) {
        LOG.info(ANSI_CYAN + LOG_DELIMETER);
        List<Message> messageList = messageMapper.mapMessagesToObjects(ctx);
        LOG.info("START WRITING JAVA CLASS FROM MESSAGES:");
        messageList.forEach(message -> LOG.info(message.getMessageName()));
        writeMessages(ctx, messageMapper);
        LOG.info(LOG_DELIMETER);
    }

    private void writeMessages(ProcessContext ctx, MessageMapper messageMapper) {
        List<Message> messageList = messageMapper.mapMessagesToObjects(ctx);
        for (Message message : messageList) {
            String customPath = message.getPathForGenerateMessage();
            writeFileUnified(ctx, message.getMessageName(), new MessageCodeGenerator(message).generate(), true, customPath);
        }
    }

    private void writeSchemas(ProcessContext ctx, SchemaMapper schemaMapper) {
        List<Schema> schemaList = schemaMapper.mapSchemasToObjects(ctx);
        for (Schema schema : schemaList) {
            writeFileUnified(ctx, schema.getSchemaName(), new SchemaCodeGenerator(schema).generate(), false, null);
        }
    }

    private void writeFileUnified(ProcessContext ctx, String fileName, String content, boolean isMessage, String customPath) {
        String baseOutput = ctx.getOutputDirectory();
        if (!baseOutput.endsWith("/")) {
            baseOutput += "/";
        }

        String targetDir;
        if (customPath != null && !customPath.trim().isEmpty()) {
            // customPath — это относительный путь (например, "io.github.somepath")
            targetDir = baseOutput + customPath.replace('.', '/') + "/";
        } else if (ctx.isSplitModels()) {
            targetDir = baseOutput + (isMessage ? "messages/" : "common/");
        } else {
            targetDir = baseOutput;
        }

        JavaFileWriter writer = new JavaFileWriter();
        writer.writeFile(targetDir, fileName, content);
    }

}
