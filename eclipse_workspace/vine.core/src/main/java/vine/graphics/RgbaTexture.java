package vine.graphics;

import vine.io.assets.Asset;

/**
 * A texture represents an image object that can be bound to the render
 * pipeline.
 * 
 * @author Steffen
 *
 */
public class RgbaTexture implements Texture, Asset
{
    protected WrapMode      yWrapping;
    protected WrapMode      xWrapping;
    protected TextureFilter minFilter = TextureFilter.LINEAR_LINEAR;
    protected TextureFilter magFilter = TextureFilter.NEAREST;
    /**
     * The width in number of texels
     */
    protected final int     width;
    /**
     * The height in number of texels
     */
    protected final int     height;
    /**
     * Stores the id of the texture in open gl.
     */
    protected final int     textureId;

    private final int[]     pixel;

    public RgbaTexture(final int[] data, final int width, final int height)
    {
        this.height = height;
        this.width = width;
        textureId = GraphicsProvider.getGraphics().generateTexture();
        pixel = data;
        applyChanges();
    }

    public void applyChanges()
    {
        bind();
        GraphicsProvider.getGraphics().setTextureFilter(magFilter, minFilter);
        GraphicsProvider.getGraphics().createRgbaTexture2D(width, height, pixel);
    }

    @Override
    public void bind()
    {
        GraphicsProvider.getGraphics().bindTexture2D(textureId);
    }

    @Override
    public void unbind()
    {
        GraphicsProvider.getGraphics().bindTexture2D(0);
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    public int getPixel(final int x, final int y)
    {
        return pixel[y * width + x];
    }

    public void setPixel(final int x, final int y, final int color)
    {
        pixel[y * width + x] = color;
    }

    public float[] getUvQuad(final int texX, final int texY, final int texWidth, final int texHeight)
    {
        final int textureWidth = getWidth();
        final int textureHeight = getHeight();
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