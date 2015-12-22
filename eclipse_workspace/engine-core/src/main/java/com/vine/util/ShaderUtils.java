package com.vine.util;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShaderUtils {

    private ShaderUtils() {
    }

    /**
     * Loads the given shader files and creates a new shader with them.
     */
    public static int load(String vertPath, String fragPath) {
        String vert = FileUtils.loadAsString(vertPath);
        String frag = FileUtils.loadAsString(fragPath);
        return create(vert, frag);
    }

    /**
     * Creates a new shader in open-gl and returns its id.
     */
    public static int create(String vert, String frag) {
        int vertId = glCreateShader(GL_VERTEX_SHADER);
        int fragId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertId, vert);
        glShaderSource(fragId, frag);

        glCompileShader(vertId);
        if (glGetShaderi(vertId, GL_COMPILE_STATUS) == GL_FALSE) {
            // Could not load vertex shader
            Logger.getGlobal().log(Level.SEVERE, "Failed to compile vertex shader!" + glGetShaderInfoLog(vertId));
            return -1;
        }

        glCompileShader(fragId);
        if (glGetShaderi(fragId, GL_COMPILE_STATUS) == GL_FALSE) {
            // Could not load fragment shader
            Logger.getGlobal().log(Level.SEVERE, "Failed to compile fragment shader!" + glGetShaderInfoLog(fragId));
            return -1;
        }
        int program = glCreateProgram();
        glAttachShader(program, vertId);
        glAttachShader(program, fragId);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(vertId);
        glDeleteShader(fragId);

        return program;
    }

}