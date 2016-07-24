package vine.io.assets;

public class AssetPointer
{
    private final String path;
    private final int    offset;
    private final int    length;

    public AssetPointer(final String path, final int offset, final int length)
    {
        super();
        this.path = path;
        this.offset = offset;
        this.length = length;
    }

    public String getPath()
    {
        return path;
    }

    public int getOffset()
    {
        return offset;
    }

    public int getLength()
    {
        return length;
    }
}
