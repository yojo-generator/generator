package discriminator.common;

import javax.annotation.processing.Generated;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
* A representation of a dog
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class Dog extends Pet {


    /**
     * the size of the pack the dog is from
     */
    @Min(0)
    private Integer packSize;
}