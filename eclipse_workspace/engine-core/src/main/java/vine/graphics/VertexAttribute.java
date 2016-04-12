package vine.graphics;

public enum VertexAttribute {
    POSITION(0, 3, 0), COLOR(2, 4, 1), TEXTURE(1, 2, 0), BITANGENT(5, 3, 0), TANGENT(4, 3, 0), LIGHT_POWER(3, 1, 0);

    private VertexAttribute(int id, int dimension, int type) {
        this.id = id;
        this.dimension = dimension;
        this.type = type;
    }

    private int id;
    private int dimension;
    private int type;

    public int getId() {
        return this.id;
    }

    public int getDimension() {
        return this.dimension;
    }

    public int getType() {
        return this.type;
    }
}
