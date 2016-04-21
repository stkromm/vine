package vine.graphics;

import vine.util.TypeSize;

public enum VertexAttribute
{
    POSITION(0, 3, TypeSize.INT32), COLOR(2, 4, TypeSize.BYTE8), TEXTURE(1, 2, TypeSize.INT32), TANGENT(4, 3,
            TypeSize.INT32), LIGHT_POWER(3, 1, TypeSize.INT32), GENERIC_FLOAT(6, 1, TypeSize.INT32);

    private VertexAttribute(final int id, final int dimension, final TypeSize type)
    {
        this.id = id;
        this.dimension = dimension;
        this.type = type;
    }

    private final int      id;
    private final int      dimension;
    private final TypeSize type;

    public int getId()
    {
        return this.id;
    }

    public int getDimension()
    {
        return this.dimension;
    }

    public TypeSize getType()
    {
        return this.type;
    }
}
