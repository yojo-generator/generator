package discriminator.common;

import java.util.Objects;
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
    @Override
    public String toString() {
        return "Dog{" +
                "packSize=" + packSize +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog that = (Dog) o;
        return Objects.equals(packSize, that.packSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packSize);
    }
}