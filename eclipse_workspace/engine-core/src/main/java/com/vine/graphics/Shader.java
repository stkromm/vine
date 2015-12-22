package com.vine.graphics;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import com.vine.math.Transformation;
import com.vine.math.Vector3f;
import com.vine.util.ShaderUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shader {

    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD_ATTRIB = 1;

    private boolean enabled = false;

    private final int id;
    private Map<String, Integer> locationCache = new HashMap<String, Integer>();

    public Shader(String vertex, String fragment) {
        id = ShaderUtils.load(vertex, fragment);
    }

    public static Shader load(String fragShader, String vertShader) {
        return new Shader(vertShader, fragShader);
    }

    /**
     * Tries to get the uniform variable from open-gl by the given name.
     */
    public int getUniform(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }
        int result = glGetUniformLocation(id, name);
        if (result == -1) {
            Logger.getGlobal().log(Level.SEVERE, "Could not find uniform variable '" + name + "'!");
        } else {
            locationCache.put(name, result);
        }
        return result;
    }

    /**
     * Tries to set the uniform variable with the given name and value.
     */
    public void setUniform1i(String name, int value) {
        if (!enabled) {
            enable();
        }
        glUniform1i(getUniform(name), value);
    }

    /**
     * Tries to set the uniform variable with the given name and value.
     */
    public void setUniform1f(String name, float value) {
        if (!enabled) {
            enable();
        }
        glUniform1f(getUniform(name), value);
    }

    /**
     * Tries to set the uniform vector 2 with the given name and value.
     */
    public void setUniform2f(String name, float x, float y) {
        if (!enabled) {
            enable();
        }
        glUniform2f(getUniform(name), x, y);
    }

    /**
     * Tries to set the uniform vector 3 with the given name and value.
     */
    public void setUniform3f(String name, Vector3f vector) {
        if (!enabled) {
            enable();
        }
        glUniform3f(getUniform(name), vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Tries to set the uniform transformation matrix 4x4 with the given name
     * and value.
     */
    public void setUniformMat4f(String name, Transformation matrix) {
        if (!enabled) {
            enable();
        }
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(id);
        enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        enabled = false;
    }

}