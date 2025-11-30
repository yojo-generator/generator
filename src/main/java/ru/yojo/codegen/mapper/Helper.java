package ru.yojo.codegen.mapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Helper {

    private Boolean isMappedFromMessages = false;

    private Boolean isMappedFromSchemas = false;

    private Set<String> removeSchemas = new HashSet<>();
    private Set<String> excludeRemoveSchemas = new HashSet<>();
    private Set<String> excludeInheritanceSchemas = new HashSet<>();
    private Map<String, Object> innerSchemas = new ConcurrentHashMap<>();

    public Boolean isMappedFromMessages() {
        return isMappedFromMessages;
    }

    public void setIsMappedFromMessages(Boolean isMappedFromMessages) {
        this.isMappedFromMessages = isMappedFromMessages;
    }

    public Boolean isMappedFromSchemas() {
        return isMappedFromSchemas;
    }

    public void setIsMappedFromSchemas(Boolean isMappedFromSchemas) {
        this.isMappedFromSchemas = isMappedFromSchemas;
    }

    public Set<String> getRemoveSchemas() {
        return removeSchemas;
    }

    public void setRemoveSchemas(Set<String> removeSchemas) {
        this.removeSchemas = removeSchemas;
    }

    public Set<String> getExcludeRemoveSchemas() {
        return excludeRemoveSchemas;
    }

    public void setExcludeRemoveSchemas(Set<String> excludeRemoveSchemas) {
        this.excludeRemoveSchemas = excludeRemoveSchemas;
    }

    public Set<String> getExcludeInheritanceSchemas() {
        return excludeInheritanceSchemas;
    }

    public void setExcludeInheritanceSchemas(Set<String> excludeInheritanceSchemas) {
        this.excludeInheritanceSchemas = excludeInheritanceSchemas;
    }

    public Map<String, Object> getInnerSchemas() {
        return innerSchemas;
    }

    public void setInnerSchemas(Map<String, Object> innerSchemas) {
        this.innerSchemas = innerSchemas;
    }
}
