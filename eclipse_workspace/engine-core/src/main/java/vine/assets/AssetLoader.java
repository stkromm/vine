package vine.assets;

public abstract class AssetLoader<A, P extends AssetLoaderParameters<A>> {
    /**
     * Loads the non-OpenGL part of the asset and injects any dependencies of
     * the asset into the AssetManager.
     * 
     * @param manager
     * @param fileName
     *            the name of the asset to load
     * @param file
     *            the resolved file to load
     * @param parameter
     *            the parameters to use for loading the asset
     */
    public abstract void loadAsync(AssetManager manager, String fileName, FileHandle file, P parameter);

    /**
     * Loads the OpenGL part of the asset.
     * 
     * @param manager
     * @param fileName
     * @param file
     *            the resolved file to load
     * @param parameter
     */
    public abstract A loadSync(AssetManager manager, String fileName, FileHandle file, P parameter);
}