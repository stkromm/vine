package vine.assets;

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

import vine.game.Game;
import vine.graphics.Shader;

public class ShaderLoader extends AssetLoader<Shader, AssetLoaderParameters<Shader>> {

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file,
            AssetLoaderParameters<Shader> parameter) {
        // TODO Auto-generated method stub

    }

    @Override
    public Shader loadSync(AssetManager manager, String fileName, FileHandle file,
            AssetLoaderParameters<Shader> parameter) {
        final String frag = "#version 330 core\n" + "layout (location = 0) out vec4 color;" + "in DATA{"
                + "  vec2 tc;       vec3 position;" + "} fs_in;" + "uniform sampler2D tex;\n" + "void main() {\n"
                + " color = texture(tex, fs_in.tc);\n" + "}";
        final String vert = "#version 330 core \n layout(location = 0)\n "
                + "in vec4 position;layout (location = 1) in vec2 tc;\n"
                + "uniform mat4 pr_matrix;uniform mat4 vw_matrix;out DATA{vec2 tc;vec3 position;}\n "
                + "vs_out;void main(){gl_Position = pr_matrix * vw_matrix * position;vs_out.tc = tc;\n"
                + "vs_out.position = vec3(vw_matrix * position);}";
        final int vertId = glCreateShader(GL_VERTEX_SHADER);
        final int fragId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertId, vert);
        glShaderSource(fragId, frag);
        glCompileShader(vertId);
        if (glGetShaderi(vertId, GL_COMPILE_STATUS) == GL_FALSE) {
            // Could not load vertex shader
            Logger.getGlobal().log(Level.SEVERE, "Failed to compile vertex shader!" + glGetShaderInfoLog(vertId));
        }

        glCompileShader(fragId);
        if (glGetShaderi(fragId, GL_COMPILE_STATUS) == GL_FALSE) {
            // Could not load fragment shader
            Logger.getGlobal().log(Level.SEVERE, "Failed to compile fragment shader!" + glGetShaderInfoLog(fragId));
        }
        final int program = glCreateProgram();
        glAttachShader(program, vertId);
        glAttachShader(program, fragId);
        glLinkProgram(program);
        glValidateProgram(program);
        glDeleteShader(vertId);
        glDeleteShader(fragId);
        return new Shader(program, Game.getGame().getGraphics());
    }
}
