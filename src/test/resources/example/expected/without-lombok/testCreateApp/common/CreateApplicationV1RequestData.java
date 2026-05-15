package testCreateApp.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import testCreateApp.common.CreateApplicationV1RequestDataConditions;

@Generated("Yojo")
public class CreateApplicationV1RequestData {


    @Valid
    private CreateApplicationV1RequestDataConditions conditions;
    public void setConditions(CreateApplicationV1RequestDataConditions conditions) {
        this.conditions = conditions;
    }
    public CreateApplicationV1RequestDataConditions getConditions() {
        return conditions;
    }
    @Override
    public String toString() {
        return "CreateApplicationV1RequestData{" +
                "conditions=" + conditions +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateApplicationV1RequestData that = (CreateApplicationV1RequestData) o;
        return Objects.equals(conditions, that.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditions);
    }
}