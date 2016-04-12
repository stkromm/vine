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
        this.addReference();
    }

    private final void addReference() {
        this.refCount++;
    }

    private final void removeReference() {
        this.refCount--;
        if (this.refCount == 0) {
            this.remover.remove();
        }
    }

    public void returnAsset() {
        this.removeReference();
    }

    public A getAsset() {
        this.addReference();
        return this.asset;
    }
}
