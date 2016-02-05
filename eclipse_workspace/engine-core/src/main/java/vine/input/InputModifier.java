package vine.input;

/**
 * @author Steffen
 *
 */
public enum InputModifier {
    /**
     * 
     */
    ALT(4),
    /**
    * 
    */
    CONTROL(2),
    /**
    * 
    */
    SHIFT(1),
    /**
    * 
    */
    SUPER(8);

    private final int flag;

    InputModifier(final int flag) {
        this.flag = flag;
    }

    /**
     * @param mods
     *            Int that is checked, if the corresponding flag is set
     * @return true, if the flag bit is 1.
     */
    public static final boolean isAlt(final int mods) {
        return (mods & ALT.flag) == 1;
    }

    /**
     * @param mods
     *            Int that is checked, if the corresponding flag is set
     * @return true, if the flag bit is 1.
     */
    public static final boolean isControl(final int mods) {
        return (mods & CONTROL.flag) == 1;
    }

    /**
     * @param mods
     *            Int that is checked, if the corresponding flag is set
     * @return true, if the flag bit is 1.
     */
    public static final boolean isSuper(final int mods) {
        return (mods & SUPER.flag) == 1;
    }

    /**
     * @param mods
     *            Int that is checked, if the corresponding flag is set
     * @return true, if the flag bit is 1.
     */
    public static final boolean isShift(final int mods) {
        return (mods & SHIFT.flag) == 1;
    }
}
