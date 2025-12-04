package ru.yojo.codegen.domain.lombok;

/**
 * Represents Lombok's {@code @EqualsAndHashCode} configuration.
 * Controls whether {@code equals()} and {@code hashCode()} are generated,
 * and whether {@code callSuper = true/false} is used.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class EqualsAndHashCode {

    /**
     * Whether {@code @EqualsAndHashCode} annotation should be generated.
     * Default: {@code false}.
     */
    private boolean enable = false;

    /**
     * Value for {@code callSuper} in {@code @EqualsAndHashCode}.
     * <ul>
     *   <li>{@code null} — omit the attribute (Lombok default: {@code callSuper = false} for classes without superclass)</li>
     *   <li>{@code true} — include {@code callSuper = true}</li>
     *   <li>{@code false} — include {@code callSuper = false}</li>
     * </ul>
     */
    private Boolean callSuper;

    /**
     * Returns whether {@code @EqualsAndHashCode} is enabled.
     *
     * @return {@code true} if annotation should be generated
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Enables or disables generation of {@code @EqualsAndHashCode}.
     *
     * @param enable {@code true} to generate the annotation
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Returns the value of the {@code callSuper} attribute.
     *
     * @return {@code Boolean} (may be {@code null})
     */
    public Boolean getCallSuper() {
        return callSuper;
    }

    /**
     * Sets the {@code callSuper} value for {@code @EqualsAndHashCode}.
     *
     * @param callSuper {@code true}, {@code false}, or {@code null} to omit
     */
    public void setCallSuper(Boolean callSuper) {
        this.callSuper = callSuper;
    }
}