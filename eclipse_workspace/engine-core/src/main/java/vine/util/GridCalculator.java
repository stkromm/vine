package vine.util;

public final class GridCalculator
{
    public static final int SQUAD_VERTICES_FLOATS = 12;

    private GridCalculator()
    {

    }

    public static float[] calculateVertexGrid(
            final int tileWidth,
            final int tileHeight,
            final int width,
            final int height)
    {
        final float[] vertices = new float[width * height * GridCalculator.SQUAD_VERTICES_FLOATS];
        final float[] squadVertices = new float[GridCalculator.SQUAD_VERTICES_FLOATS];
        for (int i = width - 1; i >= 0; i--)
        {
            for (int j = height - 1; j >= 0; j--)
            {
                final int index = i + j * width;
                squadVertices[0] = i * tileWidth;
                squadVertices[1] = j * tileHeight;
                squadVertices[3] = i * tileWidth;
                squadVertices[4] = j * tileHeight + tileHeight;
                squadVertices[6] = i * tileWidth + tileWidth;
                squadVertices[7] = j * tileHeight + tileHeight;
                squadVertices[9] = i * tileWidth + tileWidth;
                squadVertices[10] = j * tileHeight;
                System.arraycopy(squadVertices, 0, vertices, index * GridCalculator.SQUAD_VERTICES_FLOATS,
                        GridCalculator.SQUAD_VERTICES_FLOATS);
            }
        }
        return vertices;
    }

}
