package vine.graphics;

public class Icon
{
    private Resolution[] resolutions;

    public int getLevels()
    {
        return resolutions.length;
    }

    public Resolution getLevel(final int i)
    {
        return resolutions[i];
    }

    public class Resolution
    {
        final int    size = 32;
        final byte[] data = new byte[32 * 32 * 4];

        public int getSize()
        {
            return size;
        }

        public byte[] getData()
        {
            return data;
        }

    }
}
