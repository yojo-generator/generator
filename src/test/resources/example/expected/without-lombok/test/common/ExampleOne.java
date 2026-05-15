package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class ExampleOne {


    private String status;
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    @Override
    public String toString() {
        return "ExampleOne{" +
                "status=" + status +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleOne that = (ExampleOne) o;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}