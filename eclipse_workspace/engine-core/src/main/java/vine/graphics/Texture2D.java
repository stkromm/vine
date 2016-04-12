package vine.graphics;

import org.lwjgl.opengl.GL11;

import vine.assets.Asset;

/**
 * A texture represents an image object that can be bound to the render
 * pipeline.
 * 
 * @author Steffen
 *
 */
public class Texture2D implements BindableTexture, Asset {

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

    public Texture2D(final int[] data, final int width, final int height) {
        this.graphics = GraphicsProvider.getGraphics();
        this.height = height;
        this.width = width;
        this.textureId = this.graphics.generateTexture();
        this.graphics.bindTexture2D(this.textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        this.graphics.createRgbaTexture2D(width, height, data);
    }

    @Override
    public void bind() {
        this.graphics.bindTexture2D(this.textureId);
    }

    @Override
    public void unbind() {
        this.graphics.bindTexture2D(0);
    }

    /**
     * @return
     */
    @Override
    public int getWidth() {
        return this.width;
    }

    /**
     * @return
     */
    @Override
    public int getHeight() {
        return this.height;
    }

    public float[] getUVSquad(final int texX, final int texY, final int texWidth, final int texHeight) {
        final int textureWidth = this.getWidth();
        final int textureHeight = this.getHeight();
        final float uvX = texX / (float) textureWidth;
        final float uvY = texY / (float) textureHeight;
        final float uvWidth = texWidth / (float) textureWidth;
        final float uvHeight = texHeight / (float) textureHeight;
        return new float[] { uvX, uvY + uvHeight, uvX, uvY, uvX + uvWidth, uvY, uvX + uvWidth, uvY + uvHeight };
    }
}