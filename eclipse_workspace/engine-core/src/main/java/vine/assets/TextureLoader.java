package vine.assets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import vine.graphics.Texture2D;
import vine.util.FileUtils;

public class TextureLoader extends AssetLoader<Texture2D, AssetLoaderParameters<Texture2D>> {

    @Override
    public Texture2D loadSync(AssetPointer pointer, final AssetLoaderParameters<Texture2D> parameter) {
        BufferedImage image = null;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(FileUtils.readBytes(pointer.path))) {
            image = ImageIO.read(stream);
        } catch (final IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Failed to read texture source file", e);
        }
        if (image == null) {
            return null;
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
        return new Texture2D(data, width, height);
    }

    @Override
    public void loadAsync(AssetPointer pointer, AssetLoaderParameters<Texture2D> parameter,
            vine.assets.AssetLoader.FinishCallback<Texture2D> callback,
            vine.assets.AssetLoader.ProgressCallback progessCallback) {
        // TODO Auto-generated method stub

    }

}
