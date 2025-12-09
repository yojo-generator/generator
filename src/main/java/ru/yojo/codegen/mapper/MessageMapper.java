package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.message.Message;
import ru.yojo.codegen.util.MapperUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.LombokUtils.*;
import static ru.yojo.codegen.util.MapperUtil.*;

/**
 * Maps AsyncAPI message definitions ({@code components.messages.*}) to {@link Message} objects.
 * <p>
 * Handles:
 * <ul>
 *   <li>Direct {@code properties}-based messages</li>
 *   <li>{@code $ref} to schemas (with optional inline {@code properties} merge)</li>
 *   <li>Polymorphic payloads ({@code oneOf}/{@code allOf}/{@code anyOf})</li>
 *   <li>Inheritance ({@code extends}, {@code implements})</li>
 *   <li>Lombok configuration per-message</li>
 *   <li>Custom package via {@code pathForGenerateMessage}</li>
 *   <li>Leaf scalars/arrays/enums (treated as wrapper or delegated to {@link SchemaMapper})</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class MessageMapper extends AbstractMapper {

    /**
     * Delegate for schema-related logic (used for polymorphism, enums, and {@code $ref} resolution).
     */
    private final SchemaMapper schemaMapper;

    /**
     * Flag indicating whether current message payload was already processed via {@code $ref}.
     */
    private boolean filledByRef = false;

    /**
     * Constructs a message mapper with the given schema mapper.
     *
     * @param schemaMapper delegate for schema mapping
     */
    public MessageMapper(SchemaMapper schemaMapper) {
        this.schemaMapper = schemaMapper;
    }

    /**
     * Transforms all message definitions in the context into {@link Message} instances.
     * <p>
     * For each message:
     * <ul>
     *   <li>Resolves {@code $ref} and polymorphism</li>
     *   <li>Collects Lombok config</li>
     *   <li>Handles {@code extends}/{@code implements}</li>
     *   <li>Fills fields via {@link #getFillParameters}</li>
     * </ul>
     * Also cleans up referenced schemas marked for removal.
     *
     * @param processContext current generation context
     * @return list of fully prepared {@link Message} instances
     */
    public List<Message> mapMessagesToObjects(ProcessContext processContext) {
        List<Message> messageList = new ArrayList<>();
        processContext.getMessagesMap().forEach((messageName, messageValues) -> {
            // flag will be true if message was filled by schema properties
            filledByRef = false;
            System.out.println("START MAPPING OF MESSAGE: " + messageName);
            Map<String, Object> messageMap = castObjectToMap(messageValues);
            Message message = new Message();
            message.setMessageName(capitalize(messageName));
            message.setLombokProperties(LombokProperties.newLombokProperties(processContext.getLombokProperties()));
            Map<String, Object> payloadMap = castObjectToMap(messageMap.get(PAYLOAD));
            String refObject = getStringValueIfExistOrElseNull(REFERENCE, payloadMap);
            message.setPathForGenerateMessage(getStringValueIfExistOrElseNull("pathForGenerateMessage", payloadMap));

            // Process extends/implements early to determine whether fields should be filled
            AtomicBoolean needToFill = new AtomicBoolean(true);
            extendsAndImplFilling(processContext.getHelper().getExcludeInheritanceSchemas(), message, payloadMap, refObject, needToFill);

            if (needToFill.get()) {
                filledByRef = false;
                FillParameters fillParams = getFillParameters(
                        messageName,
                        payloadMap,
                        processContext,
                        processContext.getHelper().getRemoveSchemas(),
                        processContext.getHelper().getExcludeRemoveSchemas(),
                        message.getLombokProperties()
                );
                message.setFillParameters(fillParams);

                if (refObject != null) {
                    payloadMap.remove(PROPERTIES);
                    message.enrichFillParameters(
                            getFillParameters(
                                    messageName,
                                    payloadMap,
                                    processContext,
                                    processContext.getHelper().getRemoveSchemas(),
                                    processContext.getHelper().getExcludeRemoveSchemas(),
                                    message.getLombokProperties()));
                }

                // Check inheritance from referenced schema (post-fill)
                if (message.getImplementsFrom().isEmpty() && isBlank(message.getExtendsFrom())) {
                    if (refObject != null) {
                        Map<String, Object> refMap = castObjectToMap(processContext.getSchemasMap().get(refReplace(refObject)));
                        refMap.forEach((mk, mv) -> {
                            if (mk.equals(EXTENDS)) {
                                String fromClass = prepareExtendsMessage(message, mv);
                                // Skip field filling if extends == $ref target
                                if (refObject != null && refReplace(refObject).equals(fromClass)) {
                                    needToFill.set(false);
                                }
                            }
                            if (mk.equals(IMPLEMENTS)) {
                                prepareImplementsMessage(message, mv);
                            }
                        });
                    }
                }
            } else {
                message.setFillParameters(new FillParameters(new ArrayList<>()));
            }

            message.setSummary(getStringValueIfExistOrElseNull(SUMMARY, messageMap));
            message.setMessagePackageName(processContext.getMessagePackage());
            message.setCommonPackageName(processContext.getCommonPackage());
            messageList.add(message);
        });

        // Merge inheritance-excluded schemas into global exclusion set
        processContext.getHelper().getExcludeRemoveSchemas().addAll(processContext.getHelper().getExcludeInheritanceSchemas());
        if (!processContext.getHelper().getExcludeRemoveSchemas().isEmpty()) {
            processContext.getHelper().getExcludeRemoveSchemas().forEach(processContext.getHelper().getRemoveSchemas()::remove);
        }
        System.out.println("FINISH MAPPING OF MESSAGES! CLEAN UP SCHEMAS: " + processContext.getHelper().getRemoveSchemas());
        processContext.getHelper().getRemoveSchemas().forEach(processContext.getSchemasMap()::remove);
        return messageList;
    }

    /**
     * Adds interfaces to the message and registers their imports.
     *
     * @param message message instance
     * @param mv      {@code implements} section value
     */
    private static void prepareImplementsMessage(Message message, Object mv) {
        Map<String, Object> implementsMap = castObjectToMap(mv);
        List<String> fromInterfaceList = castObjectToList(implementsMap.get(FROM_INTERFACE));
        System.out.println("SHOULD IMPLEMENTS FROM: " + fromInterfaceList);
        fromInterfaceList.forEach(ifc -> {
            String[] split = ifc.split("[.]");
            message.getImplementsFrom().add(split[split.length - 1]);
            message.getImportSet().add(ifc + ";");
        });
    }

    /**
     * Adds superclass to the message and registers its import.
     *
     * @param message message instance
     * @param mv      {@code extends} section value
     * @return class name (simple)
     */
    private static String prepareExtendsMessage(Message message, Object mv) {
        Map<String, Object> extendsMap = castObjectToMap(mv);
        String fromClass = getStringValueIfExistOrElseNull(FROM_CLASS, extendsMap);
        String fromPackage = getStringValueIfExistOrElseNull(FROM_PACKAGE, extendsMap);
        System.out.println("SHOULD EXTENDS FROM: " + fromClass);
        message.setExtendsFrom(fromClass);
        message.getImportSet().add(fromPackage + "." + fromClass + ";");
        return fromClass;
    }

    /**
     * Processes {@code extends} and {@code implements} sections early to determine field-filling strategy.
     * <p>
     * If {@code $ref} and {@code extends.fromClass} point to the same schema, field population is skipped.
     *
     * @param excludeInheritanceSchemas schemas to preserve (not remove)
     * @param message                   current message
     * @param payloadMap                payload definition
     * @param refObject                 {@code $ref} target (if any)
     * @param needToFill                output flag — {@code false} if extends == $ref
     */
    private static void extendsAndImplFilling(Set<String> excludeInheritanceSchemas,
                                              Message message,
                                              Map<String, Object> payloadMap,
                                              String refObject,
                                              AtomicBoolean needToFill) {
        payloadMap.forEach((mk, mv) -> {
            if (mk.equals(EXTENDS)) {
                String fromClass = prepareExtendsMessage(message, mv);
                // Skip field filling if extends == $ref target
                if (refObject != null && refReplace(refObject).equals(fromClass)) {
                    needToFill.set(false);
                    excludeInheritanceSchemas.add(refReplace(refObject));
                }
            }
            if (mk.equals(IMPLEMENTS)) {
                prepareImplementsMessage(message, mv);
            }
        });
    }

    /**
     * Extracts field definitions and validation for a message payload.
     * Supports:
     * <ul>
     *   <li>Direct {@code properties}</li>
     *   <li>{@code $ref} to schema</li>
     *   <li>Top-level enums, scalars, arrays, maps</li>
     *   <li>Polymorphic payloads ({@code oneOf})</li>
     *   <li>{@code schema} wrapper (AsyncAPI 3 style)</li>
     * </ul>
     *
     * @param messageName          for diagnostics
     * @param payload              message payload YAML map
     * @param processContext       context
     * @param removeSchemas        schemas to skip generation (if {@code removeSchema: true})
     * @param excludeRemoveSchemas accumulated list of inner schemas to preserve
     * @param lombokProperties     effective lombok config
     * @return filled {@link FillParameters}
     */
    private FillParameters getFillParameters(String messageName,
                                             Map<String, Object> payload,
                                             ProcessContext processContext,
                                             Set<String> removeSchemas,
                                             Set<String> excludeRemoveSchemas,
                                             LombokProperties lombokProperties) {
        FillParameters parameters = new FillParameters();
        processContext.getHelper().setIsMappedFromMessages(true);
        processContext.getHelper().setIsMappedFromSchemas(false);

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

        // Polymorphism (oneOf/allOf/anyOf) → delegate to SchemaMapper
        if (POLYMORPHS.stream().anyMatch(payload::containsKey)) {
            System.out.println("POLYMORPHIC MESSAGE DETECTED: " + messageName);
            return schemaMapper.getSchemaVariableProperties(
                    messageName,
                    payload,
                    processContext.getSchemasMap(),
                    Collections.emptyMap(),
                    processContext,
                    processContext.getHelper().getInnerSchemas()
            );
        }

        // Top-level enum → generate enum in common/, leave message empty
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

        // Top-level scalars/arrays → wrap as { payload: T }
        if (isLeafScalarOrArray(payload)) {
            System.out.println("LEAF SCALAR/ARRAY DETECTED: generating wrapper DTO");
            VariableProperties vp = new VariableProperties();
            vp.setName("payload");
            vp.setSpringBootVersion(processContext.getSpringBootVersion());

            // Reuse full type resolution logic via AbstractMapper
            Map<String, Object> fakeProp = new LinkedHashMap<>(payload); // defensive copy
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

        // AsyncAPI 3 gitter-style: payload: { schema: { ... } }
        if (payload.containsKey("schema") && payload.get("schema") instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> innerSchema = (Map<String, Object>) payload.get("schema");
            return getFillParameters(messageName, innerSchema, processContext, removeSchemas, excludeRemoveSchemas, lombokProperties);
        }

        // Standard: properties map
        if (getStringValueIfExistOrElseNull(PROPERTIES, payload) != null) {
            System.out.println("Properties Mapping from message");
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

        // $ref → schema
        if (getStringValueIfExistOrElseNull(REFERENCE, payload) != null && !filledByRef) {
            filledByRef = true;
            processContext.getHelper().setIsMappedFromMessages(true);
            processContext.getHelper().setIsMappedFromSchemas(false);
            System.out.println("Starting schema-like mapping");
            String schemaName = getStringValueIfExistOrElseNull(REFERENCE, payload).replaceAll(".+/", "");
            Map<String, Object> schema = castObjectToMap(processContext.getSchemasMap().get(schemaName));
            System.out.println("SCHEMA: " + schemaName);
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

        // Payload-level additionalProperties → Map<K,V>
        if (payload.containsKey(ADDITIONAL_PROPERTIES)) {
            System.out.println("PAYLOAD-LEVEL ADDITIONAL PROPERTIES DETECTED");
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

        // Bare object → { payload: Object }
        if ("object".equals(getStringValueIfExistOrElseNull(TYPE, payload)) &&
            !payload.containsKey(PROPERTIES) &&
            !payload.containsKey(ADDITIONAL_PROPERTIES) &&
            !payload.containsKey(REFERENCE) &&
            !payload.containsKey(ENUMERATION) &&
            !POLYMORPHS.stream().anyMatch(payload::containsKey)) {
            System.out.println("BARE OBJECT DETECTED: generating simple Object field");
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

        // Fallback: malformed payload
        throw new RuntimeException("Not correct filled block messages! Payload: " + payload);
    }

    /**
     * Checks if the payload is a leaf scalar or array (not object, enum, or polymorphic).
     * Valid types: {@code string}, {@code number}, {@code integer}, {@code boolean}, {@code array}.
     *
     * @param payload payload map
     * @return {@code true} for leaf scalars/arrays
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
     * Checks if the payload is a top-level enum (type + enum, no properties/ref).
     *
     * @param payload payload map
     * @return {@code true} for leaf enums
     */
    private boolean isLeafEnum(Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) return false;
        if (POLYMORPHS.stream().anyMatch(payload::containsKey)) return false;
        if (payload.containsKey(PROPERTIES)) return false;
        if (payload.containsKey(REFERENCE)) return false;
        return getStringValueIfExistOrElseNull(TYPE, payload) != null &&
               getStringValueIfExistOrElseNull(ENUMERATION, payload) != null;
    }
}