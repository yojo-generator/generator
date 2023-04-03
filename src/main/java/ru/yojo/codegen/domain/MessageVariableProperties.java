package ru.yojo.codegen.domain;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.FIELD;
import static ru.yojo.codegen.constants.ConstantsEnum.formatString;
import static ru.yojo.codegen.util.MapperUtil.capitalize;
import static ru.yojo.codegen.util.MapperUtil.uncapitalize;

@SuppressWarnings("all")
public class MessageVariableProperties {

    private String name;
    private String type;
    private String reference;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        if (reference != null) {
            return lineSeparator() +
                    formatString(FIELD, reference, uncapitalize(reference));
        } else {
            return lineSeparator() +
                    formatString(FIELD, capitalize(type), name);
        }
    }
}
