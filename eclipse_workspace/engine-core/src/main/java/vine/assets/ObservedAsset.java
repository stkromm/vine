package vine.assets;

class ObservedAsset<A> {

    @FunctionalInterface
    interface AssetRemover {
        void remove();
    }

    private int refCount;
    private final A asset;
    private final AssetRemover remover;

    ObservedAsset(final A asset, final AssetRemover remover) {
        this.asset = asset;
        this.remover = remover;
    }

    private final void addReference() {
        refCount++;
    }

    private final void removeReference() {
        refCount--;
        if (refCount == 0) {
            remover.remove();
        }
    }

    public void returnAsset() {
        removeReference();
    }

    public A getAsset() {
        addReference();
        return asset;
    }
}