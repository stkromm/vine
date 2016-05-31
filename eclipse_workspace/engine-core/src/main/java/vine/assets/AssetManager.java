package vine.assets;

import java.util.HashMap;
import java.util.Map;

import vine.assets.AssetLoader.FinishCallback;
import vine.graphics.Font;
import vine.graphics.RgbaImage;
import vine.graphics.shader.Shader;
import vine.sound.SoundClip;

public class AssetManager
{
    private static final Map<String, Map<String, ObservedAsset<?>>> ASSETS_BY_TYPE = new HashMap<String, Map<String, ObservedAsset<?>>>();

    private static AssetTable                                       assetTable   = new AssetTable("res/assets");
    static
    {
        AssetManager.ASSETS_BY_TYPE.put(RgbaImage.class.getName(), new HashMap<String, ObservedAsset<?>>());
        AssetManager.ASSETS_BY_TYPE.put(Shader.class.getName(), new HashMap<String, ObservedAsset<?>>());
        AssetManager.ASSETS_BY_TYPE.put(Font.class.getName(), new HashMap<String, ObservedAsset<?>>());
        AssetManager.ASSETS_BY_TYPE.put(SoundClip.class.getName(), new HashMap<String, ObservedAsset<?>>());
    }

    public static <T extends Asset> T loadSync(final String assetName, final Class<T> type)
    {
        final Map<String, ObservedAsset<?>> assetsOfType = AssetManager.ASSETS_BY_TYPE.get(type.getName());
        final ObservedAsset<?> asset = assetsOfType.get(assetName);

        if (asset != null)
        {
            return type.cast(asset.getAsset());
        } else
        {
            T loadedAsset = null;
            if (type.equals(SoundClip.class))
            {
                loadedAsset = type.cast(new SoundLoader().loadSync(AssetManager.getAssetPointer(assetName), null));
            } else if (type.equals(Shader.class))
            {
                loadedAsset = type.cast(new ShaderLoader().loadSync(AssetManager.getAssetPointer(assetName), null));
            } else if (type.equals(RgbaImage.class))
            {
                loadedAsset = type.cast(new TextureLoader().loadSync(AssetManager.getAssetPointer(assetName), null));
            } else if (type.equals(Font.class))
            {
                loadedAsset = type.cast(new FontLoader().loadSync(AssetManager.getAssetPointer(assetName), null));
            }
            AssetManager.ASSETS_BY_TYPE.get(type.getName()).put(assetName, new ObservedAsset<T>(loadedAsset,
                    () -> AssetManager.ASSETS_BY_TYPE.get(type.getName()).remove(assetName)));
            return loadedAsset;
        }
    }

    public static AssetPointer getAssetPointer(String name)
    {
        return AssetManager.assetTable.pointer.get(name);
    }

    /**
     * Removes the asset and all its dependencies, if they are not used by other
     * assets.
     * 
     * @param fileName
     *            the file name
     */
    public static void unload(final String fileName)
    {
        //
    }

    public static <T extends Asset> void loadAsync(String string, Class<T> class1, FinishCallback<T> callback)
    {
        //
    }
}
