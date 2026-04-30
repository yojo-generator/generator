package discriminator.common;

import javax.annotation.processing.Generated;
import javax.validation.constraints.Min;

/**
* A representation of a dog
*/
@Generated("Yojo")
public class Dog extends Pet {

    /**
     * the size of the pack the dog is from
     */
    @Min(0)
    private Integer packSize;

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }
    public Integer getPackSize() {
        return packSize;
    }
}