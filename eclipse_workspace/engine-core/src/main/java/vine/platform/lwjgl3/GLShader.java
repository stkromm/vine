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

    private boolean enabled;

    @Override
    public int getUniform(final String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }
        final int result = glGetUniformLocation(id, name);
        if (result == -1) {
            Logger.getGlobal().log(Level.SEVERE, "Failed to create shader");
        } else {
            locationCache.put(name, result);
        }
        return result;
    }

    @Override
    public void setUniform1i(final String name, final int value) {
        if (!enabled) {
            bind();
        }
        glUniform1i(getUniform(name), value);
    }

    @Override
    public void setUniform1f(final String name, final float value) {
        if (!enabled) {
            bind();
        }
        glUniform1f(getUniform(name), value);
    }

    @Override
    public void setUniform3f(final String name, final Vector3f vector) {
        if (!enabled) {
            bind();
        }
        glUniform3f(getUniform(name), vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void setUniformMat3f(final String name, final Matrix3f matrix) {
        if (!enabled) {
            bind();
        }
    }

    @Override
    public void setUniformMat4f(final String name, final Matrix4f matrix) {
        if (!enabled) {
            bind();
        }
        glUniformMatrix4fv(getUniform(name), false, BufferConverter.createFloatBuffer(matrix.elements));
    }

    @Override
    public void bind() {
        glUseProgram(id);
        enabled = true;
    }

    @Override
    public void unbind() {
        glUseProgram(0);
        enabled = false;
    }

}