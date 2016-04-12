package vine.assets;

import vine.tilemap.TileMap;

public class TileMapLoader extends AssetLoader<TileMap, AssetLoaderParameters<TileMap>> {

    @Override
    public void loadAsync(AssetPointer pointer, AssetLoaderParameters<TileMap> parameter,
            vine.assets.AssetLoader.FinishCallback<TileMap> callback,
            vine.assets.AssetLoader.ProgressCallback progessCallback) {
        // TODO Auto-generated method stub

    }

    @Override
    public TileMap loadSync(AssetPointer pointer, AssetLoaderParameters<TileMap> parameter) {
        // TODO Auto-generated method stub
        return null;
    }

}
