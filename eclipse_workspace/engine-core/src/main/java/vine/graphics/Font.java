package vine.graphics;

import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import vine.assets.Asset;

public class Font implements Texture, Asset
{

    // Constants
    private final Map<Integer, String> CHARS = new HashMap<Integer, String>()
                                             {
                                                 private static final long serialVersionUID = 1L;
                                                 {
                                                     this.put(Integer.valueOf(0), "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                                                     this.put(Integer.valueOf(1), "abcdefghijklmnopqrstuvwxyz");
                                                     this.put(Integer.valueOf(2), "0123456789");
                                                     this.put(Integer.valueOf(3), "ÄÖÜäöüß");
                                                     this.put(Integer.valueOf(4), " $+-*/=%\"'#@&_(),.;:?!\\|<>[]§`^~");
                                                 }
                                             };

    // Variables
    private final java.awt.Font        font;
    private final FontMetrics          fontMetrics;
    private final BufferedImage        bufferedImage;
    private final int                  fontTextureId;
    final Graphics2D                   imageGraphics;

    public Font(final String path, final float size) throws FontFormatException, IOException
    {
        this.font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(path)).deriveFont(size);

        // Generate buffered image
        final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        final Graphics2D graphics = gc.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
        graphics.setFont(this.font);

        this.fontMetrics = graphics.getFontMetrics();
        this.bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage((int) this.getFontImageWidth(),
                (int) this.getFontImageHeight(), Transparency.TRANSLUCENT);
        // Draw the characters on our image
        this.imageGraphics = (Graphics2D) this.bufferedImage.getGraphics();
        this.imageGraphics.setFont(this.font);

        // draw every CHAR by line...
        this.imageGraphics.setColor(java.awt.Color.BLACK);
        this.CHARS.keySet().stream().forEach(i -> this.imageGraphics.drawString(this.CHARS.get(i), 0,
                this.fontMetrics.getMaxAscent() + this.getCharHeight() * i.floatValue()));

        // Generate texture data
        final int[] pixels = new int[this.bufferedImage.getWidth() * this.bufferedImage.getHeight()];
        this.bufferedImage.getRGB(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight(), pixels, 0,
                this.bufferedImage.getWidth());
        // Generate texture

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.fontTextureId = GraphicsProvider.getGraphics().generateTexture();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureId);
        GraphicsProvider.getGraphics().createRgbaTexture2D(this.bufferedImage.getWidth(),
                this.bufferedImage.getHeight(), pixels);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
    }

    public final void changeColor(Color color)
    {
        // draw every CHAR by line...
        this.imageGraphics.setColor(new java.awt.Color(color.getColor()));
        this.CHARS.keySet().stream().forEach(i -> this.imageGraphics.drawString(this.CHARS.get(i), 0,
                this.fontMetrics.getMaxAscent() + this.getCharHeight() * i.floatValue()));

        // Generate texture data
        final int[] pixels = new int[this.bufferedImage.getWidth() * this.bufferedImage.getHeight()];
        this.bufferedImage.getRGB(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight(), pixels, 0,
                this.bufferedImage.getWidth());
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureId);
        GraphicsProvider.getGraphics().createRgbaTexture2D(this.bufferedImage.getWidth(),
                this.bufferedImage.getHeight(), pixels);
    }

    public final float getFontImageWidth()
    {
        return (float) this.CHARS.values().stream()
                .mapToDouble(e -> this.fontMetrics.getStringBounds(e, null).getWidth()).max().getAsDouble();
    }

    public final float getFontImageHeight()
    {
        return this.CHARS.keySet().size() * this.getCharHeight();
    }

    public float getCharX(final char c)
    {
        final String originStr = this.CHARS.values().stream().filter(e -> e.contains(String.valueOf(c))).findFirst()
                .orElse(String.valueOf(c));
        return (float) this.fontMetrics.getStringBounds(originStr.substring(0, originStr.indexOf(c)), null).getWidth();
    }

    public float getCharY(final char c)
    {
        final float lineId = this.CHARS.keySet().stream().filter(i -> this.CHARS.get(i).contains(String.valueOf(c)))
                .findFirst().orElse(Integer.valueOf(0)).floatValue();
        return this.getCharHeight() * lineId;
    }

    public final float getCharWidth(final char c)
    {
        return this.fontMetrics.charWidth(c);
    }

    public final float getCharHeight()
    {
        return this.fontMetrics.getMaxAscent() + this.fontMetrics.getMaxDescent();
    }

    @Override
    public void bind()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureId);
    }

    @Override
    public void unbind()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public int getWidth()
    {
        return (int) this.getFontImageWidth();
    }

    @Override
    public int getHeight()
    {
        return (int) this.getFontImageHeight();
    }
}
