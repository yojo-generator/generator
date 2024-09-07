package ru.yojo.codegen.mapper;

import org.springframework.stereotype.Component;
import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.message.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.LombokUtils.fillLombokAccessors;
import static ru.yojo.codegen.util.LombokUtils.fillLombokEqualsAndHashCode;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
@Component
public class MessageMapper extends AbstractMapper {

    private final Helper helper;
    private final SchemaMapper schemaMapper;
    private boolean filledByRef = false;

    public MessageMapper(Helper helper, SchemaMapper schemaMapper) {
        super(helper);
        this.helper = helper;
        this.schemaMapper = schemaMapper;
    }

    /**
     * @param processContext processContext
     * @return list of prepared messages
     */
    public List<Message> mapMessagesToObjects(ProcessContext processContext) {
        List<Message> messageList = new ArrayList<>();
        Set<String> removeSchemas = new HashSet<>();
        Set<String> excludeRemoveSchemas = new HashSet<>();
        Set<String> excludeInheritanceSchemas = new HashSet<>();
        processContext.getMessagesMap().forEach((messageName, messageValues) -> {
            //flag will true if message was filled by schemas propeties
            filledByRef = false;
            System.out.println("START MAPPING OF MESSAGE: " + messageName);
            Map<String, Object> messageMap = castObjectToMap(messageValues);
            Message message = new Message();
            message.setMessageName(capitalize(messageName));
            message.setLombokProperties(LombokProperties.newLombokProperties(processContext.getLombokProperties()));

            Map<String, Object> payloadMap = castObjectToMap(messageMap.get(PAYLOAD));
            String refObject = getStringValueIfExistOrElseNull(REFERENCE, payloadMap);
            message.setPathForGenerateMessage(getStringValueIfExistOrElseNull("pathForGenerateMessage", payloadMap));

            //see extendsAndImplFilling method
            AtomicBoolean needToFill = new AtomicBoolean(true);
            extendsAndImplFilling(excludeInheritanceSchemas, message, payloadMap, refObject, needToFill);

            if (needToFill.get()) {
                filledByRef = false;
                message.setFillParameters(
                        getFillParameters(
                                messageName,
                                payloadMap,
                                processContext,
                                removeSchemas,
                                excludeRemoveSchemas,
                                message.getLombokProperties()
                        )
                );
                if (refObject != null) {
                    payloadMap.remove(PROPERTIES);
                    message.enrichFillParameters(
                            getFillParameters(
                                    messageName,
                                    payloadMap,
                                    processContext,
                                    removeSchemas,
                                    excludeRemoveSchemas,
                                    message.getLombokProperties()));
                }
                //Check inhiritance from schema
                if (message.getImplementsFrom().isEmpty() && isBlank(message.getExtendsFrom())) {
                    if (refObject != null) {
                        Map<String, Object> refMap = castObjectToMap(processContext.getSchemasMap().get(refReplace(refObject)));
                        refMap.forEach((mk, mv) -> {
                            if (mk.equals(EXTENDS)) {
                                String fromClass = prepareExtendsMessage(message, mv);
                                //If the object reference is equal to the fromClass field,
                                //the message will not be filled with data from the schema and the extends keyword will be inserted.
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
        excludeRemoveSchemas.addAll(excludeInheritanceSchemas);
        if (!excludeRemoveSchemas.isEmpty()) {
            excludeRemoveSchemas.forEach(removeSchemas::remove);
        }

        System.out.println("FINISH MAPPING OF MESSAGES! CLEAN UP SCHEMAS: " + removeSchemas);
        removeSchemas.forEach(processContext.getSchemasMap()::remove);
        return messageList;
    }

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
     * @param excludeInheritanceSchemas set of schemas which willn't remove
     * @param message                   message
     * @param payloadMap                payload of message
     * @param refObject                 reference to schema, may be null
     * @param needToFill                here sets flag to determine whether a message needs to be filled with data from the schema
     */
    private static void extendsAndImplFilling(Set<String> excludeInheritanceSchemas,
                                              Message message,
                                              Map<String, Object> payloadMap,
                                              String refObject,
                                              AtomicBoolean needToFill) {
        payloadMap.forEach((mk, mv) -> {
            if (mk.equals(EXTENDS)) {
                String fromClass = prepareExtendsMessage(message, mv);
                //If the object reference is equal to the fromClass field,
                //the message will not be filled with data from the schema and the extends keyword will be inserted.
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
     * @param payload              payload from message
     * @param commonPackage        directory named common for generate schemas(here used for schema-like mapping)
     * @param schemasMap           map of schemas
     * @param removeSchemas        schemas for remove
     * @param excludeRemoveSchemas exclude schemas for remove
     * @param lombokProperties     lombokProperties properties for lombok {@link LombokProperties}
     * @return mapped parameters of message
     */
    private FillParameters getFillParameters(String messageName,
                                             Map<String, Object> payload,
                                             ProcessContext processContext,
                                             Set<String> removeSchemas,
                                             Set<String> excludeRemoveSchemas,
                                             LombokProperties lombokProperties) {
        FillParameters parameters = new FillParameters();
        helper.setIsMappedFromMessages(true);
        helper.setIsMappedFromSchemas(false);
        if (payload.containsKey(LOMBOK)) {
            Map<String, Object> lombokProps = castObjectToMap(payload.get(LOMBOK));
            fillLombokAccessors(lombokProperties, lombokProps);
            fillLombokEqualsAndHashCode(lombokProperties, lombokProps);
        }
        if (getStringValueIfExistOrElseNull(PROPERTIES, payload) != null) {
            System.out.println("Properties Mapping from message");
            Map<String, Object> propertiesMap = castObjectToMap(payload.get(PROPERTIES));
            List<VariableProperties> variableProperties = new LinkedList<>();

            propertiesMap.forEach((propertyName, propertyValue) -> {

                VariableProperties mvp = new VariableProperties();
                Map<String, Object> innerSchemas = new ConcurrentHashMap<>();

                fillProperties(messageName, mvp, payload, payload, propertyName, castObjectToMap(propertyValue), processContext, innerSchemas);

                if (mvp.getItems() != null && !JAVA_DEFAULT_TYPES.contains(mvp.getItems())) {
                    excludeRemoveSchemas.add(mvp.getItems());
                }
                variableProperties.add(mvp);
            });
            return new FillParameters(variableProperties);
        } else {
            //Here we go only if payload filled by one field $ref
            if (getStringValueIfExistOrElseNull(REFERENCE, payload) != null && !filledByRef) {
                filledByRef = true;
                helper.setIsMappedFromMessages(true);
                helper.setIsMappedFromSchemas(false);
                System.out.println("Starting schema-like mapping");
                String schemaName = getStringValueIfExistOrElseNull(REFERENCE, payload).replaceAll(".+/", "");
                Map<String, Object> schema = castObjectToMap(processContext.getSchemasMap().get(schemaName));
                System.out.println("SCHEMA: " + schemaName);

                SchemaMapper schemaMapper = new SchemaMapper(helper);

                Set<String> requiredPropertiesSet = getSetValueIfExistsOrElseEmptySet(REQUIRED, schema);
                Map<String, Object> innerSchemas = new ConcurrentHashMap<>();
                parameters = schemaMapper.getSchemaVariableProperties(schemaName,
                        schema, processContext.getSchemasMap(), castObjectToMap(schema.get(PROPERTIES)), processContext, innerSchemas);
                removeSchemas.add(schemaName);
                if (!innerSchemas.isEmpty()) {
                    innerSchemas.forEach((pk, pv) -> excludeRemoveSchemas.add(pk));
                    processContext.getSchemasMap().putAll(innerSchemas);
                }
                return parameters;
            } else if (filledByRef) {
                return new FillParameters();
            } else {
                throw new RuntimeException("Not correct filled block messages!");
            }
        }
    }
}