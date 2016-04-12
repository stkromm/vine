package vine.assets;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import vine.graphics.Shader;

/**
 * @author Steffen
 *
 */
public class ShaderLoader extends AssetLoader<Shader, AssetLoaderParameters<Shader>> {

    @Override
    public Shader loadSync(AssetPointer pointer, final AssetLoaderParameters<Shader> parameter) {
        final String frag = "#version 330 core\nlayout (location = 0) out vec4 color;in DATA{"
                + "vec4 color;vec2 tc;vec3 position;" + "} fs_in;" + "uniform sampler2D tex;\n" + "void main() {\n"
                + " color = texture(tex, fs_in.tc); if(color.a == 0) discard; color += fs_in.color;\n}";
        final String vert = "#version 330 core\nlayout(location = 0)\n "
                + "in vec4 position;layout (location = 1) in vec2 tc;layout (location = 2) in vec4 color;\n"
                + "uniform mat4 pr_matrix;uniform mat4 vw_matrix;out DATA{vec4 color;vec2 tc;vec3 position;}\n "
                + "vs_out;void main(){gl_Position = pr_matrix * vw_matrix * position;vs_out.tc = tc;vs_out.color = color;\n"
                + "vs_out.position = vec3(vw_matrix * position);}";
        final int vertId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        final int fragId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(vertId, vert);
        GL20.glShaderSource(fragId, frag);
        GL20.glCompileShader(vertId);
        if (GL20.glGetShaderi(vertId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            // Could not load vertex shader
            Logger.getGlobal().log(Level.SEVERE, "Failed to compile vertex shader!" + GL20.glGetShaderInfoLog(vertId));
        }

        GL20.glCompileShader(fragId);
        if (GL20.glGetShaderi(fragId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            // Could not load fragment shader
            Logger.getGlobal().log(Level.SEVERE,
                    "Failed to compile fragment shader!" + GL20.glGetShaderInfoLog(fragId));
        }
        final int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertId);
        GL20.glAttachShader(program, fragId);
        GL20.glLinkProgram(program);
        GL20.glValidateProgram(program);
        GL20.glDeleteShader(vertId);
        GL20.glDeleteShader(fragId);
        return new Shader(program);
    }

    @Override
    public void loadAsync(AssetPointer pointer, AssetLoaderParameters<Shader> parameter,
            vine.assets.AssetLoader.FinishCallback<Shader> callback,
            vine.assets.AssetLoader.ProgressCallback progessCallback) {
        // TODO Auto-generated method stub

    }

}
