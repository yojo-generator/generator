package ru.yojo.codegen.mapper;

import org.springframework.stereotype.Component;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.exception.SchemaFillException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static ru.yojo.codegen.constants.ConstantsEnum.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
@Component
public class SchemaMapper {

    public List<Schema> mapSchemasToObjects(Map<String, Object> schemas,
                                            LombokProperties lombokProperties,
                                            String commonPackage) {
        List<Schema> schemaList = new ArrayList<>();
        Map<String, Object> innerSchemas = new ConcurrentHashMap<>();
        schemas.forEach((schemaName, schemaValues) -> {
            System.out.println("START MAPPING OF SCHEMA " + schemaName);
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
            if (schemaType != null && !JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                Schema schema = new Schema();
                schema.setSchemaName(capitalize(schemaName));
                schema.setLombokProperties(lombokProperties);
                schema.setPackageName(commonPackage);
                schema.setFillParameters(getSchemaVariableProperties(
                        getSetValueIfExistsOrElseEmptySet(REQUIRED.getValue(), schemaMap),
                        schemas,
                        castObjectToMap(schemaMap.get(PROPERTIES.getValue())), commonPackage, innerSchemas)
                );
                schemaList.add(schema);
            } else if (schemaType != null && JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                System.out.println("SKIP SCHEMA BECAUSE TYPE IS: " + schemaType);
            } else {
                throw new SchemaFillException("NOT DEFINED TYPE OF SCHEMA! Schema: " + schemaName);
            }
        });
        if (!innerSchemas.isEmpty()) {
            innerSchemas.forEach((schemaName, schemaValues) -> {
                System.out.println("START MAPPING OF INNER SCHEMA: " + schemaName);
                Map<String, Object> schemaMap = castObjectToMap(schemaValues);
                String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
                if (schemaType != null && !JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                    Schema schema = new Schema();
                    schema.setSchemaName(capitalize(schemaName));
                    schema.setLombokProperties(lombokProperties);
                    schema.setPackageName(commonPackage);
                    schema.setFillParameters(getSchemaVariableProperties(
                            getSetValueIfExistsOrElseEmptySet(REQUIRED.getValue(), schemaMap),
                            innerSchemas,
                            castObjectToMap(schemaMap.get(PROPERTIES.getValue())), commonPackage, innerSchemas)
                    );
                    schemaList.add(schema);
                } else {
                    throw new SchemaFillException("NOT DEFINED TYPE OF INNER SCHEMA! Schema: " + schemaName);
                }
            });
        }
        return schemaList;
    }

    public FillParameters getSchemaVariableProperties(Set<String> requiredAttributes, Map<String, Object> schemas, Map<String, Object> properties, String commonPackage, Map<String, Object> innerSchemas) {
        List<VariableProperties> variableProperties = new ArrayList<>();

        if (!requiredAttributes.isEmpty()) {
            System.out.println("REQUIRED ATTRIBUTES: " + requiredAttributes);
        }
        properties.forEach((propertyName, propertyValue) -> {
            VariableProperties vp = new VariableProperties();
            fillProperties(vp, requiredAttributes, schemas, propertyName, castObjectToMap(propertyValue), commonPackage, innerSchemas);
            variableProperties.add(vp);
        });
        return new FillParameters(variableProperties);
    }
}