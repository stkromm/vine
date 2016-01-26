package vine.graphics;

/**
 * @author Steffen
 *
 */
public interface Graphics {

    /**
     * @param context
     */
    void makeContext(long context);

    /**
     * 
     */
    void init();

    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */
    void setViewport(int x, int y, int width, int height);

    /**
     * 
     */
    void clearBuffer();

    /**
     * 
     */
    void swapBuffers();

}