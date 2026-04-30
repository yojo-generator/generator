package discriminator.common;

import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;

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