package vine.platform.lwjgl3;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL;

import vine.graphics.Graphics;
import vine.graphics.Shader;
import vine.math.Matrix4f;
import vine.math.Vector3f;
import vine.util.BufferConverter;

/**
 * @author Steffen
 *
 */
public final class GLGraphics implements Graphics {
    private long context;

    @Override
    public void makeContext(final long context) {
        glfwMakeContextCurrent(context);
        this.context = context;
    }

    @Override
    public void swapBuffer() {
        glfwSwapBuffers(context);
    }

    @Override
    public void init() {
        // init open gl
        GL.createCapabilities();
        // configure open gl
        // glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);

        glfwSwapInterval(0);
    }

    @Override
    public void setViewport(final int x, final int y, final int width, final int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void clearBuffer() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void bindTexture2D(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    @Override
    public void setTextureParameter(int parameter, int value) {
        glTexParameteri(GL_TEXTURE_2D, parameter, value);
    }

    @Override
    public void createRgbaTexture2D(int width, int height, int[] data) {
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                BufferConverter.createIntBuffer(data));
    }

    @Override
    public int generateTexture() {
        return glGenTextures();
    }

    @Override
    public int getUniformLocation(int id, String name) {
        return glGetUniformLocation(id, name);
    }

    @Override
    public void storeUniformInt(int location, int value) {
        glUniform1i(location, value);
    }

    @Override
    public void storeUniformVector3f(int location, Vector3f vector) {
        glUniform3f(location, vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void storeUniformMatrix4f(int location, Matrix4f matrix) {
        glUniformMatrix4fv(location, false, BufferConverter.createFloatBuffer(matrix.elements));
    }

    @Override
    public void bindShader(int id) {
        glUseProgram(id);

    }

    @Override
    public int generateBuffer() {
        return glGenBuffers();
    }

    @Override
    public void bindArrayBuffer(int id) {
        glBindBuffer(GL_ARRAY_BUFFER, id);
    }

    @Override
    public void bindVertexData(int bufferId, FloatBuffer vertices) {
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);
    }

    @Override
    public void bindTextureData(int bufferId, FloatBuffer uvs) {
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, uvs, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(Shader.TCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.TCOORD_ATTRIB);
    }

    @Override
    public void bindIndexData(int bufferId, IntBuffer indices) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    @Override
    public int generateVertexArray() {
        return glGenVertexArrays();
    }

    @Override
    public void bindVertexArray(int id) {
        glBindVertexArray(id);
    }

    @Override
    public void drawElements(int count) {
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void drawArrays(int count) {
        glDrawArrays(GL_TRIANGLES, 0, count);
    }

    @Override
    public void bindElementArrayBuffer(int id) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
    }

    @Override
    public void reallocateVerticeData(int vbo, FloatBuffer vertices) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
    }

    @Override
    public void reallocateTextureData(int tbo, FloatBuffer uvBuffer) {
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, uvBuffer);
    }
}
