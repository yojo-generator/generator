package ru.croc.yojo.generator.util;

import java.util.Map;

/**
 * Утилитный класс для работы с YAML данными.
 * Содержит константы и вспомогательные методы для парсинга YAML.
 */
public class YamlUtils {

    // Константы для ключей YAML
    public static final String ASYNCAPI = "asyncapi";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String VERSION = "version";
    public static final String DESCRIPTION = "description";
    public static final String TYPE = "type";
    public static final String PROPERTIES = "properties";
    public static final String COMPONENTS = "components";
    public static final String SCHEMAS = "schemas";
    public static final String MESSAGES = "messages";
    public static final String PAYLOAD = "payload";
    public static final String PUBLISH = "publish";
    public static final String SUBSCRIBE = "subscribe";
    public static final String CHANNELS = "channels";
    public static final String REF = "$ref";

    /**
     * Безопасно извлекает строковое значение из карты.
     *
     * @param map карта данных
     * @param key ключ
     * @return строковое значение или null
     */
    public static String getStringValue(Map<String, Object> map, String key) {
        if (map == null) {
            return null;
        }
        Object value = map.get(key);
        return value instanceof String ? (String) value : null;
    }

    /**
     * Безопасно извлекает числовое значение из карты.
     *
     * @param map карта данных
     * @param key ключ
     * @return числовое значение или null
     */
    public static Number getNumberValue(Map<String, Object> map, String key) {
        if (map == null) {
            return null;
        }
        Object value = map.get(key);
        return value instanceof Number ? (Number) value : null;
    }

    /**
     * Безопасно извлекает булевое значение из карты.
     *
     * @param map карта данных
     * @param key ключ
     * @return булевое значение или null
     */
    public static Boolean getBooleanValue(Map<String, Object> map, String key) {
        if (map == null) {
            return null;
        }
        Object value = map.get(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }

    /**
     * Безопасно извлекает Map значение из карты.
     *
     * @param map карта данных
     * @param key ключ
     * @return Map значение или null
     */
    public static Map<String, Object> getMapValue(Map<String, Object> map, String key) {
        if (map == null) {
            return null;
        }
        Object value = map.get(key);
        return value instanceof Map ? (Map<String, Object>) value : null;
    }

    /**
     * Проверяет, является ли значение ссылкой на компонент.
     *
     * @param value значение для проверки
     * @return true, если значение является ссылкой на компонент
     */
    public static boolean isReference(Object value) {
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            return map.containsKey(REF);
        }
        return false;
    }

    /**
     * Извлекает имя компонента из ссылки.
     *
     * @param value значение ссылки
     * @return имя компонента или null
     */
    public static String getReferenceName(Object value) {
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            String ref = (String) map.get(REF);
            if (ref != null && ref.startsWith("#/components/schemas/")) {
                return ref.substring("#/components/schemas/".length());
            }
        }
        return null;
    }
}