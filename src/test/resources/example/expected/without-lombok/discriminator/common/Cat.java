package discriminator.common;

import discriminator.common.CatHuntingSkill;
import java.util.Objects;
import javax.annotation.processing.Generated;

/**
* A representation of a cat
*/
@Generated("Yojo")
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
    @Override
    public String toString() {
        return "Cat{" +
                "huntingSkill=" + huntingSkill +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat that = (Cat) o;
        return Objects.equals(huntingSkill, that.huntingSkill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(huntingSkill);
    }
}