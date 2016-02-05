package vine.graphics;

public interface Sprite {
    public static final int NUM_OF_VERTICES = 12;
    public static final int NUM_OF_UVS = 8;
    public static final int NUM_OF_INDICES = 6;

    float[] getVertices();

    float[] getUVCoordinates();

    Texture2D getTexture();

    int[] getIndices();
}
