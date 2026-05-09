package ru.yojo.codegen.generator.code;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * Abstract base class for Java source code generators.
 * Provides common methods for building package declarations, imports, JavaDoc, and annotations.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
abstract class AbstractCodeGenerator {

    /**
     * Builds the package declaration line.
     *
     * @param packageName full package with trailing semicolon (e.g., {@code "com.example.common;"})
     * @param description optional JavaDoc description (may be {@code null})
     * @return StringBuilder with package declaration and optional JavaDoc
     */
    protected StringBuilder finishBuild(StringBuilder content, Set<String> imports, String packageName, String description) {
        StringBuilder result = new StringBuilder();
        
        // Add imports including @Generated
        imports.add(JAVAX_GENERATED_IMPORT);
        
        // Package declaration
        if (packageName != null && !packageName.trim().isEmpty()) {
            result.append("package ").append(packageName).append(lineSeparator());
            result.append(lineSeparator());
        }
        
        // Imports (sorted)
        imports.stream()
                .sorted()
                .distinct()
                .filter(i -> i != null && !i.trim().isEmpty())
                .forEach(i -> result.append(IMPORT).append(i).append(lineSeparator()));
        
        result.append(lineSeparator());
        
        // Class-level JavaDoc first (before annotations)
        if (description != null && !description.trim().isEmpty()) {
            generateClassJavaDoc(result, description);
        }
        
        // @Generated annotation (between JavaDoc and other annotations)
        result.append(GENERATED_ANNOTATION).append(lineSeparator());
        
        result.append(content);
        result.append(lineSeparator()).append("}");
        return result;
    }

    /**
     * Generates class-level JavaDoc.
     *
     * @param sb         target StringBuilder
     * @param description description text
     */
    protected void generateClassJavaDoc(StringBuilder sb, String description) {
        sb.append(JAVA_DOC_CLASS_START).append(lineSeparator());
        sb.append(String.format(JAVA_DOC_CLASS_LINE, description)).append(lineSeparator());
        sb.append(JAVA_DOC_CLASS_END).append(lineSeparator());
    }

    /**
     * Generates field-level JavaDoc.
     *
     * @param sb      target StringBuilder
     * @param description field description
     * @param example     optional example value
     */
    protected void generateFieldJavaDoc(StringBuilder sb, String description, String example) {
        if (description == null && example == null) return;
        
        sb.append(JAVA_DOC_START).append(lineSeparator());
        if (description != null && !description.trim().isEmpty()) {
            sb.append(String.format(JAVA_DOC_LINE, description)).append(lineSeparator());
        }
        if (example != null && !example.trim().isEmpty()) {
            sb.append(String.format(JAVA_DOC_EXAMPLE, example)).append(lineSeparator());
        }
        sb.append(JAVA_DOC_END).append(lineSeparator());
    }

    /**
     * Generates getter method.
     *
     * @param type field type
     * @param name field name
     * @return getter method source code
     */
    protected String generateGetter(String type, String name) {
        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);
        return String.format(GETTER, type, capitalized, name);
    }

    /**
     * Generates setter method.
     *
     * @param type field type
     * @param name field name
     * @return setter method source code
     */
    protected String generateSetter(String type, String name) {
        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);
        return String.format(SETTER, capitalized, type, name, name, name);
    }

    /**
     * Builds Lombok annotations based on LombokProperties.
     *
     * @param props      Lombok configuration
     * @param imports    import set to populate
     * @param sb         target StringBuilder for annotations
     */
    protected void buildLombokAnnotations(LombokProperties props, Set<String> imports, StringBuilder sb) {
        if (props == null) return;
        
        if (props.noArgsConstructor()) {
            sb.append(LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_NO_ARGS_CONSTRUCTOR_IMPORT);
        }
        
        if (props.getAccessors() != null && props.getAccessors().isEnable()) {
            boolean fluent = props.getAccessors().isFluent();
            boolean chain = props.getAccessors().isChain();
            if (fluent && chain) {
                sb.append(String.format(LOMBOK_ACCESSORS_ANNOTATION, "fluent = true, chain = true", ""));
            } else if (fluent) {
                sb.append(String.format(LOMBOK_ACCESSORS_ANNOTATION, "fluent = true", ""));
            } else if (chain) {
                sb.append(String.format(LOMBOK_ACCESSORS_ANNOTATION, "chain = true", ""));
            } else {
                sb.append(LOMBOK_ACCESSORS_EMPTY_ANNOTATION);
            }
            sb.append(lineSeparator());
            imports.add(LOMBOK_ACCESSORS_IMPORT);
        }
        
        if (props.allArgsConstructor()) {
            sb.append(LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT);
        }
        
        if (props.getEqualsAndHashCode() != null && props.getEqualsAndHashCode().isEnable()) {
            Boolean callSuper = props.getEqualsAndHashCode().getCallSuper();
            if (callSuper != null) {
                if (callSuper) {
                    sb.append(EQUALS_AND_HASH_CODE_CALL_SUPER_TRUE_ANNOTATION).append(lineSeparator());
                } else {
                    sb.append(EQUALS_AND_HASH_CODE_CALL_SUPER_FALSE_ANNOTATION).append(lineSeparator());
                }
            } else {
                sb.append(EQUALS_AND_HASH_CODE_ANNOTATION).append(lineSeparator());
            }
            imports.add(LOMBOK_EQUALS_AND_HASH_CODE_IMPORT);
        }
    }

    /**
     * Prepares the class declaration part (before fields).
     *
     * @param imports         import set to populate
     * @param implementsFrom set of interfaces to implement
     * @param extendsFrom   superclass name (or {@code null})
     * @param className      class name
     * @param importSet      custom imports from domain object
     * @param fillParameters fill parameters (unused here, kept for compatibility)
     * @return StringBuilder with class declaration
     */
    protected StringBuilder prepareStringBuilder(Set<String> imports, Set<String> implementsFrom, 
                                                  String extendsFrom, String className, Set<String> importSet,
                                                  FillParameters fillParameters) {
        StringBuilder sb = new StringBuilder();
        
        // Add custom imports
        if (importSet != null) {
            imports.addAll(importSet);
        }
        
        // Class declaration
        sb.append(PUBLIC_CLASS).append(className);
        
        // Extends
        if (extendsFrom != null && !extendsFrom.trim().isEmpty()) {
            sb.append(" extends ").append(extendsFrom);
        }
        
        // Implements
        if (implementsFrom != null && !implementsFrom.isEmpty()) {
            sb.append(" implements ");
            // Sort interfaces for consistent order
            List<String> sortedInterfaces = new ArrayList<>(implementsFrom);
            Collections.sort(sortedInterfaces);
            int count = 0;
            for (String ifc : sortedInterfaces) {
                if (count > 0) sb.append(",");
                sb.append(ifc);
                count++;
            }
        }
        
        sb.append(" {").append(lineSeparator());
        return sb;
    }

    /**
     * Generates interface declaration.
     *
     * @param interfaceName interface name
     * @return StringBuilder with interface declaration
     */
    protected StringBuilder getInterfaceBuilder(String interfaceName) {
        StringBuilder sb = new StringBuilder();
        sb.append(PUBLIC_INTERFACE).append(interfaceName).append(" {");
        return sb;
    }

    /**
     * Generates enum class declaration.
     *
     * @param enumName enum name
     * @return StringBuilder with enum declaration
     */
    protected StringBuilder getEnumClassBuilder(String enumName) {
        StringBuilder sb = new StringBuilder();
        sb.append(PUBLIC_ENUM).append(enumName).append(" {");
        return sb;
    }
}
