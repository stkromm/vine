package vine.assets;

import vine.game.tilemap.TileMap;

public class TileMapLoader extends AssetLoader<TileMap, AssetLoaderParameters<TileMap>>
{

    @Override
    public void loadAsync(
            final AssetPointer pointer,
            final AssetLoaderParameters<TileMap> parameter,
            final vine.assets.AssetLoader.FinishCallback<TileMap> callback,
            final vine.assets.AssetLoader.ProgressCallback progessCallback)
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
