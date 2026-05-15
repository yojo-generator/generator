package testCreateApp.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import testCreateApp.common.CreateApplicationV1RequestDataConditionsRequested;

@Generated("Yojo")
public class CreateApplicationV1RequestDataConditions {


    @Valid
    private CreateApplicationV1RequestDataConditionsRequested requested;
    public void setRequested(CreateApplicationV1RequestDataConditionsRequested requested) {
        this.requested = requested;
    }
    public CreateApplicationV1RequestDataConditionsRequested getRequested() {
        return requested;
    }
    @Override
    public String toString() {
        return "CreateApplicationV1RequestDataConditions{" +
                "requested=" + requested +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateApplicationV1RequestDataConditions that = (CreateApplicationV1RequestDataConditions) o;
        return Objects.equals(requested, that.requested);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requested);
    }
}