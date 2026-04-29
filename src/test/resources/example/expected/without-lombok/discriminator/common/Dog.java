package discriminator.common;

import com.fasterxml.jackson.annotation.JsonTypeId;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class Dog {

    private String name;

    @JsonTypeId
    private String petType;

    private Integer packSize;

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
    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }
    public Integer getPackSize() {
        return packSize;
    }
}