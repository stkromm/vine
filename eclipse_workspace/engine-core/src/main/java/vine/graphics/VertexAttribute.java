package vine.graphics;

public enum VertexAttribute {
    POSITION(0, 3), COLOR(2, 4), TEXTURE(1, 2), BITANGENT(5, 3), TANGENT(4, 3), LIGHT_POWER(3, 1);

    private VertexAttribute(int id, int dimension) {
        this.id = id;
        this.dimension = dimension;
    }

    private int id;
    private int dimension;

    public int getId() {
        return id;
    }

    public int getDimension() {
        return dimension;
    }
}
