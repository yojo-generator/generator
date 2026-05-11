package discriminator.common;

import discriminator.common.CatHuntingSkill;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
* A representation of a cat
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class Cat extends Pet {


    /**
     * The measured skill for hunting
     */
    private CatHuntingSkill huntingSkill;
}