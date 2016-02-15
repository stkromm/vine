package vine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import vine.math.Matrix4f;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public interface Graphics {

    // RENDER FUNCTIONS
    /**
     * @param context
     */
    void makeContext(long context);

    /**
     * 
     */
    void clearBuffer();

    /**
     * 
     */
    void swapBuffer();

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

    void bindVertexData(int bufferId, FloatBuffer vertices);

    void bindTextureData(int bufferId, FloatBuffer uvs);

    void bindIndexData(int bufferId, IntBuffer indices);

    int generateVertexArray();

    void bindVertexArray(int id);

    // Drawing

    void drawElements(int count);

    void drawArrays(int count);

    void reallocateVerticeData(int vbo, FloatBuffer vertices);

    void reallocateTextureData(int tbo, FloatBuffer uvBuffer);

    void reallocateIndicesData(int ibo, IntBuffer indicesBuffer);
}