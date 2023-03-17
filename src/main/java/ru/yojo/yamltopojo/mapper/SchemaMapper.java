package ru.yojo.yamltopojo.mapper;

import org.springframework.stereotype.Component;
import ru.yojo.yamltopojo.domain.LombokProperties;
import ru.yojo.yamltopojo.domain.Schema;
import ru.yojo.yamltopojo.domain.SchemaProperties;
import ru.yojo.yamltopojo.domain.VariableProperties;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static ru.yojo.yamltopojo.constants.ConstantsEnum.*;
import static ru.yojo.yamltopojo.util.MapperUtil.*;

@Component
public class SchemaMapper {

    public List<Schema> mapSchemasToObjects(Map<String, Object> schemas, LombokProperties lombokProperties) {
        List<Schema> schemaList = new ArrayList<>();
        schemas.forEach((schemaName, schemaValues) -> {
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
            if (schemaType != null && !JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                Schema schema = new Schema();
                schema.setSchemaName(capitalize(schemaName));
                schema.setLombokProperties(lombokProperties);
                schema.setSchemaProperties(getProperties(getSetValueIfExistsOrElseEmptySet(REQUIRED.getValue(), schemaMap), schemas, castObjectToMap(schemaMap.get(PROPERTIES.getValue()))));
                schemaList.add(schema);
            }
        });
        return schemaList;
    }

    private SchemaProperties getProperties(Set<String> requiredAttributes, Map<String, Object> schemas, Map<String, Object> properties) {
        List<VariableProperties> variableProperties = new ArrayList<>();
        properties.forEach((propertyName, propertyValue) -> {
            VariableProperties vp = new VariableProperties();

            fillProperties(vp, requiredAttributes, schemas, propertyName, castObjectToMap(propertyValue));
            variableProperties.add(vp);

        });

        SchemaProperties schemaProperties = new SchemaProperties();
        schemaProperties.setVariableProperties(variableProperties);
        return schemaProperties;

    }

    private void fillProperties(VariableProperties variableProperties, Set<String> requiredAttributes, Map<String, Object> schemas, String propertyName, Map<String, Object> propertiesMap) {
        variableProperties.setName(propertyName);
        variableProperties.setType(capitalize(getStringValueIfExistOrElseNull(TYPE, propertiesMap)));
        variableProperties.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, propertiesMap));
        variableProperties.setExample(getStringValueIfExistOrElseNull(EXAMPLE, propertiesMap));
        variableProperties.setFormat(getStringValueIfExistOrElseNull(FORMAT, propertiesMap));
        variableProperties.setPattern(getStringValueIfExistOrElseNull(PATTERN, propertiesMap));
        variableProperties.setMinMaxLength(getStringValueIfExistOrElseNull(MIN_LENGTH, propertiesMap), getStringValueIfExistOrElseNull(MAX_LENGTH, propertiesMap));

        if (ARRAY.getValue().equals(uncapitalize(variableProperties.getType()))) {
            variableProperties.setItems(refReplace(propertiesMap.get(ITEMS.getValue()).toString().replaceFirst(".$", "")));
            variableProperties.setType(String.format(refReplace(LIST_TYPE.getValue()), variableProperties.getItems()));
        } else if (getStringValueIfExistOrElseNull(REFERENCE, propertiesMap) != null && !ARRAY.getValue().equals(uncapitalize(variableProperties.getType()))) {
            String referenceObject = propertiesMap.get(REFERENCE.getValue()).toString();
            Map<String, Object> stringObjectMap = castObjectToMap(schemas.get(referenceObject.replaceAll(".+/", "")));
            String objectType = getStringValueIfExistOrElseNull(TYPE, stringObjectMap);
            if (JAVA_DEFAULT_TYPES.contains(capitalize(objectType))) {
                fillProperties(variableProperties, requiredAttributes, schemas, propertyName, stringObjectMap);
            }
            if (variableProperties.getType() == null) {
                variableProperties.setType(refReplace(referenceObject));
            }
        }
        Set<String> annotationSet = new HashSet<>();
        Set<String> importSet = new HashSet<>();
        requiredAttributes.forEach(requiredAttribute -> {
            if (requiredAttribute.contains(propertyName)) {
                if (variableProperties.getItems() != null) {
                    importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(NOT_EMPTY_ANNOTATION.getValue()));
                    annotationSet.add(NOT_EMPTY_ANNOTATION.getValue());
                } else {
                    String annotation = JAVA_TYPES_REQUIRED_ANNOTATIONS.get(variableProperties.getType());
                    importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(annotation));
                    annotationSet.add(annotation);
                }
            }
            if (variableProperties.getType() != null && !JAVA_DEFAULT_TYPES.contains(variableProperties.getType())) {
                importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION.getValue()));
                annotationSet.add(VALID_ANNOTATION.getValue());
            }
        });
        if (variableProperties.getType() != null && !JAVA_DEFAULT_TYPES.contains(variableProperties.getType())) {
            annotationSet.add(VALID_ANNOTATION.getValue());
        }
        variableProperties.getRequiredImports().addAll(importSet);
        variableProperties.getAnnotationSet().addAll(annotationSet);
    }
}
