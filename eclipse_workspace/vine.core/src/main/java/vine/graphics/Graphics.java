package vine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import vine.graphics.Texture.TextureFilter;
import vine.graphics.Texture.WrapMode;
import vine.math.matrix.Mat4f;
import vine.math.vector.Vec3f;

/**
 * @author Steffen
 *
 */
public interface Graphics
{
    // OBJECT BINDINGS
    void bindTexture2D(int id);

    void bindShader(int id);

    void bindArrayBuffer(int id);

    void bindElementArrayBuffer(int id);

    void bindVertexAttribute(int bufferId, FloatBuffer vertices, VertexAttribute usage);

    void bindIndexData(int bufferId, IntBuffer indices);

    void bindVAO(int id);

    // INIT
    void makeContext(long l);

    void clearBuffer();

    void swapBuffer();

    void init();

    void setViewport(int x, int y, int width, int height);

    // TEXTURE FUNCTIONS

    int generateTexture();

    void setTextureFilter(TextureFilter mag, TextureFilter min);

    void setTextureWrapMode(WrapMode x, WrapMode y);

    void createRgbaTexture2D(int width, int height, final int[] data);

    // SHADER FUNCTIONS

    int getUniformLocation(int id, String name);

    void storeUniformInt(int location, int value);

    void storeUniformVector3f(int location, Vec3f vector);

    void storeUniformMatrix4f(int location, Mat4f matrix);

    void replaceShaderCode(String sourceCode, int shader);

    // BUFFERS

    int generateBuffer();

    void reallocateAttributeData(int vbo, FloatBuffer vertices);

    void reallocateIndicesData(int ibo, IntBuffer indicesBuffer);

    int generateVAO();

    // Drawing
    public enum RenderMode
    {
        RENDER, SELECT, FEEDBACK;
    }

    void setRenderMode(RenderMode mode);

    public enum PolygonMode
    {
        POINTS, WIREFRAME, FILLED
    }

    void setPolygonMode(PolygonMode mode);

    void drawElements(int count, DrawPrimitive primitive);

    void drawArrays(int count, DrawPrimitive primitive);
}