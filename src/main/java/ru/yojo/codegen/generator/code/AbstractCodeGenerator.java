package ru.yojo.codegen.generator.code;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;

/**
 * Helper record to hold intermediate builder generation data.
 */
record BuilderFieldInfo(String name, String type, String elementType, boolean isCollection, boolean hasDefault, String defaultValue) {
}

/**
 * Abstract base class for Java source code generators.
 * Provides common methods for building package declarations, imports, JavaDoc, and annotations.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
abstract class AbstractCodeGenerator {

    /**
     * Builds the final source code by wrapping content with package declaration, imports,
     * class JavaDoc, annotations, and class declaration.
     *
     * @param content     the class body content
     * @param imports     set of import strings to include
     * @param packageName full package with trailing semicolon (e.g., {@code "com.example.common;"})
     * @param description optional JavaDoc description (may be {@code null})
     * @return StringBuilder with fully assembled Java source code
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
     * Generates an all-args constructor for a set of fields.
     * Used for final fields without default values.
     *
     * @param className the class name
     * @param fields    list of variable properties to include as constructor parameters
     * @return constructor source code
     */
    protected String generateConstructor(String className, List<VariableProperties> fields) {
        if (fields == null || fields.isEmpty()) return "";

        StringBuilder params = new StringBuilder();
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            VariableProperties vp = fields.get(i);
            if (i > 0) {
                params.append(", ");
            }
            params.append(vp.getType()).append(" ").append(vp.getName());
            body.append(String.format(CONSTRUCTOR_ASSIGNMENT, vp.getName(), vp.getName()));
            body.append(System.lineSeparator());
        }

        return String.format(CONSTRUCTOR, className, params.toString(), body.toString());
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
     * Generates a manual {@code toString()} method, including the field values in the output.
     * <p>
     * Produces:
     * <pre>{@code
     * @Override
     * public String toString() {
     *     return "ClassName{field1=" + field1 + ", field2=" + field2 + "}";
     * }
     * }</pre>
     *
     * @param className the class name
     * @param fields    list of non-enum variable properties (field definitions)
     * @return the generated toString method source code
     */
    protected String generateToString(String className, List<VariableProperties> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("    @Override").append(lineSeparator());
        sb.append("    public String toString() {").append(lineSeparator());
        sb.append("        return \"").append(className).append("{\" +").append(lineSeparator());

        for (int i = 0; i < fields.size(); i++) {
            VariableProperties vp = fields.get(i);
            String fieldName = vp.getName();
            sb.append("                \"").append(fieldName).append("=\" + ").append(fieldName);
            if (i < fields.size() - 1) {
                sb.append(" + \", \" +").append(lineSeparator());
            } else {
                sb.append(" +").append(lineSeparator());
            }
        }

        sb.append("                \"}\";").append(lineSeparator());
        sb.append("    }");
        return sb.toString();
    }

    /**
     * Generates a manual {@code equals(Object)} method using {@link java.util.Objects#equals(Object, Object)}.
     * <p>
     * Produces:
     * <pre>{@code
     * @Override
     * public boolean equals(Object o) {
     *     if (this == o) return true;
     *     if (o == null || getClass() != o.getClass()) return false;
     *     ClassName that = (ClassName) o;
     *     return Objects.equals(field1, that.field1) &&
     *             Objects.equals(field2, that.field2);
     * }
     * }</pre>
     *
     * @param className the class name
     * @param fields    list of non-enum variable properties
     * @param imports   import set to populate with {@code java.util.Objects}
     * @return the generated equals method source code
     */
    protected String generateEquals(String className, List<VariableProperties> fields, Set<String> imports) {
        imports.add("java.util.Objects;");
        StringBuilder sb = new StringBuilder();
        sb.append("    @Override").append(lineSeparator());
        sb.append("    public boolean equals(Object o) {").append(lineSeparator());
        sb.append("        if (this == o) return true;").append(lineSeparator());
        sb.append("        if (o == null || getClass() != o.getClass()) return false;").append(lineSeparator());
        sb.append("        ").append(className).append(" that = (").append(className).append(") o;").append(lineSeparator());

        if (fields.size() == 1) {
            VariableProperties vp = fields.get(0);
            sb.append("        return Objects.equals(").append(vp.getName()).append(", that.").append(vp.getName()).append(");");
        } else {
            sb.append("        return ");
            for (int i = 0; i < fields.size(); i++) {
                VariableProperties vp = fields.get(i);
                if (i > 0) {
                    sb.append(" &&");
                    if (i < fields.size()) {
                        sb.append(lineSeparator()).append("                ");
                    }
                }
                sb.append("Objects.equals(").append(vp.getName()).append(", that.").append(vp.getName()).append(")");
            }
            sb.append(";");
        }

        sb.append(lineSeparator()).append("    }");
        return sb.toString();
    }

    /**
     * Generates a manual {@code hashCode()} method using {@link java.util.Objects#hash(Object...)}.
     * <p>
     * Produces:
     * <pre>{@code
     * @Override
     * public int hashCode() {
     *     return Objects.hash(field1, field2);
     * }
     * }</pre>
     *
     * @param className the class name (unused, for consistency with other generation methods)
     * @param fields    list of non-enum variable properties
     * @param imports   import set to populate with {@code java.util.Objects}
     * @return the generated hashCode method source code
     */
    protected String generateHashCode(String className, List<VariableProperties> fields, Set<String> imports) {
        imports.add("java.util.Objects;");
        StringBuilder sb = new StringBuilder();
        sb.append("    @Override").append(lineSeparator());
        sb.append("    public int hashCode() {").append(lineSeparator());

        sb.append("        return Objects.hash(");
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(fields.get(i).getName());
        }
        sb.append(");").append(lineSeparator());

        sb.append("    }");
        return sb.toString();
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

        // @Value (immutable DTO) — mutually exclusive with @Data, handled in SchemaCodeGenerator
        if (props.isValue()) {
            sb.append(LOMBOK_VALUE_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_VALUE_IMPORT);
        }

        // @With (wither methods) — typically paired with @Value for immutable modification
        if (props.isWith()) {
            sb.append(LOMBOK_WITH_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_WITH_IMPORT);
        }

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

        // @Getter (standalone, not via @Data)
        if (props.isGetter()) {
            sb.append(LOMBOK_GETTER_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_GETTER_IMPORT);
        }

        // @Setter (standalone, not via @Data) — skipped when @Value is active
        if (props.isSetter() && !props.isValue()) {
            sb.append(LOMBOK_SETTER_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_SETTER_IMPORT);
        }

        // @ToString (standalone, not via @Data)
        if (props.isToString()) {
            sb.append(LOMBOK_TO_STRING_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_TO_STRING_IMPORT);
        }

        if (props.allArgsConstructor()) {
            sb.append(LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT);
        }

        // @RequiredArgsConstructor — standalone constructor for required/final fields
        if (props.isRequiredArgsConstructor()) {
            sb.append(LOMBOK_REQUIRED_ARGS_CONSTRUCTOR_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_REQUIRED_ARGS_CONSTRUCTOR_IMPORT);
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

        if (props.getBuilder() != null && props.getBuilder().isEnable()) {
            sb.append(LOMBOK_BUILDER_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_BUILDER_IMPORT);

            if (props.getBuilder().isBuilderDefault()) {
                imports.add(LOMBOK_BUILDER_DEFAULT_IMPORT);
            }
            if (props.getBuilder().isSingular()) {
                imports.add(LOMBOK_SINGULAR_IMPORT);
            }
        }

        // @Slf4j — logger field
        if (props.isSlf4j()) {
            sb.append(LOMBOK_SLF4J_ANNOTATION).append(lineSeparator());
            imports.add(LOMBOK_SLF4J_IMPORT);
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

    /**
     * Checks if the given type name represents a collection type (List or Set).
     *
     * @param type the Java type string (e.g., "List&lt;String&gt;", "Set&lt;Integer&gt;")
     * @return {@code true} if the type starts with "List" or "Set"
     */
    protected static boolean isCollectionType(String type) {
        return type != null && (type.startsWith("List") || type.startsWith("Set"));
    }

    /**
     * Generates the manual builder parts when Lombok is disabled but builder is enabled.
     * Produces:
     * <ul>
     *   <li>Private constructor accepting {@code Builder}</li>
     *   <li>Static {@code builder()} factory method</li>
     *   <li>Public static inner {@code Builder} class with fluent setters, singular adders, and {@code build()}</li>
     * </ul>
     *
     * @param className     the outer class name
     * @param fields        the field definitions
     * @param imports       import set to populate with required imports (List, Set, ArrayList, HashSet)
     * @param stringBuilder target StringBuilder to append to
     */
    protected void generateManualBuilder(String className, List<VariableProperties> fields, Set<String> imports, StringBuilder stringBuilder) {
        if (fields == null || fields.isEmpty()) return;

        // 1. Private constructor taking Builder
        stringBuilder.append(lineSeparator());
        stringBuilder.append(String.format("    private %s(Builder builder) {", className)).append(lineSeparator());
        for (VariableProperties vp : fields) {
            stringBuilder.append("        this.").append(vp.getName()).append(" = builder.").append(vp.getName()).append(";").append(lineSeparator());
        }
        stringBuilder.append("    }").append(lineSeparator());

        // 2. Static builder() method
        stringBuilder.append(lineSeparator());
        stringBuilder.append("    public static Builder builder() {").append(lineSeparator());
        stringBuilder.append("        return new Builder();").append(lineSeparator());
        stringBuilder.append("    }").append(lineSeparator());

        // 3. Builder inner class
        stringBuilder.append(lineSeparator());
        stringBuilder.append("    public static class Builder {").append(lineSeparator());

        // 3a. Builder fields (same as outer, but without 'final', with initialization for collections)
        for (VariableProperties vp : fields) {
            String fieldLine;
            if (isCollectionType(vp.getType())) {
                String initExpr = getCollectionInitExpr(vp.getType());
                if (initExpr != null) {
                    fieldLine = "        private " + vp.getType() + " " + vp.getName() + " = " + initExpr + ";";
                } else {
                    fieldLine = "        private " + vp.getType() + " " + vp.getName() + ";";
                }
            } else if (vp.getDefaultProperty() != null) {
                fieldLine = "        private " + vp.getType() + " " + vp.getName() + " = " + vp.getDefaultProperty() + ";";
            } else {
                fieldLine = "        private " + vp.getType() + " " + vp.getName() + ";";
            }
            stringBuilder.append(fieldLine).append(lineSeparator());
        }

        // 3b. Fluent setters
        for (VariableProperties vp : fields) {
            stringBuilder.append(lineSeparator());
            stringBuilder.append(String.format("        public Builder %s(%s %s) {", vp.getName(), vp.getType(), vp.getName())).append(lineSeparator());
            stringBuilder.append("            this.").append(vp.getName()).append(" = ").append(vp.getName()).append(";").append(lineSeparator());
            stringBuilder.append("            return this;").append(lineSeparator());
            stringBuilder.append("        }").append(lineSeparator());
        }

        // 3c. Singular adders for collection fields
        for (VariableProperties vp : fields) {
            if (isCollectionType(vp.getType())) {
                String elementType = extractElementType(vp.getType());
                String singularName = deriveSingularName(vp.getName());
                stringBuilder.append(lineSeparator());
                stringBuilder.append(String.format("        public Builder %s(%s %s) {", singularName, elementType, singularName)).append(lineSeparator());
                stringBuilder.append("            this.").append(vp.getName()).append(".add(").append(singularName).append(");").append(lineSeparator());
                stringBuilder.append("            return this;").append(lineSeparator());
                stringBuilder.append("        }").append(lineSeparator());
            }
        }

        // 3d. build() method
        stringBuilder.append(lineSeparator());
        stringBuilder.append(String.format("        public %s build() {", className)).append(lineSeparator());
        stringBuilder.append("            return new ").append(className).append("(this);").append(lineSeparator());
        stringBuilder.append("        }").append(lineSeparator());

        stringBuilder.append("    }").append(lineSeparator());

        // 4. Add required imports
        boolean hasList = fields.stream().anyMatch(vp -> vp.getType().startsWith("List"));
        boolean hasSet = fields.stream().anyMatch(vp -> vp.getType().startsWith("Set"));
        if (hasList) imports.add(LIST_IMPORT);
        if (hasSet) imports.add(SET_IMPORT);
        if (hasList || hasSet) {
            imports.add(ARRAY_LIST_IMPORT);
            imports.add(HASH_SET_IMPORT);
        }
    }

    /**
     * Extracts the element type from a generic collection type string.
     * <p>
     * Examples:
     * <ul>
     *   <li>{@code "List&lt;String&gt;" → "String"}</li>
     *   <li>{@code "Set&lt;Integer&gt;" → "Integer"}</li>
     *   <li>{@code "List&lt;Map&lt;String, UUID&gt;&gt;" → "Map&lt;String, UUID&gt;"}</li>
     * </ul>
     *
     * @param type the full generic type (e.g., "List&lt;String&gt;")
     * @return the element type or the original type if parsing fails
     */
    private static String extractElementType(String type) {
        if (type == null) return null;
        int start = type.indexOf('<');
        int end = type.lastIndexOf('>');
        if (start >= 0 && end > start) {
            return type.substring(start + 1, end);
        }
        return type;
    }

    /**
     * Returns the collection initialization expression for the given collection type.
     *
     * @param type the collection type (e.g., "List&lt;String&gt;", "Set&lt;Integer&gt;")
     * @return initialization expression like "new ArrayList&lt;&gt;()" or "new HashSet&lt;&gt;()"
     */
    private static String getCollectionInitExpr(String type) {
        if (type == null) return null;
        if (type.startsWith("List")) return "new ArrayList<>()";
        if (type.startsWith("Set")) return "new HashSet<>()";
        return null;
    }

    /**
     * Derives the singular name for a field by removing a trailing 's' character.
     * This matches Lombok's {@code @Singular} convention.
     * <p>
     * Examples:
     * <ul>
     *   <li>{@code "items" → "item"}</li>
     *   <li>{@code "names" → "name"}</li>
     *   <li>{@code "addresses" → "addresse"}</li>
     *   <li>{@code "status" → "status"} (no trailing 's')</li>
     *   <li>{@code "data" → "data"} (no trailing 's')</li>
     * </ul>
     *
     * @param name the field name
     * @return the singularized name
     */
    protected static String deriveSingularName(String name) {
        if (name == null || name.isEmpty()) return name;
        if (name.endsWith("s") && name.length() > 1) {
            return name.substring(0, name.length() - 1);
        }
        return name;
    }
}
