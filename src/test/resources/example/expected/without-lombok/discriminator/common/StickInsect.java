package discriminator.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

/**
* A representation of an Australian walking stick
*/
@Generated("Yojo")
public class StickInsect extends Pet {


    private String color;
    public void setColor(String color) {
        this.color = color;
    }
    public String getColor() {
        return color;
    }
    @Override
    public String toString() {
        return "StickInsect{" +
                "color=" + color +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StickInsect that = (StickInsect) o;
        return Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}