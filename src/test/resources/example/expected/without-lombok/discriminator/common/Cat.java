package discriminator.common;

import discriminator.common.CatHuntingSkill;
import javax.annotation.processing.Generated;

@Generated("Yojo")
/**
* A representation of a cat
*/
public class Cat extends Pet {


    /**
     * The measured skill for hunting
     */
    private CatHuntingSkill huntingSkill;
    public void setHuntingSkill(CatHuntingSkill huntingSkill) {
        this.huntingSkill = huntingSkill;
    }
    public CatHuntingSkill getHuntingSkill() {
        return huntingSkill;
    }
}