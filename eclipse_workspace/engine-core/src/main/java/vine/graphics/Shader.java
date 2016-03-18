package vine.graphics;

import java.util.Map;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.math.Matrix4f;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public class Shader {
    /**
     * Used logger for gameplay logs.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(Shader.class);
    /**
     * 
     */
    private static final int VIEWPORT_WIDTH = 1024;
    /**
     * 
     */
    private static final int VIEWPORT_HEIGHT = 720;
    /**
     * 
     */
    public static final int VERTEX_ATTRIB = 0;
    /**
     * 
     */
    public static final int TCOORD_ATTRIB = 1;
    /**
     * 
     */
    protected final int id;

    private boolean enabled;
    /**
     * 
     */
    protected final Map<String, Integer> locationCache = new WeakHashMap<>();

    private final Graphics graphics;

    /**
     * @param vertPath
     *            The file path to the vertex shader code
     * @param fragPath
     *            The file path to the fragment shader code
     */
    public Shader(final int id) {
        this.id = id;
        this.graphics = GraphicsProvider.getGraphics();
        setProperties();
    }

    private void setProperties() {
        setUniform1i("tex", 1);
        final Matrix4f mat = Matrix4f.orthographic(0, VIEWPORT_WIDTH, 0, VIEWPORT_HEIGHT, -1, 1);
        setUniformMat4f("pr_matrix", mat);
        setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(0.f, 0.0f, 0.0f)));
    }

    /**
     * Tries to get the uniform variable from open-gl by the given name.
     * 
     * @param name
     *            uniform name
     * @return the stored uniform
     */
    public int getUniform(final String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }
        final int result = graphics.getUniformLocation(id, name);
        if (result == -1) {
            LOGGER.error("Failed to create shader");
        } else {
            locationCache.put(name, result);
        }
        return result;
    }

    /**
     * Tries to set the uniform variable with the given name and value.
     * 
     * @param name
     *            uniform name
     * @param value
     *            the to store uniform
     */
    public void setUniform1i(final String name, final int value) {
        if (!enabled) {
            graphics.bindShader(id);
        }
        graphics.storeUniformInt(getUniform(name), value);
    }

    /**
     * Tries to set the uniform vector 3 with the given name and value.
     * 
     * @param name
     *            uniform name
     * @param vector
     *            the vector that should be stored in the uniform
     */
    public void setUniform3f(final String name, final Vector3f vector) {
        if (!enabled) {
            graphics.bindShader(id);
        }
        graphics.storeUniformVector3f(getUniform(name), vector);
    }

    /**
     * @param name
     *            uniform name
     * @param matrix
     *            the matrix that should be stored in the uniform
     */
    public void setUniformMat4f(final String name, final Matrix4f matrix) {
        if (!enabled) {
            graphics.bindShader(id);
        }
        graphics.storeUniformMatrix4f(getUniform(name), matrix);
    }

    public void bind() {
        graphics.bindShader(id);
        enabled = true;
    }

    public void unbind() {
        graphics.bindShader(0);
        enabled = false;
    }
}