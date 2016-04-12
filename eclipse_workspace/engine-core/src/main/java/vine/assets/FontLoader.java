package vine.assets;

import vine.graphics.Font;

public class FontLoader extends AssetLoader<Font, AssetLoaderParameters<Font>> {
    @Override
    public void loadAsync(AssetPointer pointer, AssetLoaderParameters<Font> parameter,
            vine.assets.AssetLoader.FinishCallback<Font> callback,
            vine.assets.AssetLoader.ProgressCallback progessCallback) {
        final Thread loader = new Thread(() -> {
            callback.finished(this.loadSync(pointer, parameter));
        });
        loader.start();
    }

    @Override
    public Font loadSync(AssetPointer pointer, AssetLoaderParameters<Font> parameter) {
        try {
            return new Font(pointer.path, 20);
        } catch (final Exception exception) {
            return null;
        }
    }

}
