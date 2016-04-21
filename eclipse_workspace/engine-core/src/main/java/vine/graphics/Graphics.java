package vine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import vine.math.Matrix4f;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public interface Graphics
{

    // RENDER FUNCTIONS
    void makeContext(long l);

    void clearBuffer();

    void swapBuffer();

    void init();

    void setViewport(int x, int y, int width, int height);

    // TEXTURE FUNCTIONS

    int generateTexture();

    void bindTexture2D(int id);

    void setTextureParameter(int parameter, int value);

    void createRgbaTexture2D(int width, int height, final int[] data);

    // SHADER FUNCTIONS

    int getUniformLocation(int id, String name);

    void storeUniformInt(int location, int value);

    void storeUniformVector3f(int location, Vector3f vector);

    void storeUniformMatrix4f(int location, Matrix4f matrix);

    void bindShader(int id);

    // BUFFERS

    int generateBuffer();

    void bindArrayBuffer(int id);

    void bindElementArrayBuffer(int id);

    void bindVertexAttribute(int bufferId, FloatBuffer vertices, VertexAttribute usage);

    void bindIndexData(int bufferId, IntBuffer indices);

    void reallocateAttributeData(int vbo, FloatBuffer vertices);

    void reallocateIndicesData(int ibo, IntBuffer indicesBuffer);

    int generateVertexArray();

    void bindVertexArray(int id);

    // Drawing

    void drawElements(int count);

    void drawArrays(int count);
}