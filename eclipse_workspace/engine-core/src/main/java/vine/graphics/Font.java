package vine.graphics;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import vine.assets.Asset;

public class Font implements BindableTexture, Asset {

    // Constants
    private final Map<Integer, String> CHARS = new HashMap<Integer, String>() {
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
    private final java.awt.Font font;
    private final FontMetrics fontMetrics;
    private final BufferedImage bufferedImage;
    private final int fontTextureId;

    // Getters
    public float getFontImageWidth() {
        return (float) this.CHARS.values().stream()
                .mapToDouble(e -> this.fontMetrics.getStringBounds(e, null).getWidth()).max().getAsDouble();
    }

    public float getFontImageHeight() {
        return this.CHARS.keySet().size() * this.getCharHeight();
    }

    public float getCharX(char c) {
        final String originStr = this.CHARS.values().stream().filter(e -> e.contains("" + c)).findFirst()
                .orElse("" + c);
        return (float) this.fontMetrics.getStringBounds(originStr.substring(0, originStr.indexOf(c)), null).getWidth();
    }

    public float getCharY(char c) {
        final float lineId = this.CHARS.keySet().stream().filter(i -> this.CHARS.get(i).contains("" + c)).findFirst()
                .orElse(Integer.valueOf(0)).floatValue();
        return this.getCharHeight() * lineId;
    }

    public float getCharWidth(char c) {
        return this.fontMetrics.charWidth(c);
    }

    public float getCharHeight() {
        return this.fontMetrics.getMaxAscent() + this.fontMetrics.getMaxDescent();
    }

    // Constructors
    public Font(String path, float size) throws Exception {
        this.font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(path)).deriveFont(size);

        // Generate buffered image
        final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        final Graphics2D graphics = gc.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
        graphics.setFont(this.font);

        this.fontMetrics = graphics.getFontMetrics();
        this.bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage((int) this.getFontImageWidth(),
                (int) this.getFontImageHeight(), Transparency.TRANSLUCENT);

        // Generate texture
        this.fontTextureId = GL11.glGenTextures();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureId);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, (int) this.getFontImageWidth(),
                (int) this.getFontImageHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.asByteBuffer());

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    }

    @Override
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureId);
    }

    @Override
    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public int getWidth() {
        return (int) this.getFontImageWidth();
    }

    @Override
    public int getHeight() {
        return (int) this.getFontImageHeight();
    }

    // Conversions
    public ByteBuffer asByteBuffer() {

        ByteBuffer byteBuffer;

        // Draw the characters on our image
        final Graphics2D imageGraphics = (Graphics2D) this.bufferedImage.getGraphics();
        imageGraphics.setFont(this.font);
        imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // draw every CHAR by line...
        imageGraphics.setColor(java.awt.Color.BLACK);
        this.CHARS.keySet().stream().forEach(i -> imageGraphics.drawString(this.CHARS.get(i), 0,
                this.fontMetrics.getMaxAscent() + this.getCharHeight() * i.floatValue()));

        // Generate texture data
        final int[] pixels = new int[this.bufferedImage.getWidth() * this.bufferedImage.getHeight()];
        this.bufferedImage.getRGB(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight(), pixels, 0,
                this.bufferedImage.getWidth());
        byteBuffer = ByteBuffer.allocateDirect(this.bufferedImage.getWidth() * this.bufferedImage.getHeight() * 4);

        for (int y = 0; y < this.bufferedImage.getHeight(); y++) {
            for (int x = 0; x < this.bufferedImage.getWidth(); x++) {
                final int pixel = pixels[y * this.bufferedImage.getWidth() + x];
                byteBuffer.put((byte) (pixel >> 16 & 0xFF)); // Red component
                byteBuffer.put((byte) (pixel >> 8 & 0xFF)); // Green component
                byteBuffer.put((byte) (pixel & 0xFF)); // Blue component
                byteBuffer.put((byte) (pixel >> 24 & 0xFF)); // Alpha component.
                                                             // Only for RGBA
            }
        }

        byteBuffer.flip();

        return byteBuffer;
    }
}
