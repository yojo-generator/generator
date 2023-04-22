package ru.yojo.codegen.domain;

@SuppressWarnings("all")
public class MessageImplementationProperties {

    private String implementationPackage;
    private String implementationClass;

    public MessageImplementationProperties(String implementationPackage, String implementationClass) {
        this.implementationPackage = implementationPackage;
        this.implementationClass = implementationClass;
    }

    public String getImplementationPackage() {
        return implementationPackage;
    }

    public void setImplementationPackage(String implementationPackage) {
        this.implementationPackage = implementationPackage;
    }

    public String getImplementationClass() {
        return implementationClass;
    }

    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
    }
}
