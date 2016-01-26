package vine.game.screen;

public class Viewport {
    private int rightOffset;
    private int bottomOffset;
    private int leftOffset;
    private int topOffset;

    public int getRightOffset() {
        return rightOffset;
    }

    public int getBottomOffset() {
        return bottomOffset;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public int getTopOffset() {
        return topOffset;
    }

    protected void setRightOffset(int rightOffset) {
        this.rightOffset = rightOffset;
    }

    protected void setBottomOffset(int bottomOffset) {
        this.bottomOffset = bottomOffset;
    }

    protected void setLeftOffset(int leftOffset) {
        this.leftOffset = leftOffset;
    }

    protected void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }

}
