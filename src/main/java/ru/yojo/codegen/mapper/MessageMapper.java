package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.message.Message;
import ru.yojo.codegen.domain.message.MessageBuilder;
import ru.yojo.codegen.exception.SchemaFillException;
import ru.yojo.codegen.util.Logger;
import ru.yojo.codegen.util.MapperUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.LombokUtils.*;
import static ru.yojo.codegen.util.MapperUtil.*;

/**
 * Mapper responsible for converting AsyncAPI {@code components.messages} definitions into {@link Message} objects.
 * <p>
 * Handles:
 * <ul>
 *   <li>Polymorphic payloads ({@code oneOf}, {@code allOf}, {@code anyOf}) via deep merging</li>
 *   <li>References to schemas ({@code $ref}) including inheritance/implementation</li>
 *   <li>Leaf scalars/arrays as wrapper DTOs (e.g., {@code payload: string} → class with {@code private String payload})</li>
 *   <li>Custom generation paths via {@code pathForGenerateMessage}</li>
 *   <li>Schema cleanup (e.g., {@code removeSchema: true})</li>
 *   <li>Validation groups and Lombok overrides per message</li>
 * </ul>
 *
 * <p>
 * Delegates schema-based mapping logic (e.g., field resolution, inner-schema discovery) to {@link SchemaMapper}.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class MessageMapper extends AbstractMapper {

    private static final Logger LOG = new Logger(MessageMapper.class);

    private final SchemaMapper schemaMapper;
    private boolean filledByRef = false;

    /**
     * Constructs a new message mapper with the given schema mapper for delegation.
     *
     * @param schemaMapper schema mapper to resolve $ref and shared logic
     */
    public MessageMapper(SchemaMapper schemaMapper) {
        this.schemaMapper = schemaMapper;
    }

    /**
     * Converts all message definitions from the given {@link ProcessContext} into {@link Message} instances.
     * <p>
     * For each message:
     * <ul>
     *   <li>Initializes message metadata (name, packages, summary)</li>
     *   <li>Processes inheritance/implementation directives ({@code extends}, {@code implements})</li>
     *   <li>Resolves payload structure (polymorphic, reference, inline properties, leaf types)</li>
     *   <li>Merges fields from {@code $ref} and inline {@code properties} without duplication</li>
     *   <li>Applies schema removal logic (e.g., via {@code removeSchema: true})</li>
     * </ul>
     *
     * @param processContext current generation context
     * @return list of fully populated {@link Message} objects ready for code generation
     */
    public List<Message> mapMessagesToObjects(ProcessContext processContext) {
        List<Message> messageList = new ArrayList<>();
        processContext.getMessagesMap().forEach((messageName, messageValues) -> {
            filledByRef = false;
            LOG.info("START MAPPING OF MESSAGE: " + messageName);
            Map<String, Object> messageMap = castObjectToMap(messageValues);
            Map<String, Object> payloadMap = castObjectToMap(messageMap.get(PAYLOAD));
            String refObject = getStringValueIfExistOrElseNull(REFERENCE, payloadMap);
            LombokProperties msgLombok = LombokProperties.newLombokProperties(processContext.getLombokProperties());

            String customPath = getStringValueIfExistOrElseNull("pathForGenerateMessage", payloadMap);
            MessageBuilder builder = Message.builder()
                    .name(capitalize(messageName))
                    .lombokProperties(msgLombok)
                    .pathForGenerateMessage(customPath);

            // Effective message package: custom path overrides default
            if (customPath != null) {
                builder.messagePackageName(processContext.getPackageLocation() + "." + customPath + ";");
            } else {
                builder.messagePackageName(processContext.getMessagePackage());
            }

            AtomicBoolean needToFill = new AtomicBoolean(true);
            extendsAndImplFilling(processContext.getHelper().getExcludeInheritanceSchemas(), builder, payloadMap, refObject, needToFill, processContext);
            if (needToFill.get()) {
                filledByRef = false;
                FillParameters fillParams = getFillParameters(
                        messageName,
                        payloadMap,
                        processContext,
                        processContext.getHelper().getRemoveSchemas(),
                        processContext.getHelper().getExcludeRemoveSchemas(),
                        msgLombok
                );
                builder.fillParameters(fillParams);

                if (refObject != null) {
                    Map<String, Object> propertiesMap = new LinkedHashMap<>();
                    if (payloadMap.containsKey(PROPERTIES)) {
                        propertiesMap = castObjectToMap(payloadMap.get(PROPERTIES));
                    }
                    payloadMap.remove(PROPERTIES);
                    FillParameters extraFillParams = getFillParameters(
                            messageName,
                            payloadMap,
                            processContext,
                            processContext.getHelper().getRemoveSchemas(),
                            processContext.getHelper().getExcludeRemoveSchemas(),
                            msgLombok);
                    // Merge fill params (equivalent to enrichFillParameters)
                    List<VariableProperties> combined = new ArrayList<>(fillParams.getVariableProperties());
                    combined.addAll(extraFillParams.getVariableProperties());
                    builder.fillParameters(new FillParameters(combined));

                    if (!propertiesMap.isEmpty()) {
                        payloadMap.put(PROPERTIES, propertiesMap);
                        LOG.info("Properties Mapping from message");
                        List<VariableProperties> inlineVars = new LinkedList<>();
                        propertiesMap.forEach((propertyName, propertyValue) -> {
                            VariableProperties mvp = new VariableProperties();
                            String javaName = MapperUtil.toValidJavaFieldName(propertyName);
                            fillProperties(messageName, mvp, payloadMap, payloadMap, javaName, castObjectToMap(propertyValue), processContext, processContext.getHelper().getInnerSchemas());
                            if (mvp.getItems() != null && !JAVA_DEFAULT_TYPES.contains(mvp.getItems())) {
                                processContext.getHelper().getExcludeRemoveSchemas().add(mvp.getItems());
                            }
                            inlineVars.add(mvp);
                        });
                        Set<String> existingNames = combined.stream()
                                .map(VariableProperties::getName)
                                .collect(Collectors.toSet());
                        inlineVars.stream()
                                .filter(vp -> !existingNames.contains(vp.getName()))
                                .forEach(combined::add);
                        builder.fillParameters(new FillParameters(combined));
                    }
                }
                if (builder.getImplementsFrom().isEmpty() && isBlank(builder.getExtendsFrom())) {
                    if (refObject != null) {
                        Map<String, Object> refMap = castObjectToMap(processContext.getSchemasMap().get(refReplace(refObject)));
                        refMap.forEach((mk, mv) -> {
                            if (mk.equals(EXTENDS)) {
                                String fromClass = prepareExtendsMessage(builder, mv, processContext);
                                if (refObject != null && refReplace(refObject).equals(fromClass)) {
                                    needToFill.set(false);
                                }
                            }
                            if (mk.equals(IMPLEMENTS)) {
                                prepareImplementsMessage(builder, mv);
                            }
                        });
                    }
                }
            } else {
                builder.fillParameters(new FillParameters(new ArrayList<>()));
            }
            // Process x-class-annotation for messages
            Set<String> classAnnotations = getSetValueIfExistsOrElseEmptySet(X_CLASS_ANNOTATION, payloadMap);
            classAnnotations.forEach(builder::addClassAnnotation);

            Message message = builder
                    .summary(getStringValueIfExistOrElseNull(SUMMARY, messageMap))
                    .commonPackageName(processContext.getCommonPackage())
                    .build();
            messageList.add(message);
        });

        processContext.getHelper().getExcludeRemoveSchemas().addAll(processContext.getHelper().getExcludeInheritanceSchemas());
        if (!processContext.getHelper().getExcludeRemoveSchemas().isEmpty()) {
            processContext.getHelper().getExcludeRemoveSchemas().forEach(processContext.getHelper().getRemoveSchemas()::remove);
        }
        LOG.info("FINISH MAPPING OF MESSAGES! CLEAN UP SCHEMAS: " + processContext.getHelper().getRemoveSchemas());
        processContext.getHelper().getRemoveSchemas().forEach(processContext.getSchemasMap()::remove);
        return messageList;
    }

    /**
     * Parses {@code implements} block and configures message to implement the specified interfaces.
     *
     * @param builder message builder to configure
     * @param mv      value of {@code implements} key (should be a map with {@code fromInterface} list)
     */
    private static void prepareImplementsMessage(MessageBuilder builder, Object mv) {
        Map<String, Object> implementsMap = castObjectToMap(mv);
        List<String> fromInterfaceList = castObjectToList(implementsMap.get(FROM_INTERFACE));
        LOG.info("SHOULD IMPLEMENTS FROM: " + fromInterfaceList);
        fromInterfaceList.forEach(ifc -> {
            String[] split = ifc.split("[.]");
            builder.addImplementsFrom(split[split.length - 1]);
            builder.addImport(ifc + ";");
        });
    }

    /**
     * Parses {@code extends} block and configures message to extend the specified class.
     *
     * @param builder         message builder to configure
     * @param mv              value of {@code extends} key (should be a map with {@code fromClass}, {@code fromPackage})
     * @param processContext  context
     * @return resolved superclass simple name (e.g., {@code "BaseMessage"})
     */
    private static String prepareExtendsMessage(MessageBuilder builder, Object mv, ProcessContext processContext) {
        Map<String, Object> extendsMap = castObjectToMap(mv);
        String fromClass = getStringValueIfExistOrElseNull(FROM_CLASS, extendsMap);
        String fromPackage = getStringValueIfExistOrElseNull(FROM_PACKAGE, extendsMap);
        LOG.info("SHOULD EXTENDS FROM: " + fromClass);
        if (fromClass != null && processContext.getSchemasMap().containsKey(fromClass)) {
            fromPackage = null;
        }

        if (fromPackage != null) {
            builder.addImport(fromPackage + "." + fromClass + ";");
        } else {
            String effectivePkg = processContext.getEffectiveCommonPackage().replace(";", "");
            builder.addImport(effectivePkg + "." + fromClass + ";");
        }

        builder.extendsFrom(fromClass);
        return fromClass;
    }

    /**
     * Processes {@code extends} and {@code implements} sections for a message payload.
     * <p>
     * If {@code extends} targets the same schema referenced via {@code $ref}, skips field generation
     * and marks the schema for potential exclusion from inheritance filling.
     *
     * @param excludeInheritanceSchemas shared set to mark excluded schemas
     * @param builder                    message builder to configure
     * @param payloadMap                 message payload definition map
     * @param refObject                  value of {@code $ref}, if any
     * @param needToFill                 flag to disable field population
     * @param processContext             context
     */
    private static void extendsAndImplFilling(Set<String> excludeInheritanceSchemas,
                                              MessageBuilder builder,
                                              Map<String, Object> payloadMap,
                                              String refObject,
                                              AtomicBoolean needToFill,
                                              ProcessContext processContext) {
        payloadMap.forEach((mk, mv) -> {
            if (mk.equals(EXTENDS)) {
                String fromClass = prepareExtendsMessage(builder, mv, processContext);
                if (refObject != null && refReplace(refObject).equals(fromClass)) {
                    needToFill.set(false);
                    excludeInheritanceSchemas.add(refReplace(refObject));
                }
            }
            if (mk.equals(IMPLEMENTS)) {
                prepareImplementsMessage(builder, mv);
            }
        });
    }

    /**
     * Populates {@link FillParameters} for a given message payload.
     * <p>
     * Handles 9 cases in order:
     * <ol>
     *   <li>Lombok override block</li>
     *   <li>Polymorphic payload ({@code oneOf}/{@code allOf}/{@code anyOf}) → flattened into properties</li>
     *   <li>Top-level enum (e.g., {@code type: object, enum: [...]})</li>
     *   <li>Leaf scalar/array (e.g., {@code type: string}) → wrapper field named {@code payload}</li>
     *   <li>Gitter-style {@code schema} block</li>
     *   <li>Inline {@code properties}</li>
     *   <li>{@code $ref} to schema</li>
     *   <li>{@code additionalProperties} at payload root</li>
     *   <li>Bare {@code type: object} with no content → {@code Object payload}</li>
     * </ol>
     *
     * @param messageName          message key (e.g., {@code "UserSignedUp"})
     * @param payload              payload definition map
     * @param processContext       generation context
     * @param removeSchemas        set to register schemas for removal (e.g., via {@code removeSchema: true})
     * @param excludeRemoveSchemas set to preserve referenced schemas from removal
     * @param lombokProperties     effective Lombok config for this message
     * @return populated {@link FillParameters}
     */
    private FillParameters getFillParameters(String messageName,
                                             Map<String, Object> payload,
                                             ProcessContext processContext,
                                             Set<String> removeSchemas,
                                             Set<String> excludeRemoveSchemas,
                                             LombokProperties lombokProperties) {
        FillParameters parameters = new FillParameters();

        if (payload.containsKey(LOMBOK)) {
            Map<String, Object> lombokProps = castObjectToMap(payload.get(LOMBOK));
            if (lombokProps.containsKey(ENABLE) &&
                "false".equals(getStringValueIfExistOrElseNull(ENABLE, lombokProps))) {
                lombokProperties.setEnableLombok(Boolean.valueOf(getStringValueIfExistOrElseNull(ENABLE, lombokProps)));
            } else {
                fillLombokAccessors(lombokProperties, lombokProps);
                fillLombokEqualsAndHashCode(lombokProperties, lombokProps);
                fillLombokConstructors(lombokProperties, lombokProps);
            }
        }

        if (POLYMORPHS.stream().anyMatch(payload::containsKey)) {
            LOG.info("POLYMORPHIC MESSAGE DETECTED: " + messageName);

            // 🔧 FIX: resolve polymorphic payload into flat properties and generate fields
            Map<String, Object> resolvedPayload = new LinkedHashMap<>(payload);

            // 1. Merge allOf/oneOf/anyOf into flat 'properties'
            Map<String, Object> mergedProperties = mergePolymorphicProperties(
                    messageName, resolvedPayload, processContext.getSchemasMap(), processContext.getHelper().getInnerSchemas()
            );

            // If resolvedProperties not empty — inject into payload for standard processing
            if (!mergedProperties.isEmpty()) {
                resolvedPayload.put("properties", mergedProperties);
                resolvedPayload.remove("allOf");
                resolvedPayload.remove("oneOf");
                resolvedPayload.remove("anyOf");
                LOG.info(" → flattened polymorphic payload for message: " + messageName);
            }

            // 2. Proceed as if it's a normal properties-based message
            if (resolvedPayload.containsKey("properties")) {
                LOG.info("Properties Mapping from flattened polymorphic message");
                Map<String, Object> propertiesMap = castObjectToMap(resolvedPayload.get("properties"));
                List<VariableProperties> variableProperties = new LinkedList<>();
                propertiesMap.forEach((propertyName, propertyValue) -> {
                    VariableProperties vp = new VariableProperties();
                    String javaName = MapperUtil.toValidJavaFieldName(propertyName);
                    fillProperties(
                            messageName,
                            vp,
                            resolvedPayload,
                            resolvedPayload,
                            javaName,
                            castObjectToMap(propertyValue),
                            processContext,
                            processContext.getHelper().getInnerSchemas()
                    );
                    if (vp.getItems() != null && !JAVA_DEFAULT_TYPES.contains(vp.getItems())) {
                        processContext.getHelper().getExcludeRemoveSchemas().add(vp.getItems());
                    }
                    variableProperties.add(vp);
                });
                return new FillParameters(variableProperties);
            }

            // Fallback: delegate to schema mapper (legacy path)
            return schemaMapper.getSchemaVariableProperties(
                    messageName,
                    payload,
                    processContext.getSchemasMap(),
                    Collections.emptyMap(),
                    processContext,
                    processContext.getHelper().getInnerSchemas()
            );
        }

        if (isLeafEnum(payload)) {
            schemaMapper.getSchemaVariableProperties(
                    messageName,
                    payload,
                    processContext.getSchemasMap(),
                    Collections.emptyMap(),
                    processContext,
                    processContext.getHelper().getInnerSchemas()
            );
            return new FillParameters(Collections.emptyList());
        }

        if (isLeafScalarOrArray(payload)) {
            LOG.info("LEAF SCALAR/ARRAY DETECTED: generating wrapper DTO");
            VariableProperties vp = new VariableProperties();
            vp.setName("payload");
            vp.setSpringBootVersion(processContext.getSpringBootVersion());
            Map<String, Object> fakeProp = new LinkedHashMap<>(payload);
            fillProperties(
                    messageName,
                    vp,
                    payload,
                    processContext.getSchemasMap(),
                    "payload",
                    fakeProp,
                    processContext,
                    processContext.getHelper().getInnerSchemas()
            );
            return new FillParameters(Collections.singletonList(vp));
        }

        if (payload.containsKey("schema") && payload.get("schema") instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> innerSchema = (Map<String, Object>) payload.get("schema");
            return getFillParameters(messageName, innerSchema, processContext, removeSchemas, excludeRemoveSchemas, lombokProperties);
        }

        if (getStringValueIfExistOrElseNull(PROPERTIES, payload) != null) {
            LOG.info("Properties Mapping from message");
            Map<String, Object> propertiesMap = castObjectToMap(payload.get(PROPERTIES));
            List<VariableProperties> variableProperties = new LinkedList<>();
            propertiesMap.forEach((propertyName, propertyValue) -> {
                VariableProperties mvp = new VariableProperties();
                String javaName = MapperUtil.toValidJavaFieldName(propertyName);
                fillProperties(messageName, mvp, payload, payload, javaName, castObjectToMap(propertyValue), processContext, processContext.getHelper().getInnerSchemas());
                if (mvp.getItems() != null && !JAVA_DEFAULT_TYPES.contains(mvp.getItems())) {
                    excludeRemoveSchemas.add(mvp.getItems());
                }
                variableProperties.add(mvp);
            });
            return new FillParameters(variableProperties);
        }

        if (getStringValueIfExistOrElseNull(REFERENCE, payload) != null && !filledByRef) {
            filledByRef = true;
            LOG.info("Starting schema-like mapping");
            String schemaName = getStringValueIfExistOrElseNull(REFERENCE, payload).replaceAll(".+/", "");
            Map<String, Object> schema = castObjectToMap(processContext.getSchemasMap().get(schemaName));
            LOG.info("SCHEMA: " + schemaName);
            Map<String, Object> innerSchemas = new ConcurrentHashMap<>();
            parameters = schemaMapper.getSchemaVariableProperties(
                    schemaName,
                    schema,
                    processContext.getSchemasMap(),
                    castObjectToMap(schema.get(PROPERTIES)),
                    processContext,
                    innerSchemas
            );
            if (getStringValueIfExistOrElseNull(REMOVE_SCHEMA, payload) != null &&
                getStringValueIfExistOrElseNull(REMOVE_SCHEMA, payload).equals("true")) {
                removeSchemas.add(schemaName);
            }
            if (!innerSchemas.isEmpty()) {
                innerSchemas.forEach((pk, pv) -> excludeRemoveSchemas.add(pk));
                processContext.getSchemasMap().putAll(innerSchemas);
            }
            return parameters;
        }

        if (payload.containsKey(ADDITIONAL_PROPERTIES)) {
            LOG.info("PAYLOAD-LEVEL ADDITIONAL PROPERTIES DETECTED");
            VariableProperties vp = new VariableProperties();
            vp.setName("payload");
            vp.setSpringBootVersion(processContext.getSpringBootVersion());
            fillVariableProperties(
                    messageName,
                    vp,
                    payload,
                    processContext.getSchemasMap(),
                    "payload",
                    payload,
                    processContext,
                    processContext.getHelper().getInnerSchemas()
            );
            return new FillParameters(Collections.singletonList(vp));
        }

        if ("object".equals(getStringValueIfExistOrElseNull(TYPE, payload)) &&
            !payload.containsKey(PROPERTIES) &&
            !payload.containsKey(ADDITIONAL_PROPERTIES) &&
            !payload.containsKey(REFERENCE) &&
            !payload.containsKey(ENUMERATION) &&
            !POLYMORPHS.stream().anyMatch(payload::containsKey)) {
            LOG.info("BARE OBJECT DETECTED: generating simple Object field");
            VariableProperties vp = new VariableProperties();
            vp.setName("payload");
            vp.setSpringBootVersion(processContext.getSpringBootVersion());
            fillVariableProperties(
                    messageName,
                    vp,
                    payload,
                    processContext.getSchemasMap(),
                    "payload",
                    payload,
                    processContext,
                    processContext.getHelper().getInnerSchemas()
            );
            return new FillParameters(Collections.singletonList(vp));
        }

        if (filledByRef) {
            return new FillParameters();
        }

        throw new SchemaFillException("Not correct filled block messages! Payload: " + payload);
    }

    /**
     * Registers a synthetic schema (e.g., for inner objects) if it doesn't already exist.
     * Ensures minimal structure: {@code type: object} and empty {@code properties}.
     *
     * @param schemaName   proposed schema name
     * @param payload      original payload definition
     * @param schemasMap   global schemas map (mutated)
     * @param innerSchemas inner schemas accumulator (mutated)
     */
    private void registerSyntheticSchemaIfNeeded(String schemaName,
                                                 Map<String, Object> payload,
                                                 Map<String, Object> schemasMap,
                                                 Map<String, Object> innerSchemas) {
        if (schemasMap.containsKey(schemaName)) {
            return;
        }

        Map<String, Object> synthetic = new LinkedHashMap<>(payload);
        if (!synthetic.containsKey(TYPE)) {
            synthetic.put(TYPE, OBJECT);
        }
        if (!synthetic.containsKey(PROPERTIES)) {
            synthetic.put(PROPERTIES, new LinkedHashMap<>());
        }

        schemasMap.put(schemaName, synthetic);
        innerSchemas.put(schemaName, synthetic);
        LOG.info(" → registered synthetic schema: " + schemaName);
    }

    /**
     * Checks if the payload represents a leaf scalar or array (no nesting, refs, or polymorphism).
     *
     * @param payload payload map
     * @return {@code true} for leaf primitives/arrays
     */
    private boolean isLeafScalarOrArray(Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) return false;
        if (POLYMORPHS.stream().anyMatch(payload::containsKey)) return false;
        if (payload.containsKey(PROPERTIES)) return false;
        if (payload.containsKey(REFERENCE)) return false;
        if (payload.containsKey("schema")) return false;
        if (payload.containsKey(ENUMERATION)) return false;
        String type = getStringValueIfExistOrElseNull(TYPE, payload);
        if (type == null) return false;
        return "string".equals(type) ||
               "number".equals(type) ||
               "integer".equals(type) ||
               "boolean".equals(type) ||
               "array".equals(type);
    }

    /**
     * Checks if the payload represents a top-level enum (type + enum, no properties).
     *
     * @param payload payload map
     * @return {@code true} if leaf enum
     */
    private boolean isLeafEnum(Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) return false;
        if (POLYMORPHS.stream().anyMatch(payload::containsKey)) return false;
        if (payload.containsKey(PROPERTIES)) return false;
        if (payload.containsKey(REFERENCE)) return false;
        return getStringValueIfExistOrElseNull(TYPE, payload) != null &&
               getStringValueIfExistOrElseNull(ENUMERATION, payload) != null;
    }

    /**
     * Recursively merges properties from all {@code $ref}-ed and inline objects in polymorphic blocks.
     * <p>
     * Supports nested polymorphism and prioritizes later declarations (last-wins for key collisions).
     *
     * @param messageName  message name (for logging)
     * @param payload      payload with {@code oneOf}/{@code allOf}/{@code anyOf}
     * @param schemasMap   global schemas registry
     * @param innerSchemas inner schema accumulator (mutated by recursive calls)
     * @return merged map of field name → definition
     */
    private Map<String, Object> mergePolymorphicProperties(
            String messageName,
            Map<String, Object> payload,
            Map<String, Object> schemasMap,
            Map<String, Object> innerSchemas) {

        Map<String, Object> merged = new LinkedHashMap<>();

        for (String polyKey : POLYMORPHS) {
            if (payload.containsKey(polyKey)) {
                List<Object> refs = castObjectToListObjects(payload.get(polyKey));
                for (Object refItem : refs) {
                    Map<String, Object> refOrInline = castObjectToMap(refItem);

                    // Case 1: $ref
                    String ref = getStringValueIfExistOrElseNull("$ref", refOrInline);
                    if (ref != null) {
                        String schemaName = refReplace(ref);
                        Map<String, Object> target = castObjectToMap(schemasMap.get(schemaName));
                        if (target != null) {
                            // Recursively resolve if target itself has allOf/oneOf
                            if (POLYMORPHS.stream().anyMatch(target::containsKey)) {
                                Map<String, Object> nested = mergePolymorphicProperties(schemaName, target, schemasMap, innerSchemas);
                                merged.putAll(nested);
                            }
                            Map<String, Object> props = castObjectToMap(target.get("properties"));
                            if (props != null) {
                                merged.putAll(props);
                            }
                        }
                    } else {
                        // Case 2: inline object (e.g., { type: object, properties: { ... } })
                        Map<String, Object> props = castObjectToMap(refOrInline.get("properties"));
                        if (props != null) {
                            merged.putAll(props);
                        }
                    }
                }
            }
        }

        return merged;
    }
}
