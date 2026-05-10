package ru.yojo.codegen.mapper;

import ru.yojo.codegen.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yojo.codegen.constants.Dictionary.OBJECT_TYPE;
import static ru.yojo.codegen.constants.Dictionary.PROPERTIES;
import static ru.yojo.codegen.constants.Dictionary.TYPE;
import static ru.yojo.codegen.util.MapperUtil.capitalize;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.refReplace;

/**
 * Handles polymorphic types (oneOf, allOf, anyOf in YAML).
 * Merges properties from multiple schemas and creates appropriate inner schemas.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class PolymorphicTypeHandler implements PropertyTypeHandler {

    private static final Logger LOG = new Logger(PolymorphicTypeHandler.class);

    private final AbstractMapper abstractMapper;

    /**
     * Creates a handler for polymorphic types (oneOf, allOf, anyOf).
     *
     * @param abstractMapper parent mapper for delegating type filling
     */
    public PolymorphicTypeHandler(AbstractMapper abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        return ctx.variableProperties().isPolymorph();
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        var variableProperties = ctx.variableProperties();
        var propertiesMap = ctx.propertiesMap();
        var schemas = ctx.schemas();
        var processContext = ctx.processContext();
        var innerSchemas = ctx.innerSchemas();

        LOG.info("FOUND POLYMORPHISM INSIDE SCHEMA! Schema: " + variableProperties.getName());

        List<Object> polymorphList = AbstractMapper.collectPolymorphRefs(propertiesMap);

        Map<String, Object> mergedProperties = polymorphList.stream()
                .flatMap(ref -> {
                    String refStr = ref.toString();
                    if (ref instanceof Map) {
                        Map<?, ?> mapRef = (Map<?, ?>) ref;
                        Object r = mapRef.get("$ref");
                        if (r != null) refStr = r.toString();
                    }
                    return castObjectToMap(schemas.get(refReplace(refStr))).entrySet().stream();
                })
                .filter(en -> en.getKey().equals(PROPERTIES))
                .map(pr -> castObjectToMap(pr.getValue()))
                .flatMap(map -> map.entrySet().stream())
                .distinct()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing
                ));

        String className = capitalize(ctx.propertyName());
        for (Object item : polymorphList) {
            String refName;
            if (item instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) item;
                Object refObj = m.get("$ref");
                refName = refObj != null ? refReplace(refObj.toString()) : "Unknown";
            } else {
                refName = refReplace(item.toString());
            }
            className += refName;
        }

        variableProperties.setType(className);
        variableProperties.addRequiredImports(abstractMapper.prepareImport(processContext, className));

        Map<String, Object> preparedMergedPolymorphSchema = Map.of(
                className,
                Map.of(TYPE, OBJECT_TYPE, PROPERTIES, mergedProperties)
        );

        innerSchemas.putAll(preparedMergedPolymorphSchema);
    }
}
