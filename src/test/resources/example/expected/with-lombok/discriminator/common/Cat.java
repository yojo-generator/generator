package discriminator.common;

import discriminator.common.CatHuntingSkill;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
/**
* A representation of a cat
*/
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