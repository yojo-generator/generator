package ru.yojo.codegen.mapper;

import org.springframework.stereotype.Component;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.MessageImplementationProperties;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.message.Message;
import ru.yojo.codegen.domain.message.MessageProperties;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.yojo.codegen.constants.ConstantsEnum.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
@Component
public class MessageMapper {

    public List<Message> mapMessagesToObjects(Map<String, Object> messages, Map<String, Object> schemasMap, LombokProperties lombokProperties, String messagePackage, String commonPackage, MessageImplementationProperties messageImplementationProperties) {
        List<Message> messageList = new ArrayList<>();
        Set<String> removeSchemas = new HashSet<>();
        Set<String> excludeRemoveSchemas = new HashSet<>();
        messages.forEach((messageName, messageValues) -> {
            System.out.println("START MAPPING OF MESSAGE: " + messageName);
            Map<String, Object> messageMap = castObjectToMap(messageValues);
            Message message = new Message();
            message.setMessageName(capitalize(messageName));
            message.setLombokProperties(lombokProperties);
            message.setMessageImplementationProperties(messageImplementationProperties);

            Set<String> extendingSet = new HashSet<>();
            messageMap.forEach((mk, mv) -> {
                if (mk.equals(TAGS.getValue())) {
                    ArrayList<HashMap<String, String>> arrayList = (ArrayList) mv;
                    arrayList.stream().forEach(map -> {
                        map.entrySet().stream()
                                .forEach(entryValue -> {
                                    if (entryValue.getValue().startsWith("extends")) {
                                        extendingSet.add(entryValue.getValue());
                                    }
                                });
                    });
                }
            });

            if (!extendingSet.isEmpty()) {
                System.out.println("SHOULD EXTEND: " + extendingSet);
            }
            if (!extendingSet.isEmpty()) {
                if (extendingSet.size() > 1) {
                    String result = null;
                    List<String> collect = extendingSet.stream().collect(Collectors.toList());
                    for (int i = 0; i < collect.size(); i++) {
                        if (i == 0) {
                            result = collect.get(i);
                        } else {
                            result = result + collect.get(i).replace("extends ", ", ");
                        }
                    }
                } else {
                    message.setExtendsFrom(extendingSet.stream().findFirst().get());
                }
                message.setMessageProperties(new MessageProperties(new FillParameters(new ArrayList<>())));
            } else {
                message.setMessageProperties(
                        getProperties(
                                messageMap,
                                commonPackage,
                                schemasMap,
                                removeSchemas,
                                excludeRemoveSchemas)
                );
            }

            message.setMessagePackageName(messagePackage);
            message.setCommonPackageName(commonPackage);
            messageList.add(message);
        });
        if (!excludeRemoveSchemas.isEmpty()) {
            excludeRemoveSchemas.forEach(removeSchemas::remove);
        }

        System.out.println("FINISH MAPPING OF MESSAGES! CLEAN UP SCHEMAS: " + removeSchemas);
        removeSchemas.forEach(schemasMap::remove);
        return messageList;
    }

    private MessageProperties getProperties(Map<String, Object> messageMap,
                                            String commonPackage,
                                            Map<String, Object> schemasMap,
                                            Set<String> removeSchemas,
                                            Set<String> excludeRemoveSchemas) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setName(getStringValueIfExistOrElseNull(NAME, messageMap));
        messageProperties.setTitle(getStringValueIfExistOrElseNull(TITLE, messageMap));
        messageProperties.setSummary(getStringValueIfExistOrElseNull(SUMMARY, messageMap));
        messageProperties.setPayload(
                getPayload(
                        castObjectToMap(messageMap.get(PAYLOAD.getValue())),
                        commonPackage,
                        schemasMap,
                        removeSchemas,
                        excludeRemoveSchemas
                )
        );

        return messageProperties;
    }

    private FillParameters getPayload(Map<String, Object> payload, String commonPackage, Map<String, Object> schemasMap, Set<String> removeSchemas, Set<String> excludeRemoveSchemas) {
        if (getStringValueIfExistOrElseNull(PROPERTIES, payload) != null) {
            System.out.println("Properties Mapping from message");
            Map<String, Object> propertiesMap = castObjectToMap(payload.get(PROPERTIES.getValue()));
            List<VariableProperties> variableProperties = new ArrayList<>();
            Set<String> requiredPropertiesSet = getSetValueIfExistsOrElseEmptySet(REQUIRED.getValue(), payload);

            System.out.println("REQUIRED ATTRIBUTES: " + requiredPropertiesSet);
            propertiesMap.forEach((propertyName, propertyValue) -> {

                VariableProperties mvp = new VariableProperties();
                Map<String, Object> innerSchemas = new ConcurrentHashMap<>();

                fillProperties(mvp, requiredPropertiesSet, payload, propertyName, castObjectToMap(propertyValue), commonPackage, innerSchemas);

                if (mvp.getItems() != null && !JAVA_DEFAULT_TYPES.contains(mvp.getItems())) {
                    excludeRemoveSchemas.add(mvp.getItems());
                }
                variableProperties.add(mvp);
            });
            return new FillParameters(variableProperties);
        } else {
            //Here we go only if payload filled by one field $ref
            if (getStringValueIfExistOrElseNull(REFERENCE, payload) != null) {
                System.out.println("Starting schema-like mapping");
                String schemaName = getStringValueIfExistOrElseNull(REFERENCE, payload).replaceAll(".+/", "");
                Map<String, Object> schema = castObjectToMap(schemasMap.get(schemaName));
                System.out.println("SCHEMA: " + schemaName);

                SchemaMapper schemaMapper = new SchemaMapper();

                Set<String> requiredPropertiesSet = getSetValueIfExistsOrElseEmptySet(REQUIRED.getValue(), schema);
                Map<String, Object> innerSchemas = new ConcurrentHashMap<>();
                FillParameters parameters = schemaMapper.getSchemaVariableProperties(requiredPropertiesSet, schemasMap, castObjectToMap(schema.get(PROPERTIES.getValue())), commonPackage, innerSchemas);
                removeSchemas.add(schemaName);
                if (!innerSchemas.isEmpty()) {
                    //Map<String, Object> innerSchemas2 = new ConcurrentHashMap<>();
                    innerSchemas.forEach((pk,pv) -> excludeRemoveSchemas.add(pk));
                    schemasMap.putAll(innerSchemas);
                    //schemaMapper.getSchemaVariableProperties(requiredPropertiesSet, innerSchemas, castObjectToMap(schema.get(PROPERTIES.getValue())), commonPackage, innerSchemas2);
                }

                return parameters;
            } else {
                throw new RuntimeException("Not correct filled block messages!");
            }
        }
    }
}