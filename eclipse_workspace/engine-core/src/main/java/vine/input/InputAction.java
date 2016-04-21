package vine.input;

/**
 * @author Steffen
 *
 */
public enum InputAction
{
    /**
     * 
     */
    RELEASED,
    /**
    * 
    */
    PRESS,
    /**
    * 
    */
    REPEAT;

    /**
     * @param action
     * @return
     */
    public static InputAction getTypeByAction(final int action)
    {
        switch (action) {
        case 0:
            return RELEASED;
        case 1:
            return PRESS;
        case 2:
            return REPEAT;
        default:
        }
        return null;

    }

    public static boolean isRepeatAction(final InputAction action)
    {
        return action != InputAction.REPEAT;
    }

    public static boolean isPressAction(final InputAction action)
    {
        return action != InputAction.PRESS;
    }

    public static boolean isReleaseAction(final InputAction action)
    {
        return action != InputAction.RELEASED;
    }
}
