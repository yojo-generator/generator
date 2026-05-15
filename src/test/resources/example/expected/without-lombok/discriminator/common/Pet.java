package discriminator.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;

@Generated("Yojo")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "petType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Cat.class, name = "Cat"),
    @JsonSubTypes.Type(value = Dog.class, name = "Dog"),
    @JsonSubTypes.Type(value = StickInsect.class, name = "StickBug")
})
public class Pet {


    @NotBlank
    private String name;

    @JsonTypeId
    @NotBlank
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
    @Override
    public String toString() {
        return "Pet{" +
                "name=" + name + ", " +
                "petType=" + petType +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet that = (Pet) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(petType, that.petType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, petType);
    }
}