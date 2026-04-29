package discriminator.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "petType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Cat.class, name = "Cat"),
    @JsonSubTypes.Type(value = Dog.class, name = "Dog")
})
public class Pet {

    private String name;

    private String petType;

}