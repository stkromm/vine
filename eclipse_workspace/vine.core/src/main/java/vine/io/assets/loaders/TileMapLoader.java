package vine.io.assets.loaders;

import vine.game.tilemap.TileMap;
import vine.io.assets.AssetPointer;
import vine.io.assets.loaders.AssetLoader.FinishCallback;
import vine.io.assets.loaders.AssetLoader.ProgressCallback;

public class TileMapLoader extends AssetLoader<TileMap, AssetLoaderParameters<TileMap>>
{

    @Override
    public void loadAsync(
            final AssetPointer pointer,
            final AssetLoaderParameters<TileMap> parameter,
            final vine.io.assets.loaders.AssetLoader.FinishCallback<TileMap> callback,
            final vine.io.assets.loaders.AssetLoader.ProgressCallback progessCallback)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public TileMap loadSync(final AssetPointer pointer, final AssetLoaderParameters<TileMap> parameter)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
