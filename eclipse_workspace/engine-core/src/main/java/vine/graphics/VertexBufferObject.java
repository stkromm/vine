package vine.graphics;

import java.nio.IntBuffer;

import vine.util.BufferConverter;

/**
 * @author Steffen
 * 
 */
public class VertexBufferObject
{
    private static final int[] QUAD_INDICE = new int[] { 0, 1, 2, 2, 3, 0 };
    private final int          vao;
    private final int          ibo;
    private final int          count;
    private final Graphics     graphics;
    private final IntBuffer    indiceBuffer;

    public VertexBufferObject(final int[] indices, final VertexAttributeBuffer... attributes)
    {
        graphics = GraphicsProvider.getGraphics();
        count = indices.length / 6;
        vao = graphics.generateVertexArray();
        graphics.bindVertexArray(vao);

        for (final VertexAttributeBuffer attribute : attributes)
        {
            attribute.bind();
        }

        ibo = graphics.generateBuffer();
        indiceBuffer = BufferConverter.createIntBuffer(indices);
        graphics.bindIndexData(ibo, indiceBuffer);
        graphics.bindElementArrayBuffer(0);
        graphics.bindArrayBuffer(0);
        graphics.bindVertexArray(0);
    }

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
    public VertexBufferObject(final int size, final VertexAttributeBuffer... attributes)
    {
        graphics = GraphicsProvider.getGraphics();
        count = size;
        vao = graphics.generateVertexArray();
        graphics.bindVertexArray(vao);

        for (final VertexAttributeBuffer attribute : attributes)
        {
            attribute.bind();
        }

        final int[] indices = new int[count * 6];
        for (int i = count - 1; i >= 0; i--)
        {
            for (int b = 0; b < VertexBufferObject.QUAD_INDICE.length; b++)
            {
                indices[i * VertexBufferObject.QUAD_INDICE.length + b] = i * 4 + VertexBufferObject.QUAD_INDICE[b];
            }
        }

        ibo = graphics.generateBuffer();
        indiceBuffer = BufferConverter.createIntBuffer(indices);
        graphics.bindIndexData(ibo, indiceBuffer);
        graphics.bindElementArrayBuffer(0);
        graphics.bindArrayBuffer(0);
        graphics.bindVertexArray(0);
    }

    public int getCount()
    {
        return count;
    }

    /**
     * Binds the current vertex array buffer.
     */
    public void bind()
    {
        graphics.bindVertexArray(vao);
        graphics.bindElementArrayBuffer(ibo);
    }

    /**
     * Unbinds the current vertex array buffer.
     */
    public static void unbind()
    {
        GraphicsProvider.getGraphics().bindElementArrayBuffer(0);
        GraphicsProvider.getGraphics().bindVertexArray(0);
    }

    public void draw(final int numberOfQuads)
    {
        graphics.drawElements(numberOfQuads * 6, DrawPrimitive.TRIANGLE);
    }

    public void draw()
    {
        graphics.drawElements(count * 6, DrawPrimitive.TRIANGLE);
    }
}
