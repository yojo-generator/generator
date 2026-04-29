package example.testGenerate.discriminator.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
public class Cat {

    private String name;

    private String petType;

    private String huntingSkill;

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
    public void setHuntingSkill(String huntingSkill) {
        this.huntingSkill = huntingSkill;
    }
    public String getHuntingSkill() {
        return huntingSkill;
    }
}