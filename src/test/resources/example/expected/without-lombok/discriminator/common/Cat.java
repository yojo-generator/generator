package discriminator.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
public class Cat extends Pet {

    private String huntingSkill;

    public void setHuntingSkill(String huntingSkill) {
        this.huntingSkill = huntingSkill;
    }
    public String getHuntingSkill() {
        return huntingSkill;
    }
}