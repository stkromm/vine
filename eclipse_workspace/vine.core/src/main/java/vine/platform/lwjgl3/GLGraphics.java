package vine.platform.lwjgl3;

import vine.math.matrix.Mat4f;
import vine.math.vector.Vec3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import vine.graphics.DrawPrimitive;
import vine.graphics.Graphics;
import vine.graphics.Texture.TextureFilter;
import vine.graphics.Texture.WrapMode;
import vine.graphics.VertexAttribute;
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
        GLFW.glfwSwapBuffers(context);
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
    public void createRgbaTexture2D(final int width, final int height, final int[] data)
    {
        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGBA,
                width,
                height,
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
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
    public void storeUniformVector3f(final int location, final Vec3f vector)
    {
        GL20.glUniform3f(location, vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void storeUniformMatrix4f(final int location, final Mat4f matrix)
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
        GL20.glVertexAttribPointer(
                attribute.getId(),
                attribute.getDimension(),
                GLGraphics.getTypeId(attribute.getType()),
                false,
                0,
                0);
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
    public int generateVAO()
    {
        return GL30.glGenVertexArrays();
    }

    @Override
    public void bindVAO(final int id)
    {
        GL30.glBindVertexArray(id);
    }

    @Override
    public void drawElements(final int count, final DrawPrimitive p)
    {
        GL11.glDrawElements(getPrimitiveId(p), count, GL11.GL_UNSIGNED_INT, 0);
    }

    @Override
    public void drawArrays(final int count, final DrawPrimitive p)
    {
        GL11.glDrawArrays(getPrimitiveId(p), 0, count);
    }

    private static int getPrimitiveId(final DrawPrimitive p)
    {
        switch (p) {
        case TRIANGLE:
            return GL11.GL_TRIANGLES;
        case TRIANGLE_FAN:
            return GL11.GL_TRIANGLE_FAN;
        case TRIANGLE_STRIP:
            return GL11.GL_TRIANGLE_STRIP;
        case LINE:
            return GL11.GL_LINES;
        case LINE_STRIP:
            return GL11.GL_LINE_STRIP;
        case POINT:
            return GL11.GL_POINTS;
        case LINE_LOOP:
            return GL11.GL_LINE_LOOP;
        default:
            return GL11.GL_LINES;
        }
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

    @Override
    public void setPolygonMode(final PolygonMode mode)
    {
        switch (mode) {
        case WIREFRAME:
            GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
        break;
        case POINTS:
            GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_POINT);
        break;
        case FILLED:
            GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
        break;
        default:
        break;
        }
    }

    private static final int getTextureFilterId(final TextureFilter filter)
    {

        switch (filter) {
        case NEAREST:
            return GL11.GL_NEAREST;
        case LINEAR:
            return GL11.GL_LINEAR;
        case NEAREST_LINEAR:
            return GL11.GL_NEAREST_MIPMAP_LINEAR;
        case NEAREST_NEAREST:
            return GL11.GL_NEAREST_MIPMAP_NEAREST;
        case LINEAR_LINEAR:
            return GL11.GL_LINEAR_MIPMAP_LINEAR;
        case LINEAR_NEAREST:
            return GL11.GL_LINEAR_MIPMAP_NEAREST;
        default:
            return GL11.GL_NEAREST;
        }
    }

    @Override
    public void setTextureFilter(final TextureFilter mag, final TextureFilter min)
    {
        if (mag != null)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, getTextureFilterId(min));
        }
        if (min != null)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, getTextureFilterId(mag));
        }
    }

    private static final int getWrappingModeId(final WrapMode x)
    {

        switch (x) {
        case REPEAT:
            return GL11.GL_REPEAT;
        case CLAMP:
            return GL11.GL_CLAMP;
        default:
            return GL11.GL_REPEAT;
        }
    }

    @Override
    public void setTextureWrapMode(final WrapMode x, final WrapMode y)
    {
        if (x != null)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, getWrappingModeId(x));
        }
        if (y != null)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, getWrappingModeId(y));
        }
    }

    @Override
    public void setRenderMode(final RenderMode mode)
    {
    }

    @Override
    public void replaceShaderCode(final String sourceCode, final int shader)
    {
        GL20.glShaderSource(shader, sourceCode);
    }
}
