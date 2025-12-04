package ru.yojo.codegen.util;

import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.EqualsAndHashCode;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.Map;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * Utility class for parsing and populating Lombok configuration from AsyncAPI contract.
 * <p>
 * Extracts Lombok-related attributes (e.g., {@code accessors}, {@code equalsAndHashCode},
 * {@code allArgsConstructor}) from YAML and applies them to {@link LombokProperties}.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class LombokUtils {

    /**
     * Populates {@link Accessors} configuration from the {@code accessors} section of Lombok props.
     * <p>
     * Supports:
     * <ul>
     *   <li>{@code fluent: true|false}</li>
     *   <li>{@code chain: true|false}</li>
     *   <li>{@code enable: true|false} (defaults to {@code true} if section present)</li>
     * </ul>
     *
     * @param lombokProperties target Lombok config to update
     * @param lombokProps      parsed {@code lombok} map from YAML
     */
    public static void fillLombokAccessors(LombokProperties lombokProperties, Map<String, Object> lombokProps) {
        if (lombokProps.containsKey(ACCESSORS)) {
            Accessors acc = new Accessors(true, false, false);
            Map<String, Object> accessors = castObjectToMap(lombokProps.get(ACCESSORS));
            if (accessors.containsKey(FLUENT)) {
                acc.setFluent(Boolean.valueOf(accessors.get(FLUENT).toString()));
            }
            if (accessors.containsKey(CHAIN)) {
                acc.setChain(Boolean.valueOf(accessors.get(CHAIN).toString()));
            }
            if (accessors.containsKey(ENABLE)) {
                acc.setEnable(Boolean.valueOf(accessors.get(ENABLE).toString()));
            }
            lombokProperties.setAccessors(acc);
        }
    }

    /**
     * Populates {@link EqualsAndHashCode} configuration from the {@code equalsAndHashCode} section.
     * <p>
     * Supports:
     * <ul>
     *   <li>Presence of section → enables {@code @EqualsAndHashCode}</li>
     *   <li>{@code callSuper: true|false} → controls {@code callSuper} attribute</li>
     * </ul>
     *
     * @param lombokProperties target Lombok config to update
     * @param lombokProps      parsed {@code lombok} map from YAML
     */
    public static void fillLombokEqualsAndHashCode(LombokProperties lombokProperties, Map<String, Object> lombokProps) {
        if (lombokProps.containsKey(EQUALS_AND_HASH_CODE)) {
            EqualsAndHashCode equalsAndHashCode = new EqualsAndHashCode();
            Map<String, Object> eah = castObjectToMap(lombokProps.get(EQUALS_AND_HASH_CODE));
            if (eah != null) {
                if (getStringValueIfExistOrElseNull(CALL_SUPER, eah) != null) {
                    equalsAndHashCode.setCallSuper(Boolean.valueOf(getStringValueIfExistOrElseNull(CALL_SUPER, eah)));
                }
                equalsAndHashCode.setEnable(true);
            }
            lombokProperties.setEqualsAndHashCode(equalsAndHashCode);
        }
    }

    /**
     * Populates constructor-related Lombok settings: {@code @AllArgsConstructor} and {@code @NoArgsConstructor}.
     * <p>
     * Reads boolean flags {@code allArgsConstructor} and {@code noArgsConstructor} from the config.
     *
     * @param lombokProperties target Lombok config to update
     * @param lombokProps      parsed {@code lombok} map from YAML
     */
    public static void fillLombokConstructors(LombokProperties lombokProperties, Map<String, Object> lombokProps) {
        if (lombokProps.containsKey(ALL_ARGS) || lombokProps.containsKey(NO_ARGS)) {
            String allArgs = getStringValueIfExistOrElseNull(ALL_ARGS, lombokProps);
            String noArgs = getStringValueIfExistOrElseNull(NO_ARGS, lombokProps);
            if (allArgs != null) {
                lombokProperties.setAllArgsConstructor(Boolean.valueOf(allArgs));
            }
            if (noArgs != null) {
                lombokProperties.setNoArgsConstructor(Boolean.valueOf(noArgs));
            }
        }
    }
}