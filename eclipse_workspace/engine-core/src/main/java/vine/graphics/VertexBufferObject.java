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
    private int count;
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
    public VertexBufferObject(final int[] indices, final VertexAttributeBuffer... attributes) {
        if (indices == null) {
            throw new NullPointerException("indice field of vertex array can't be null.");
        }
        this.graphics = GraphicsProvider.getGraphics();

        count = indices.length;

        vao = graphics.generateVertexArray();
        graphics.bindVertexArray(vao);

        for (VertexAttributeBuffer attribute : attributes) {
            attribute.bind();
        }

        ibo = graphics.generateBuffer();
        indiceBuffer = BufferConverter.createIntBuffer(indices);
        graphics.bindIndexData(ibo, indiceBuffer);

        graphics.bindElementArrayBuffer(0);
        graphics.bindArrayBuffer(0);
        graphics.bindVertexArray(0);
    }

    void changeIndices(final int count) {
        if (count > indiceBuffer.capacity() / 6) {
            final int[] indices = new int[count * 6];
            for (int i = count * 6 - 1; i >= 0; i--) {
                for (int b = 0; b < SQUAD_INDICE.length; b++) {
                    indices[i * SQUAD_INDICE.length + b] = i * 4 + SQUAD_INDICE[b];
                }
            }
            indiceBuffer = BufferConverter.createIntBuffer(indices);
            graphics.bindIndexData(ibo, indiceBuffer);
        }
        indiceBuffer.limit(count * 6);
        indiceBuffer.position(count * 6);
        indiceBuffer.flip();
        this.count = count * 6;
        graphics.reallocateIndicesData(ibo, indiceBuffer);
    }

    /**
     * Binds the current vertex array buffer.
     */
    void bind() {
        graphics.bindVertexArray(vao);
        graphics.bindElementArrayBuffer(ibo);
    }

    /**
     * Unbinds the current vertex array buffer.
     */
    void unbind() {
        graphics.bindElementArrayBuffer(0);
        graphics.bindVertexArray(0);
    }

    /**
     * Draws the current triangles in the buffer.
     */
    void draw() {
        graphics.drawElements(count);
    }

    void render() {
        bind();
        draw();
        unbind();
    }
}
