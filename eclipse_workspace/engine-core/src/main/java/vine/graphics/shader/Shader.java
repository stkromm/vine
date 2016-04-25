package vine.graphics.shader;

import java.util.Map;
import java.util.WeakHashMap;

import vine.assets.Asset;
import vine.graphics.Graphics;
import vine.graphics.GraphicsProvider;
import vine.math.Mat4f;
import vine.math.Vec3f;
import vine.util.Log;

/**
 * @author Steffen
 *
 */
public class Shader implements Asset
{
    /**
     * 
     */
    private static final int             VIEWPORT_WIDTH  = 1024;
    /**
     * 
     */
    private static final int             VIEWPORT_HEIGHT = 720;
    /**
     * 
     */
    public static final int              VERTEX_ATTRIB   = 0;
    /**
     * 
     */
    public static final int              TCOORD_ATTRIB   = 1;
    /**
     * 
     */
    public static final int              COLOR_ATTRIB    = 2;
    /**
     * 
     */
    protected final int                  id;

    private boolean                      enabled;
    /**
     * 
     */
    protected final Map<String, Integer> locationCache   = new WeakHashMap<>();

    private final Graphics               graphics;

    /**
     * @param vertPath
     *            The file path to the vertex shader code
     * @param fragPath
     *            The file path to the fragment shader code
     */
    public Shader(final int id)
    {
        this.id = id;
        this.graphics = GraphicsProvider.getGraphics();
        this.setProperties();
    }

    private void setProperties()
    {
        this.setUniform1i("tex", 1);
        final Mat4f mat = Mat4f.orthographic(0, Shader.VIEWPORT_WIDTH, 0, Shader.VIEWPORT_HEIGHT, -1, 1);
        this.setUniformMat4f("pr_matrix", mat);
        this.setUniformMat4f("vw_matrix", Mat4f.translate(new Vec3f(0.f, 0.0f, 0.0f)));
    }

    /**
     * Tries to get the uniform variable from open-gl by the given name.
     * 
     * @param name
     *            uniform name
     * @return the stored uniform
     */
    public int getUniform(final String name)
    {
        if (this.locationCache.containsKey(name))
        {
            return this.locationCache.get(name).intValue();
        }
        final int result = this.graphics.getUniformLocation(this.id, name);
        if (result == -1)
        {
            Log.lifecycle("Failed to create shader");
        } else
        {
            this.locationCache.put(name, Integer.valueOf(result));
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
    public void setUniform1i(final String name, final int value)
    {
        if (!this.enabled)
        {
            this.graphics.bindShader(this.id);
        }
        this.graphics.storeUniformInt(this.getUniform(name), value);
    }

    /**
     * Tries to set the uniform vector 3 with the given name and value.
     * 
     * @param name
     *            uniform name
     * @param vector
     *            the vector that should be stored in the uniform
     */
    public void setUniform3f(final String name, final Vec3f vector)
    {
        if (!this.enabled)
        {
            this.graphics.bindShader(this.id);
        }
        this.graphics.storeUniformVector3f(this.getUniform(name), vector);
    }

    /**
     * @param name
     *            uniform name
     * @param matrix
     *            the matrix that should be stored in the uniform
     */
    public void setUniformMat4f(final String name, final Mat4f matrix)
    {
        if (!this.enabled)
        {
            this.graphics.bindShader(this.id);
        }
        this.graphics.storeUniformMatrix4f(this.getUniform(name), matrix);
    }

    public void setUniformMat4f(final ShaderUniforms name, final Mat4f matrix)
    {
        if (!this.enabled)
        {
            this.graphics.bindShader(this.id);
        }
        this.graphics.storeUniformMatrix4f(this.getUniform(name.toString()), matrix);
    }

    public void bind()
    {
        this.graphics.bindShader(this.id);
        this.enabled = true;
    }

    public void unbind()
    {
        this.graphics.bindShader(0);
        this.enabled = false;
    }
}