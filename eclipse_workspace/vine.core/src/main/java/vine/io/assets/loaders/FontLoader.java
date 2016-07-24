package vine.io.assets.loaders;

import vine.graphics.Font;
import vine.io.assets.AssetPointer;
import vine.io.assets.loaders.AssetLoader.FinishCallback;
import vine.io.assets.loaders.AssetLoader.ProgressCallback;
import vine.util.Log;

public class FontLoader extends AssetLoader<Font, AssetLoaderParameters<Font>>
{
    @Override
    public void loadAsync(
            final AssetPointer pointer,
            final AssetLoaderParameters<Font> parameter,
            final vine.io.assets.loaders.AssetLoader.FinishCallback<Font> callback,
            final vine.io.assets.loaders.AssetLoader.ProgressCallback progessCallback)
    {
        final Thread loader = new Thread(() ->
        {
            callback.finished(this.loadSync(pointer, parameter));
        });
        loader.start();
    }

    @Override
    public Font loadSync(final AssetPointer pointer, final AssetLoaderParameters<Font> parameter)
    {
        try
        {
            return new Font(pointer.getPath(), 18);
        } catch (final Exception exception)
        {
            Log.exception("Auto-generated catch block", exception);
        }
        return null;
    }

}
