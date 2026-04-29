package example.testGenerate.oneMore.common;

import java.util.List;
import example.testGenerate.oneMore.common.CompanyPerson;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
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