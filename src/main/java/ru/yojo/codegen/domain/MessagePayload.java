package ru.yojo.codegen.domain;

public class MessagePayload {

    private final String reference;

    public MessagePayload(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }
}
