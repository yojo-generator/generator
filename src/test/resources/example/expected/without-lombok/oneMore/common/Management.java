package oneMore.common;

import java.util.List;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
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
    @Override
    public String toString() {
        return "Management{" +
                "persons=" + persons +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Management that = (Management) o;
        return Objects.equals(persons, that.persons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons);
    }
}