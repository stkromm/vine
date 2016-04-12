package vine.assets;

public class AssetPointer {
    String path;
    int offset;
    int length;

    public AssetPointer(String path, int offset, int length) {
        super();
        this.path = path;
        this.offset = offset;
        this.length = length;
    }

}
