package vine.game.screen;

/**
 * @author Steffen
 *
 */
public class Viewport
{
    private int rightOffset;
    private int bottomOffset;
    private int leftOffset;
    private int topOffset;

    /**
     * @return The offset the the right (black border)
     */
    public int getRightOffset()
    {
        return this.rightOffset;
    }

    /**
     * @return The offset the the bottom (black border)
     */
    public int getBottomOffset()
    {
        return this.bottomOffset;
    }

    /**
     * @return The offset the the left (black border)
     */
    public int getLeftOffset()
    {
        return this.leftOffset;
    }

    /**
     * @return The offset the the top (black border)
     */
    public int getTopOffset()
    {
        return this.topOffset;
    }

    void setRightOffset(final int rightOffset)
    {
        this.rightOffset = rightOffset;
    }

    void setBottomOffset(final int bottomOffset)
    {
        this.bottomOffset = bottomOffset;
    }

    void setLeftOffset(final int leftOffset)
    {
        this.leftOffset = leftOffset;
    }

    void setTopOffset(final int topOffset)
    {
        this.topOffset = topOffset;
    }
}
