package vine.util;

public enum TypeSize
{
    BYTE8(8), SHORT16(16), INT32(32), LONG64(64);

    private final int bitSize;

    TypeSize(final int size)
    {
        this.bitSize = size;
    }

    public int getBitSize()
    {
        return this.bitSize;
    }
}
