package vine.assets;

import vine.graphics.Font;
import vine.util.Log;

public class FontLoader extends AssetLoader<Font, AssetLoaderParameters<Font>>
{
    @Override
    public void loadAsync(
            final AssetPointer pointer,
            final AssetLoaderParameters<Font> parameter,
            final vine.assets.AssetLoader.FinishCallback<Font> callback,
            final vine.assets.AssetLoader.ProgressCallback progessCallback)
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
