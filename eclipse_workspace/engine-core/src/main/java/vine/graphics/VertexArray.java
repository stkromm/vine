package vine.graphics;

/**
 * @author Steffen
 * 
 */
public interface VertexArray {

    /**
     * Draws the current triangles in the buffer.
     */
    void draw();

    /**
     * 
     */
    void bind();

    /**
     * 
     */
    default void render() {
        bind();
        draw();
    }
}
