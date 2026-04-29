package oneMore.common;

import java.util.List;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import oneMore.common.CompanyPerson;
import lombok.NoArgsConstructor;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class Management {

    /**
     * Persons
     */
    @Valid
    private List<CompanyPerson> persons;

}