package vine.graphics;

/**
 * A texture represents an image object that can be bound to the render
 * pipeline.
 * 
 * @author Steffen
 *
 */
public class Texture2D {

    /**
     * The width in number of texels
     */
    protected final int width;
    /**
     * The height in number of texels
     */
    protected final int height;
    /**
     * Stores the id of the texture in open gl.
     */
    protected final int textureId;

    private final Graphics graphics;

    public Texture2D(final int[] data, final int width, final int height, final Graphics graphics) {
        this.graphics = graphics;
        this.height = height;
        this.width = width;
        textureId = graphics.generateTexture();
        graphics.bindTexture2D(textureId);
        graphics.setTextureParameter(10241, 9728);// GL_TEXTURE_2D,
                                                  // GL_TEXTURE_MIN_FILTER,
                                                  // GL_NEAREST
        graphics.setTextureParameter(10240, 9728);// GL_TEXTURE_2D,
                                                  // GL_TEXTURE_MAG_FILTER,
                                                  // GL_NEAREST
        graphics.createRgbaTexture2D(width, height, data);
    }

    /**
     * 
     */
    void bind() {
        graphics.bindTexture2D(textureId);
    }

    /**
     * 
     */
    void unbind() {
        graphics.bindTexture2D(0);
    }

    /**
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return
     */
    public int getHeight() {
        return height;
    }

    public float[] getUVSquad(final int texX, final int texY, final int texWidth, final int texHeight) {
        final int textureWidth = getWidth();
        final int textureHeight = getHeight();
        final float uvX = texX / (float) textureWidth;
        final float uvY = texY / (float) textureHeight;
        final float uvWidth = texWidth / (float) textureWidth;
        final float uvHeight = texHeight / (float) textureHeight;
        return new float[] { uvX, uvY + uvHeight, uvX, uvY, uvX + uvWidth, uvY, uvX + uvWidth, uvY + uvHeight };
    }
}