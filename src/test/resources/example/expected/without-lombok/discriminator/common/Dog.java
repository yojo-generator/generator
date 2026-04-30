package discriminator.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
public class Dog extends Pet {

    private Integer packSize;

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }
    public Integer getPackSize() {
        return packSize;
    }
}