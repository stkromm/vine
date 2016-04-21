package vine.input;

/**
 * @author Steffen
 *
 */
public interface Input
{
    /**
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface ScrollCallback
    {
        /**
         * @param context
         * @param offsetx
         * @param offsety
         */
        void scrolled(long context, double offsetx, double offsety);
    }

    /**
     * @param callback
     *            Callback, to assign functionality for scroll handling.
     */
    void setScrollCallback(ScrollCallback callback);

    /**
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface KeyCallback
    {
        /**
         * @param context
         * @param key
         * @param scancode
         * @param action
         * @param mods
         */
        void keyPressed(long context, int key, int scancode, InputAction action, int mods);
    }

    /**
     * @param callback
     */
    void setKeyCallback(KeyCallback callback);

    /**
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface CharCallback
    {
        /**
         * @param context
         * @param codepoint
         */
        void charInput(long context, int codepoint);
    }

    /**
     * @param callback
     */
    void setCharCallback(CharCallback callback);

    /**
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface CharModCallback
    {
        /**
         * @param context
         * @param codepoint
         * @param mods
         */
        void charModInput(long context, int codepoint, int mods);
    }

    /**
     * @param callback
     */
    void setCharModCallback(CharModCallback callback);

    /**
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface CursorPositionCallback
    {
        /**
         * @param context
         * @param x
         * @param y
         */
        void changedCursorPosition(long context, double x, double y);
    }

    /**
     * @param callback
     */
    void setCursorPositionCallback(CursorPositionCallback callback);

    /**
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface MouseButtonCallback
    {
        /**
         * @param context
         * @param button
         * @param action
         * @param mods
         */
        void pressedMouseButton(long context, int button, InputAction action, int mods);
    }

    /**
     * @param callback
     */
    void setMouseButtonCallback(MouseButtonCallback callback);

    /**
     * @return
     */
    double getCursorX();

    /**
     * @return
     */
    double getCursorY();

    /**
     * @param context
     */
    void listenToWindow(long context);

    /**
     * 
     */
    void poll();
}
