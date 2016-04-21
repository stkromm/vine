package vine.platform.lwjgl3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import vine.graphics.Graphics;
import vine.graphics.VertexAttribute;
import vine.math.Matrix4f;
import vine.math.Vector3f;
import vine.util.BufferConverter;
import vine.util.TypeSize;

/**
 * @author Steffen
 *
 */
public final class GLGraphics implements Graphics
{
    private long context;

    @Override
    public void makeContext(final long context)
    {
        GLFW.glfwMakeContextCurrent(context);
        this.context = context;
    }

    @Override
    public void swapBuffer()
    {
        GLFW.glfwSwapBuffers(this.context);
    }

    @Override
    public void init()
    {
        // init open gl
        GL.createCapabilities();
        // configure open gl
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_FRONT);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GLFW.glfwSwapInterval(0);
    }

    @Override
    public void setViewport(final int x, final int y, final int width, final int height)
    {

        GL11.glViewport(x, y, width, height);
    }

    @Override
    public void clearBuffer()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void bindTexture2D(final int id)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    @Override
    public void setTextureParameter(final int parameter, final int value)
    {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, parameter, value);
    }

    @Override
    public void createRgbaTexture2D(final int width, final int height, final int[] data)
    {
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                BufferConverter.createIntBuffer(data));
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
    }

    @Override
    public int generateTexture()
    {
        return GL11.glGenTextures();
    }

    @Override
    public int getUniformLocation(final int id, final String name)
    {
        return GL20.glGetUniformLocation(id, name);
    }

    @Override
    public void storeUniformInt(final int location, final int value)
    {
        GL20.glUniform1i(location, value);
    }

    @Override
    public void storeUniformVector3f(final int location, final Vector3f vector)
    {
        GL20.glUniform3f(location, vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void storeUniformMatrix4f(final int location, final Matrix4f matrix)
    {
        GL20.glUniformMatrix4fv(location, false, BufferConverter.createFloatBuffer(matrix.getElements()));
    }

    @Override
    public void bindShader(final int id)
    {
        GL20.glUseProgram(id);

    }

    @Override
    public int generateBuffer()
    {
        return GL15.glGenBuffers();
    }

    @Override
    public void bindArrayBuffer(final int id)
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
    }

    @Override
    public void bindVertexAttribute(final int bufferId, final FloatBuffer data, final VertexAttribute attribute)
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(attribute.getId(), attribute.getDimension(),
                GLGraphics.getTypeId(attribute.getType()), false, 0, 0);
        GL20.glEnableVertexAttribArray(attribute.getId());
    }

    private static int getTypeId(final TypeSize type)
    {
        switch (type) {
        case INT32:
            return GL11.GL_FLOAT;
        case BYTE8:
            return GL11.GL_UNSIGNED_BYTE;
        case LONG64:
            return GL11.GL_DOUBLE;
        case SHORT16:
            return GL11.GL_UNSIGNED_SHORT;
        default:
            return GL11.GL_FLOAT;
        }
    }

    @Override
    public void bindIndexData(final int bufferId, final IntBuffer indices)
    {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, bufferId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
    }

    @Override
    public int generateVertexArray()
    {
        return GL30.glGenVertexArrays();
    }

    @Override
    public void bindVertexArray(final int id)
    {
        GL30.glBindVertexArray(id);
    }

    @Override
    public void drawElements(final int count)
    {
        GL11.glDrawElements(GL11.GL_TRIANGLES, count, GL11.GL_UNSIGNED_INT, 0);
    }

    @Override
    public void drawArrays(final int count)
    {
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, count);
    }

    @Override
    public void bindElementArrayBuffer(final int id)
    {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
    }

    @Override
    public void reallocateAttributeData(final int attributeBuffer, final FloatBuffer data)
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, attributeBuffer);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, data);
    }

    @Override
    public void reallocateIndicesData(final int ibo, final IntBuffer indicesBuffer)
    {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, indicesBuffer);
    }
}
