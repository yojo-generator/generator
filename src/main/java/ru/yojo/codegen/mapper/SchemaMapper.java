package ru.yojo.codegen.mapper;

import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.Schema;
import ru.yojo.codegen.domain.SchemaProperties;
import ru.yojo.codegen.domain.SchemaVariableProperties;

import java.util.*;

import static ru.yojo.codegen.constants.ConstantsEnum.*;
import static ru.yojo.codegen.util.MapperUtil.*;


public class SchemaMapper {

    public List<Schema> mapSchemasToObjects(Map<String, Object> schemas, LombokProperties lombokProperties, String commonPackage) {
        List<Schema> schemaList = new ArrayList<>();
        schemas.forEach((schemaName, schemaValues) -> {
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
            if (schemaType != null && !JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                Schema schema = new Schema();
                schema.setSchemaName(capitalize(schemaName));
                schema.setLombokProperties(lombokProperties);
                schema.setPackageName(commonPackage);
                schema.setSchemaProperties(getProperties(getSetValueIfExistsOrElseEmptySet(REQUIRED.getValue(), schemaMap), schemas, castObjectToMap(schemaMap.get(PROPERTIES.getValue()))));
                schemaList.add(schema);
            }
        });
        return schemaList;
    }

    private SchemaProperties getProperties(Set<String> requiredAttributes, Map<String, Object> schemas, Map<String, Object> properties) {
        List<SchemaVariableProperties> schemaVariableProperties = new ArrayList<>();

        properties.forEach((propertyName, propertyValue) -> {
            SchemaVariableProperties vp = new SchemaVariableProperties();
            fillProperties(vp, requiredAttributes, schemas, propertyName, castObjectToMap(propertyValue));
            schemaVariableProperties.add(vp);
        });

        SchemaProperties schemaProperties = new SchemaProperties();
        schemaProperties.setVariableProperties(schemaVariableProperties);
        return schemaProperties;

    }

    private void fillProperties(SchemaVariableProperties schemaVariableProperties, Set<String> requiredAttributes, Map<String, Object> schemas, String propertyName, Map<String, Object> propertiesMap) {
        schemaVariableProperties.setName(propertyName);
        schemaVariableProperties.setType(capitalize(getStringValueIfExistOrElseNull(TYPE, propertiesMap)));
        schemaVariableProperties.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, propertiesMap));
        schemaVariableProperties.setExample(getStringValueIfExistOrElseNull(EXAMPLE, propertiesMap));
        schemaVariableProperties.setFormat(getStringValueIfExistOrElseNull(FORMAT, propertiesMap));
        schemaVariableProperties.setPattern(getStringValueIfExistOrElseNull(PATTERN, propertiesMap));
        schemaVariableProperties.setMinMaxLength(getStringValueIfExistOrElseNull(MIN_LENGTH, propertiesMap), getStringValueIfExistOrElseNull(MAX_LENGTH, propertiesMap));

        if (ARRAY.getValue().equals(uncapitalize(schemaVariableProperties.getType()))) {
            schemaVariableProperties.setItems(refReplace(propertiesMap.get(ITEMS.getValue()).toString().replaceFirst(".$", "")));
            schemaVariableProperties.setType(String.format(refReplace(LIST_TYPE.getValue()), schemaVariableProperties.getItems()));
        } else if (getStringValueIfExistOrElseNull(REFERENCE, propertiesMap) != null && !ARRAY.getValue().equals(uncapitalize(schemaVariableProperties.getType()))) {
            String referenceObject = propertiesMap.get(REFERENCE.getValue()).toString();
            Map<String, Object> stringObjectMap = castObjectToMap(schemas.get(referenceObject.replaceAll(".+/", "")));
            String objectType = getStringValueIfExistOrElseNull(TYPE, stringObjectMap);
            if (JAVA_DEFAULT_TYPES.contains(capitalize(objectType))) {
                fillProperties(schemaVariableProperties, requiredAttributes, schemas, propertyName, stringObjectMap);
            }
            if (schemaVariableProperties.getType() == null || capitalize(OBJECT.getValue()).equals(schemaVariableProperties.getType())) {
                schemaVariableProperties.setType(refReplace(referenceObject));
            }
        }
        Set<String> annotationSet = new HashSet<>();
        Set<String> importSet = new HashSet<>();
        requiredAttributes.forEach(requiredAttribute -> {
            if (requiredAttribute.contains(propertyName)) {
                if (schemaVariableProperties.getItems() != null) {
                    importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(NOT_EMPTY_ANNOTATION.getValue()));
                    annotationSet.add(NOT_EMPTY_ANNOTATION.getValue());
                } else {
                    String annotation = JAVA_TYPES_REQUIRED_ANNOTATIONS.get(schemaVariableProperties.getType());
                    importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(annotation));
                    annotationSet.add(annotation);
                }
            }
            if (schemaVariableProperties.getType() != null && !JAVA_DEFAULT_TYPES.contains(schemaVariableProperties.getType())) {
                importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION.getValue()));
                annotationSet.add(VALID_ANNOTATION.getValue());
            }
        });
        if (schemaVariableProperties.getType() != null && !JAVA_DEFAULT_TYPES.contains(schemaVariableProperties.getType())) {
            importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION.getValue()));
            annotationSet.add(VALID_ANNOTATION.getValue());
        }
        schemaVariableProperties.getRequiredImports().addAll(importSet);
        schemaVariableProperties.getAnnotationSet().addAll(annotationSet);
    }
}
