package vine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import vine.util.BufferConverter;

/**
 * @author Steffen
 * 
 */
public class VertexBufferObject {
    /**
     * Vertex array object.
     */
    private final int vao;
    /**
     * Vertex buffer object.
     */
    private final int vbo;
    /**
     * Index buffer object.
     */
    private final int ibo;
    /**
     * Texture buffer object.
     */
    private final int tbo;

    private int count;

    private final Graphics graphics;

    private final FloatBuffer textureBuffer;
    private final FloatBuffer verticeBuffer;
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
    public VertexBufferObject(final float[] vertices, final int[] indices, final float[] uvs, Graphics graphics) {
        if (indices == null) {
            throw new NullPointerException("indice field of vertex array can't be null.");
        }

        this.graphics = graphics;

        count = indices.length;

        vao = graphics.generateVertexArray();
        graphics.bindVertexArray(vao);

        vbo = graphics.generateBuffer();
        verticeBuffer = BufferConverter.createFloatBuffer(vertices);
        graphics.bindVertexData(vbo, verticeBuffer);

        tbo = graphics.generateBuffer();
        textureBuffer = BufferConverter.createFloatBuffer(uvs);
        graphics.bindTextureData(tbo, textureBuffer);

        ibo = graphics.generateBuffer();
        indiceBuffer = BufferConverter.createIntBuffer(indices);
        graphics.bindIndexData(ibo, indiceBuffer);

        graphics.bindElementArrayBuffer(0);
        graphics.bindArrayBuffer(0);
        graphics.bindVertexArray(0);
    }

    public void changeVertices(final float[] vertices) {
        verticeBuffer.position(0);
        verticeBuffer.put(vertices, 0, vertices.length);
        verticeBuffer.flip();
        graphics.reallocateVerticeData(vbo, verticeBuffer);
    }

    public void changeTexture(final float[] uvs) {
        textureBuffer.position(0);
        textureBuffer.put(uvs, 0, uvs.length);
        textureBuffer.flip();
        graphics.reallocateTextureData(tbo, textureBuffer);
    }

    public void changeIndices(final int count) {
        if (count > indiceBuffer.capacity() / 6) {
            int[] indices = new int[count * 6];
            int[] indice = new int[] { 0, 1, 2, 2, 3, 0 };
            for (int i = count * 6 - 1; i >= 0; i--) {
                for (int b = 0; b < indice.length; b++) {
                    indices[i * indice.length + b] = i * 4 + indice[b];
                }
            }
            indiceBuffer = BufferConverter.createIntBuffer(indices);
            graphics.bindIndexData(ibo, indiceBuffer);
        }
        indiceBuffer.limit(count* 6);
        indiceBuffer.position(count * 6);
        indiceBuffer.flip();
        this.count = count * 6;
        graphics.reallocateIndicesData(ibo, indiceBuffer);
    }

    /**
     * Binds the current vertex array buffer.
     */
    public void bind() {
        graphics.bindVertexArray(vao);
        graphics.bindElementArrayBuffer(ibo);
    }

    /**
     * Unbinds the current vertex array buffer.
     */
    public void unbind() {
        graphics.bindElementArrayBuffer(0);
        graphics.bindVertexArray(0);
    }

    /**
     * Draws the current triangles in the buffer.
     */
    public void draw() {
        graphics.drawElements(count);
    }

    void render() {
        bind();
        draw();
    }
}