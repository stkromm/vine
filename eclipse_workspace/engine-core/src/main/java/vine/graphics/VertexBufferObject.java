package vine.graphics;

import java.nio.IntBuffer;

import vine.util.BufferConverter;

/**
 * @author Steffen
 * 
 */
public class VertexBufferObject {
    private static final int[] SQUAD_INDICE = new int[] { 0, 1, 2, 2, 3, 0 };
    private final int vao;
    private final int ibo;
    private final int count;
    private final Graphics graphics;
    private IntBuffer indiceBuffer;

    /**
     * Creates a new vertex array buffer.
     * 
     * @param vertices
     *            The vertices
     * @param indices
     *            The indices for the vertex order
     * @param uvs
     *            The texture coordinates for the vertices
     */
    public VertexBufferObject(final int size, final VertexAttributeBuffer... attributes) {
        this.graphics = GraphicsProvider.getGraphics();
        this.count = size;
        this.vao = this.graphics.generateVertexArray();
        this.graphics.bindVertexArray(this.vao);

        for (final VertexAttributeBuffer attribute : attributes) {
            attribute.bind();
        }
        this.ibo = this.graphics.generateBuffer();
        final int[] indices = new int[this.count * 6];
        for (int i = this.count - 1; i >= 0; i--) {
            for (int b = 0; b < VertexBufferObject.SQUAD_INDICE.length; b++) {
                indices[i * VertexBufferObject.SQUAD_INDICE.length + b] = i * 4 + VertexBufferObject.SQUAD_INDICE[b];
            }
        }
        this.indiceBuffer = BufferConverter.createIntBuffer(indices);
        this.graphics.bindIndexData(this.ibo, this.indiceBuffer);
        this.indiceBuffer = BufferConverter.createIntBuffer(indices);
        this.graphics.bindIndexData(this.ibo, this.indiceBuffer);
        this.graphics.bindElementArrayBuffer(0);
        this.graphics.bindArrayBuffer(0);
        this.graphics.bindVertexArray(0);
    }

    public int getCount() {
        return this.count;
    }

    /**
     * Binds the current vertex array buffer.
     */
    void bind() {
        this.graphics.bindVertexArray(this.vao);
        this.graphics.bindElementArrayBuffer(this.ibo);
    }

    /**
     * Unbinds the current vertex array buffer.
     */
    void unbind() {
        this.graphics.bindElementArrayBuffer(0);
        this.graphics.bindVertexArray(0);
    }

    /**
     * Draws the current triangles in the buffer.
     */
    void draw(int count) {
        this.graphics.drawElements(count);
    }

    public void draw() {
        this.graphics.drawElements(this.count);
    }
}
