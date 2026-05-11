package oneMore.common;

import java.util.List;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import oneMore.common.CompanyPerson;

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