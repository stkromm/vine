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
public class Image implements Texture, Asset
{

    /**
     * The width in number of texels
     */
    protected final int    width;
    /**
     * The height in number of texels
     */
    protected final int    height;
    /**
     * Stores the id of the texture in open gl.
     */
    protected final int    textureId;

    private final Graphics graphics;

    public Image(final int[] data, final int width, final int height)
    {
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
    public void bind()
    {
        this.graphics.bindTexture2D(this.textureId);
    }

    @Override
    public void unbind()
    {
        this.graphics.bindTexture2D(0);
    }

    /**
     * @return
     */
    @Override
    public int getWidth()
    {
        return this.width;
    }

    /**
     * @return
     */
    @Override
    public int getHeight()
    {
        return this.height;
    }

    public float[] getPackedUVSquad(final int texX, final int texY, final int texWidth, final int texHeight)
    {
        final int textureWidth = this.getWidth();
        final int textureHeight = this.getHeight();
        final float uvX = texX / (float) textureWidth;
        final float uvY = texY / (float) textureHeight;
        final float uvWidth = texWidth / (float) textureWidth;
        final float uvHeight = texHeight / (float) textureHeight;
        final float[] floatUVs = new float[] { uvX, uvY + uvHeight, uvX, uvY, uvX + uvWidth, uvY, uvX + uvWidth,
                uvY + uvHeight };
        for (int i = floatUVs.length - 1; i >= 0; i--)
        {
            floatUVs[i] *= 1000;
        }
        return floatUVs;
        /*
         * final float[] packedUVs = new float[4]; for (int i = 0; i < 4; i++) {
         * final int upperValue = Math.round(floatUVs[i * 2]) << 16 &
         * 0xFFFF0000; final int lowerValue = Math.round(floatUVs[i * 2 + 1]) &
         * 0x0000FFFF; final int result = upperValue | lowerValue; packedUVs[i]
         * = result; } return packedUVs;
         */}
}