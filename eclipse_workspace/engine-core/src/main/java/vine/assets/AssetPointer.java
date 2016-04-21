package vine.assets;

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
        return this.path;
    }

    public int getOffset()
    {
        return this.offset;
    }

    public int getLength()
    {
        return this.length;
    }

}
