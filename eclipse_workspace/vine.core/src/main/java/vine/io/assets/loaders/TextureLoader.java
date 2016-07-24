package vine.io.assets.loaders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import vine.graphics.RgbaTexture;
import vine.io.assets.AssetPointer;
import vine.io.assets.loaders.AssetLoader.FinishCallback;
import vine.io.assets.loaders.AssetLoader.ProgressCallback;
import vine.util.FileUtils;
import vine.util.Log;

public class TextureLoader extends AssetLoader<RgbaTexture, AssetLoaderParameters<RgbaTexture>>
{

    @Override
    public RgbaTexture loadSync(final AssetPointer pointer, final AssetLoaderParameters<RgbaTexture> parameter)
    {
        BufferedImage image = null;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(FileUtils.readBytes(pointer.getPath())))
        {
            image = ImageIO.read(stream);
        } catch (final IOException e)
        {
            Log.exception("Failed to read texture source file", e);
        }
        if (image == null)
        {
            return null;
        }
        final int width = image.getWidth();
        final int height = image.getHeight();
        final int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        // rgba array data
        final int[] data = new int[width * height];
        for (int i = pixels.length - 1; i >= 0; i--)
        {
            final int alpha = (pixels[i] & 0xff000000) >> 24;
            final int red = (pixels[i] & 0xff0000) >> 16;
            final int green = (pixels[i] & 0xff00) >> 8;
            final int blue = pixels[i] & 0xff;
            data[i] = alpha << 24 | blue << 16 | green << 8 | red;
        }
        return new RgbaTexture(data, width, height);
    }

    @Override
    public void loadAsync(
            final AssetPointer pointer,
            final AssetLoaderParameters<RgbaTexture> parameter,
            final vine.io.assets.loaders.AssetLoader.FinishCallback<RgbaTexture> callback,
            final vine.io.assets.loaders.AssetLoader.ProgressCallback progessCallback)
    {
        // TODO Auto-generated method stub

    }

}
