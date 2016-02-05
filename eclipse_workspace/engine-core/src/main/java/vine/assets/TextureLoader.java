package vine.assets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import vine.game.Game;
import vine.graphics.Texture2D;
import vine.util.FileUtils;

public class TextureLoader extends AssetLoader<Texture2D, AssetLoaderParameters<Texture2D>> {

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file,
            AssetLoaderParameters<Texture2D> parameter) {
        // TODO Auto-generated method stub

    }

    @Override
    public Texture2D loadSync(AssetManager manager, String fileName, FileHandle file,
            AssetLoaderParameters<Texture2D> parameter) {
        BufferedImage image = null;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(FileUtils.readBytes(fileName))) {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Failed to read texture source file", e);
        }
        final int width = image.getWidth();
        final int height = image.getHeight();
        final int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        // rgba array data
        final int[] data = new int[width * height];
        for (int i = pixels.length - 1; i >= 0; i--) {
            final int alpha = (pixels[i] & 0xff000000) >> 24;
            final int red = (pixels[i] & 0xff0000) >> 16;
            final int green = (pixels[i] & 0xff00) >> 8;
            final int blue = pixels[i] & 0xff;
            data[i] = alpha << 24 | blue << 16 | green << 8 | red;
        }
        return new Texture2D(data, width, height, Game.getGame().getGraphics());
    }

}
