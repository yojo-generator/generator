package ru.yojo.codegen.util;

import ru.yojo.codegen.domain.lombok.Accessors;
import ru.yojo.codegen.domain.lombok.EqualsAndHashCode;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.Map;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.constants.Dictionary.ENABLE;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

@SuppressWarnings("all")
public class LombokUtils {
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
}
