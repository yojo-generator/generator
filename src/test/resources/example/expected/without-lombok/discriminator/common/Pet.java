package example.testGenerate.discriminator.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javax.annotation.processing.Generated;

@Generated("Yojo")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "petType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Cat.class, name = "Cat"),
    @JsonSubTypes.Type(value = Dog.class, name = "Dog")
})
public class Pet {

    private String name;

    private String petType;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setPetType(String petType) {
        this.petType = petType;
    }
    public String getPetType() {
        return petType;
    }
}