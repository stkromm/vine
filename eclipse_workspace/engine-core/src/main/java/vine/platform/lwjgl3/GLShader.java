package vine.platform.lwjgl3;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.logging.Level;
import java.util.logging.Logger;

import vine.graphics.Shader;
import vine.math.Matrix3f;
import vine.math.Matrix4f;
import vine.math.Vector3f;
import vine.util.BufferConverter;

/**
 * Lwjgl, Open-GL Desktop platform implementation of Shader adapter.
 * 
 * @author Steffen
 *
 */
public class GLShader extends Shader {

    private boolean enabled = false;

    /**
     * 
     */
    public GLShader() {
        super();
    }

    @SuppressWarnings("boxing")
    @Override
    public int getUniform(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }
        int result = glGetUniformLocation(id, name);
        if (result == -1) {
            Logger.getGlobal().log(Level.SEVERE,
                    Messages.getString("GLShader.0") + name + Messages.getString("GLShader.1")); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            locationCache.put(name, result);
        }
        return result;
    }

    @Override
    public void setUniform1i(String name, int value) {
        if (!enabled) {
            bind();
        }
        glUniform1i(getUniform(name), value);
    }

    @Override
    public void setUniform1f(String name, float value) {
        if (!enabled) {
            bind();
        }
        glUniform1f(getUniform(name), value);
    }

    @Override
    public void setUniform3f(String name, Vector3f vector) {
        if (!enabled) {
            bind();
        }
        glUniform3f(getUniform(name), vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void setUniformMat3f(String name, Matrix3f matrix) {
        if (!enabled) {
            bind();
        }
    }

    @Override
    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!enabled)
            bind();
        glUniformMatrix4fv(getUniform(name), false, BufferConverter.createFloatBuffer(matrix.elements));
    }

    public void bind() {
        glUseProgram(id);
        enabled = true;
    }

    public void unbind() {
        glUseProgram(0);
        enabled = false;
    }

}