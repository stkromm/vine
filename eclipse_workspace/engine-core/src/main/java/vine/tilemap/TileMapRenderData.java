package vine.tilemap;

import java.util.Arrays;

import vine.game.screen.Screen;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;

/**
 * @author Steffen
 *
 */
public class TileMapRenderData {

    private final VertexBufferObject vertexBuffer;
    private final VertexAttributeBuffer verts;
    private final VertexAttributeBuffer tcs;
    private final TileMap tileMap;

    private int screenxTiles = 42;
    private int screenyTiles = 27;

    private final float[] vertices;
    private final float[] uvs;
    private final float[] colors;
    private int cachedCameraX;
    private int cachedCameraY;

    private final GridCalculator verticeGridCalc = new GridCalculator();

    /**
     * @param map
     *            The map that is used to render
     * @param screen
     *            The screen that is used to render
     */
    public TileMapRenderData(final TileMap map, final Screen screen) {
        this.tileMap = map;
        this.screenxTiles = screen.getWidth() / 32 + 2;
        this.screenyTiles = screen.getHeight() / 32 + 2;
        //
        final int[] indices = new int[6 * this.screenxTiles * this.screenyTiles];
        this.vertices = new float[12 * this.screenxTiles * this.screenyTiles];
        this.uvs = new float[8 * this.screenxTiles * this.screenyTiles];
        this.colors = new float[16 * this.screenxTiles * this.screenyTiles];
        for (int i = this.screenxTiles - 1; i >= 0; i--) {
            for (int j = this.screenyTiles - 1; j >= 0; j--) {
                final int index = i + j * this.screenxTiles;
                System.arraycopy(
                        new int[] { index * 4, index * 4 + 1, index * 4 + 2, index * 4 + 2, index * 4 + 3, index * 4 },
                        0, indices, index * 6, 6);
            }
        }
        this.verts = new VertexAttributeBuffer(vertices, VertexAttribute.POSITION);
        this.tcs = new VertexAttributeBuffer(uvs, VertexAttribute.TEXTURE);

        this.vertexBuffer = new VertexBufferObject(indices, verts, tcs,
                new VertexAttributeBuffer(colors, VertexAttribute.COLOR));
    }

    /**
     * @param positionX
     *            The current x position of the rendered viewpoint
     * @param positionY
     *            The current y position of the rendered viewpoint
     * @return The vbo of the tilemap
     */
    public final VertexBufferObject getRenderData(final int positionX, final int positionY) {
        final int cameraX = positionX / 32 - this.screenxTiles / 2 + 1;
        final int cameraY = positionY / 32 - this.screenyTiles / 2 + 1;
        if (cameraY != this.cachedCameraY || cameraX != this.cachedCameraX) {
            this.verticeGridCalc.calculateVertexGrid(cameraX, cameraY, this.screenxTiles, this.screenyTiles,
                    this.vertices);
            this.verts.append(this.vertices);
            this.verts.reallocate();
        }
        this.cachedCameraX = cameraX;
        this.cachedCameraY = cameraY;

        Arrays.fill(this.uvs, 0);
        for (int i = this.screenxTiles - 1; i >= 0; i--) {
            for (int j = this.screenyTiles - 1; j >= 0; j--) {
                final int index = i + j * this.screenxTiles;
                final int tile = (i + cameraX) + (j + cameraY) * this.tileMap.xTiles;
                if (tile > 0 && tile < this.tileMap.yTiles * this.tileMap.xTiles) {
                    System.arraycopy(this.tileMap.tiles[tile].getUVCoordinates(), 0, this.uvs, index * 8, 8);
                }
            }
        }
        this.tcs.append(uvs);
        this.tcs.reallocate();
        return this.vertexBuffer;
    }
}
