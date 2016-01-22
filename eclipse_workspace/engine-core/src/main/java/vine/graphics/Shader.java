package vine.graphics;

import java.util.Map;
import java.util.WeakHashMap;

import vine.math.Matrix3f;
import vine.math.Matrix4f;
import vine.math.Vector3f;
import vine.util.ShaderLoader;

/**
 * @author Steffen
 *
 */
public abstract class Shader {
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

    /**
     * 
     */
    protected Map<String, Integer> locationCache = new WeakHashMap<>();

    /**
     * @param vertPath
     *            The file path to the vertex shader code
     * @param fragPath
     *            The file path to the fragment shader code
     */
    public Shader(String vertPath, String fragPath) {
        id = ShaderLoader.load(vertPath, fragPath);
        setProperties();
    }

    /**
     * 
     */
    public Shader() {
        id = ShaderLoader.load();
        setProperties();
    }

    private void setProperties() {
        setUniform1i("tex", 1);
        float zoom = 1;
        int viewportWidth = 1024;
        int viewportHeight = 720;
        Matrix4f mat = Matrix4f.orthographic(0, zoom * viewportWidth, 0, zoom * viewportHeight, -1, 1);
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
    public abstract int getUniform(String name);

    /**
     * Tries to set the uniform variable with the given name and value.
     * 
     * @param name
     *            uniform name
     * @param value
     *            the to store uniform
     */
    public abstract void setUniform1i(String name, int value);

    /**
     * Tries to set the uniform variable with the given name and value.
     * 
     * @param name
     *            uniform name
     * @param value
     *            the to store uniform
     */
    public abstract void setUniform1f(String name, float value);

    /**
     * Tries to set the uniform vector 3 with the given name and value.
     * 
     * @param name
     *            uniform name
     * @param vector
     *            the vector that should be stored in the uniform
     */
    public abstract void setUniform3f(String name, Vector3f vector);

    /**
     * Tries to set the uniform transformation matrix 3x3 with the given name
     * and value.
     * 
     * @param name
     *            uniform name
     * @param matrix
     *            the matrix that should be stored in the uniform
     */
    public abstract void setUniformMat3f(String name, Matrix3f matrix);

    /**
     * @param name
     *            uniform name
     * @param matrix
     *            the matrix that should be stored in the uniform
     */
    public abstract void setUniformMat4f(String name, Matrix4f matrix);

    public abstract void bind();

    public abstract void unbind();
}