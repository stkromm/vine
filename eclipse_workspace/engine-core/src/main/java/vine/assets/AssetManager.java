package vine.assets;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private final Map<Class<?>, Map<String, ObservedAsset<?>>> assetsByType = new HashMap<>();

    /**
     * @param fileName
     *            the asset file name
     * @param type
     *            the asset type
     * @return the asset
     */
    public synchronized <T> T get(final String fileName, final Class<T> type) {
        final ObservedAsset<?> asset = assetsByType.get(type).get(fileName);
        if (asset != null && asset.getClass().equals(type)) {
            return (T) asset.getAsset();
        } else {
            final AssetLoader<T, AssetLoaderParameters<T>> loader = getLoader(type);
            return loader.loadSync(this, fileName, null, null, null);
        }
    }

    private <T> AssetLoader<T, AssetLoaderParameters<T>> getLoader(final Class<T> type) {
        return null;
    }

    /**
     * Removes the asset and all its dependencies, if they are not used by other
     * assets.
     * 
     * @param fileName
     *            the file name
     */
    public synchronized void unload(final String fileName) {

    }
}
