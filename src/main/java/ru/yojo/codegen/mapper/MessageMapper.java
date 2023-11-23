package ru.yojo.codegen.mapper;

import org.springframework.stereotype.Component;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.message.Message;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
@Component
public class MessageMapper {

    private boolean filledByRef = false;

    public List<Message> mapMessagesToObjects(Map<String, Object> messages, Map<String, Object> schemasMap, LombokProperties lombokProperties, String messagePackage, String commonPackage) {
        List<Message> messageList = new ArrayList<>();
        Set<String> removeSchemas = new HashSet<>();
        Set<String> excludeRemoveSchemas = new HashSet<>();
        Set<String> excludeInheritanceSchemas = new HashSet<>();
        messages.forEach((messageName, messageValues) -> {
            filledByRef = false;
            System.out.println("START MAPPING OF MESSAGE: " + messageName);
            Map<String, Object> messageMap = castObjectToMap(messageValues);
            Message message = new Message();
            message.setMessageName(capitalize(messageName));
            message.setLombokProperties(lombokProperties);

            Map<String, Object> payloadMap = castObjectToMap(messageMap.get(PAYLOAD));
            String refObject = getStringValueIfExistOrElseNull(REFERENCE, payloadMap);
            AtomicBoolean needToFill = new AtomicBoolean(true);
            payloadMap.forEach((mk, mv) -> {
                if (mk.equals(EXTENDS)) {
                    Map<String, Object> extendsMap = castObjectToMap(mv);
                    String fromClass = getStringValueIfExistOrElseNull(FROM_CLASS, extendsMap);
                    String fromPackage = getStringValueIfExistOrElseNull(FROM_PACKAGE, extendsMap);
                    System.out.println("SHOULD EXTENDS FROM: " + fromClass);
                    message.setExtendsFrom(fromClass);
                    message.getImportSet().add(fromPackage + "." + fromClass + ";");
                    if (refObject != null && refReplace(refObject).equals(fromClass)) {
                        needToFill.set(false);
                        excludeInheritanceSchemas.add(refReplace(refObject));
                    }
                }
                if (mk.equals(IMPLEMENTS)) {
                    Map<String, Object> implementsMap = castObjectToMap(mv);
                    List<String> fromInterfaceList = castObjectToList(implementsMap.get(FROM_INTERFACE));
                    System.out.println("SHOULD IMPLEMENTS FROM: " + fromInterfaceList);
                    fromInterfaceList.forEach(ifc -> {
                        String[] split = ifc.split("[.]");
                        message.getImplementsFrom().add(split[split.length - 1]);
                        message.getImportSet().add(ifc + ";");
                    });
                }
            });

            if (needToFill.get()) {
                filledByRef = false;
                message.setFillParameters(
                        getFillParameters(
                                payloadMap,
                                commonPackage,
                                schemasMap,
                                removeSchemas,
                                excludeRemoveSchemas
                        )
                );
                if (refObject != null) {
                    payloadMap.remove(PROPERTIES);
                    message.enrichFillParameters(
                            getFillParameters(
                                    payloadMap,
                                    commonPackage,
                                    schemasMap,
                                    removeSchemas,
                                    excludeRemoveSchemas));
                }
                //Check from schema
                if (message.getImplementsFrom().isEmpty() && isBlank(message.getExtendsFrom())) {
                    if (refObject != null) {
                        Map<String, Object> refMap = castObjectToMap(schemasMap.get(refReplace(refObject)));
                        refMap.forEach((mk, mv) -> {
                            if (mk.equals(EXTENDS)) {
                                Map<String, Object> extendsMap = castObjectToMap(mv);
                                String fromClass = getStringValueIfExistOrElseNull(FROM_CLASS, extendsMap);
                                String fromPackage = getStringValueIfExistOrElseNull(FROM_PACKAGE, extendsMap);
                                System.out.println("SHOULD EXTENDS FROM: " + fromClass);
                                message.setExtendsFrom(fromClass);
                                message.getImportSet().add(fromPackage + "." + fromClass + ";");
                                if (refObject != null && refReplace(refObject).equals(fromClass)) {
                                    needToFill.set(false);
                                }
                            }
                            if (mk.equals(IMPLEMENTS)) {
                                Map<String, Object> implementsMap = castObjectToMap(mv);
                                List<String> fromInterfaceList = castObjectToList(implementsMap.get(FROM_INTERFACE));
                                System.out.println("SHOULD IMPLEMENTS FROM: " + fromInterfaceList);
                                fromInterfaceList.forEach(ifc -> {
                                    String[] split = ifc.split("[.]");
                                    message.getImplementsFrom().add(split[split.length - 1]);
                                    message.getImportSet().add(ifc + ";");
                                });
                            }
                        });
                    }
                }
            } else {
                message.setFillParameters(new FillParameters(new ArrayList<>()));
            }

            message.setSummary(getStringValueIfExistOrElseNull(SUMMARY, messageMap));
            message.setMessagePackageName(messagePackage);
            message.setCommonPackageName(commonPackage);
            messageList.add(message);
        });
        excludeRemoveSchemas.addAll(excludeInheritanceSchemas);
        if (!excludeRemoveSchemas.isEmpty()) {
            excludeRemoveSchemas.forEach(removeSchemas::remove);
        }

        System.out.println("FINISH MAPPING OF MESSAGES! CLEAN UP SCHEMAS: " + removeSchemas);
        removeSchemas.forEach(schemasMap::remove);
        return messageList;
    }

    private FillParameters getFillParameters(Map<String, Object> payload, String commonPackage, Map<String, Object> schemasMap, Set<String> removeSchemas, Set<String> excludeRemoveSchemas) {
        FillParameters parameters = new FillParameters();
        if (getStringValueIfExistOrElseNull(PROPERTIES, payload) != null) {
            System.out.println("Properties Mapping from message");
            Map<String, Object> propertiesMap = castObjectToMap(payload.get(PROPERTIES));
            List<VariableProperties> variableProperties = new LinkedList<>();

            propertiesMap.forEach((propertyName, propertyValue) -> {

                VariableProperties mvp = new VariableProperties();
                Map<String, Object> innerSchemas = new LinkedHashMap<>();

                fillProperties(mvp, payload, payload, propertyName, castObjectToMap(propertyValue), commonPackage, innerSchemas);

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
                System.out.println("Starting schema-like mapping");
                String schemaName = getStringValueIfExistOrElseNull(REFERENCE, payload).replaceAll(".+/", "");
                Map<String, Object> schema = castObjectToMap(schemasMap.get(schemaName));
                System.out.println("SCHEMA: " + schemaName);

                SchemaMapper schemaMapper = new SchemaMapper();

                Set<String> requiredPropertiesSet = getSetValueIfExistsOrElseEmptySet(REQUIRED, schema);
                Map<String, Object> innerSchemas = new LinkedHashMap<>();
                parameters = schemaMapper.getSchemaVariableProperties(schemaName, schema, schemasMap, castObjectToMap(schema.get(PROPERTIES)), commonPackage, innerSchemas);
                removeSchemas.add(schemaName);
                if (!innerSchemas.isEmpty()) {
                    innerSchemas.forEach((pk, pv) -> excludeRemoveSchemas.add(pk));
                    schemasMap.putAll(innerSchemas);
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