package vine.game.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.application.PerformanceMonitor;
import vine.assets.AssetManager;
import vine.graphics.BindableTexture;
import vine.graphics.Font;
import vine.time.TimerManager;

public class Text extends Widget {
    static final Logger LOGGER = LoggerFactory.getLogger(Text.class);
    Font font;
    String content = "";
    int count;

    float[] positions;
    float[] uvs;
    float[] colors = new float[this.content.length() * 16];

    public void construct() {
        try {
            this.font = AssetManager.loadSync("font", Font.class);
            AssetManager.loadAsync("font", Font.class, font -> this.font = font);
        } catch (final Exception exception) {
            Text.LOGGER.debug("Failed to load font");
        }
        this.positions = this.getPositions(this.content, 0, 100);
        this.uvs = this.getUVs(this.content);
        TimerManager.get().createTimer(1, -1, () -> {
            this.setText("Current fps:" + PerformanceMonitor.getFPS());
        });
    }

    public void setText(String text) {
        this.count = text.length();
        this.content = text;
        this.colors = new float[this.content.length() * 16];
        this.positions = this.getPositions(this.content, 0, 100);
        this.uvs = this.getUVs(this.content);
    }

    @Override
    public float[] getPosition() {
        return this.positions;
    }

    @Override
    public float[] getColor() {
        return this.colors;
    }

    @Override
    public float[] getTextureCoords() {
        return this.uvs;
    }

    @Override
    public int getCount() {
        return 64;
    }

    private float[] getUVs(String text) {
        final float[] uvs = new float[8 * text.toCharArray().length];
        int i = 0;
        for (final char c : text.toCharArray()) {
            final float cw = this.font.getCharWidth(c);
            final float ch = this.font.getCharHeight();
            final float cx = this.font.getCharX(c);
            final float cy = this.font.getCharY(c);
            uvs[i * 8 + 0] = cx;
            uvs[i * 8 + 1] = cy + ch;
            uvs[i * 8 + 2] = cx;
            uvs[i * 8 + 3] = cy;
            uvs[i * 8 + 4] = cx + cw;
            uvs[i * 8 + 5] = cy;
            uvs[i * 8 + 6] = cx + cw;
            uvs[i * 8 + 7] = cy + ch;
            i++;
        }
        for (int a = uvs.length - 1; a >= 0; a--) {
            if (a % 2 == 0) {
                uvs[a] /= this.font.getFontImageWidth();
            } else {
                uvs[a] /= this.font.getFontImageHeight();
            }
        }
        return uvs;
    }

    private float[] getPositions(String text, int x, int y) {
        final float[] uvs = new float[12 * text.toCharArray().length];
        int i = 0;
        float xTmp = x;
        for (final char c : text.toCharArray()) {
            final float width = this.font.getCharWidth(c);
            final float height = this.font.getCharHeight();
            uvs[i * 12 + 0] = xTmp;
            uvs[i * 12 + 1] = y;
            uvs[i * 12 + 2] = 0.9f;
            uvs[i * 12 + 3] = xTmp;
            uvs[i * 12 + 4] = y + height;
            uvs[i * 12 + 5] = 0.9f;
            uvs[i * 12 + 6] = xTmp + width;
            uvs[i * 12 + 7] = y + height;
            uvs[i * 12 + 8] = 0.9f;
            uvs[i * 12 + 9] = xTmp + width;
            uvs[i * 12 + 10] = y;
            uvs[i * 12 + 11] = 0.9f;
            i++;
            xTmp += width;
        }
        return uvs;
    }

    @Override
    public BindableTexture getTexture() {
        return this.font;
    }
}
