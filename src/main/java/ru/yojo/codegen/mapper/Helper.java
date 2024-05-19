package ru.yojo.codegen.mapper;

import org.springframework.stereotype.Component;


@Component
public class Helper {

    private Boolean isMappedFromMessages = false;

    private Boolean isMappedFromSchemas = false;

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
}
