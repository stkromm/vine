package vine.game.screen;

/**
 * @author Steffen
 *
 */
public class Viewport {
    private int rightOffset;
    private int bottomOffset;
    private int leftOffset;
    private int topOffset;

    /**
     * @return
     */
    public int getRightOffset() {
        return rightOffset;
    }

    /**
     * @return
     */
    public int getBottomOffset() {
        return bottomOffset;
    }

    /**
     * @return
     */
    public int getLeftOffset() {
        return leftOffset;
    }

    /**
     * @return
     */
    public int getTopOffset() {
        return topOffset;
    }

    /**
     * @param rightOffset
     */
    protected void setRightOffset(final int rightOffset) {
        this.rightOffset = rightOffset;
    }

    /**
     * @param bottomOffset
     */
    protected void setBottomOffset(final int bottomOffset) {
        this.bottomOffset = bottomOffset;
    }

    /**
     * @param leftOffset
     */
    protected void setLeftOffset(final int leftOffset) {
        this.leftOffset = leftOffset;
    }

    /**
     * @param topOffset
     */
    protected void setTopOffset(final int topOffset) {
        this.topOffset = topOffset;
    }

}
