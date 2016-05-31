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

import vine.assets.Asset;
import vine.math.GMath;

public class Font implements Texture, Asset
{
    private final Map<Integer, String> CHARS = new HashMap<Integer, String>()
                                             {
                                                 private static final long serialVersionUID = 1L;
                                                 {
                                                     put(Integer.valueOf(0), "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                                                     put(Integer.valueOf(1), "abcdefghijklmnopqrstuvwxyz");
                                                     put(Integer.valueOf(2), "0123456789");
                                                     put(Integer.valueOf(3), "ÄÖÜäöüß");
                                                     put(Integer.valueOf(4), " $+-*/=%\"'#@&_(),.;:?!\\|<>[]§`^~");
                                                 }
                                             };

    private final java.awt.Font        font;
    private final FontMetrics          fontMetrics;
    private final BufferedImage        bufferedImage;
    private final int                  fontTextureId;
    final Graphics2D                   imageGraphics;

    public Font(final String path, final float size) throws FontFormatException, IOException
    {
        font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(path)).deriveFont(size);

        // Generate buffered image
        final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        final Graphics2D graphics = gc.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
        graphics.setFont(font);

        fontMetrics = graphics.getFontMetrics();
        bufferedImage = graphics.getDeviceConfiguration()
                .createCompatibleImage((int) getFontImageWidth(), (int) getFontImageHeight(), Transparency.TRANSLUCENT);
        // Draw the characters on our image
        imageGraphics = (Graphics2D) bufferedImage.getGraphics();
        imageGraphics.setFont(font);

        // draw every CHAR by line...
        imageGraphics.setColor(java.awt.Color.BLACK);
        CHARS.keySet().stream().forEach(
                i -> imageGraphics
                        .drawString(CHARS.get(i), 0, fontMetrics.getMaxAscent() + getCharHeight() * i.floatValue()));

        // Generate texture data
        final int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
        bufferedImage
                .getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());
        // Generate texture

        fontTextureId = GraphicsProvider.getGraphics().generateTexture();
        bind();
        GraphicsProvider.getGraphics().createRgbaTexture2D(bufferedImage.getWidth(), bufferedImage.getHeight(), pixels);
        GraphicsProvider.getGraphics().setTextureFilter(TextureFilter.LINEAR_LINEAR, TextureFilter.NEAREST);
    }

    public final void changeColor(final Color color)
    {
        // draw every CHAR by line...
        imageGraphics.setColor(new java.awt.Color(color.getColor()));
        CHARS.keySet().stream().forEach(
                i -> imageGraphics
                        .drawString(CHARS.get(i), 0, fontMetrics.getMaxAscent() + getCharHeight() * i.floatValue()));

        // Generate texture data
        final int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
        bufferedImage
                .getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());
        bind();
        GraphicsProvider.getGraphics().createRgbaTexture2D(bufferedImage.getWidth(), bufferedImage.getHeight(), pixels);
    }

    public final float getFontImageWidth()
    {
        return (float) CHARS.values().stream().mapToDouble(e -> fontMetrics.getStringBounds(e, null).getWidth()).max()
                .getAsDouble();
    }

    public final float getFontImageHeight()
    {
        return CHARS.keySet().size() * getCharHeight();
    }

    public float getCharX(final char c)
    {
        final String originStr = CHARS.values().stream().filter(e -> e.contains(String.valueOf(c))).findFirst()
                .orElse(String.valueOf(c));
        return (float) fontMetrics.getStringBounds(originStr.substring(0, originStr.indexOf(c)), null).getWidth();
    }

    public float getCharY(final char c)
    {
        final float lineId = CHARS.keySet().stream().filter(i -> CHARS.get(i).contains(String.valueOf(c))).findFirst()
                .orElse(Integer.valueOf(0)).floatValue();
        return getCharHeight() * lineId;
    }

    public final float getCharWidth(final char c)
    {
        return fontMetrics.charWidth(c);
    }

    public final float getCharHeight()
    {
        return fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
    }

    @Override
    public void bind()
    {
        GraphicsProvider.getGraphics().bindTexture2D(fontTextureId);
    }

    @Override
    public void unbind()
    {
        GraphicsProvider.getGraphics().bindTexture2D(0);
    }

    @Override
    public int getWidth()
    {
        return GMath.roundPositive(getFontImageWidth());
    }

    @Override
    public int getHeight()
    {
        return GMath.roundPositive(getFontImageHeight());
    }
}
