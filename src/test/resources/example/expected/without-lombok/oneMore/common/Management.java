package oneMore.common;

import java.util.List;
import javax.validation.Valid;
import javax.annotation.processing.Generated;
import oneMore.common.CompanyPerson;

@Generated("Yojo")
public class Management {

    /**
     * Persons
     */
    @Valid
    private List<CompanyPerson> persons;

    public void setPersons(List<CompanyPerson> persons) {
        this.persons = persons;
    }
    public List<CompanyPerson> getPersons() {
        return persons;
    }
}