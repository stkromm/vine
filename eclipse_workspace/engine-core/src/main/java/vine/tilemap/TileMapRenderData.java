package vine.tilemap;

import vine.game.Game;
import vine.graphics.VertexBufferObject;

public class TileMapRenderData {

    VertexBufferObject vertexBuffer;
    TileMap tileMap;

    int screenxTiles = 42;
    int screenyTiles = 27;

    float[] vertices;
    float[] uvs;

    int lastX;
    int lastY;

    public TileMapRenderData(TileMap map) {
        tileMap = map;
        screenxTiles = Game.getGame().getScreen().getWidth() / 32 + 2;
        screenyTiles = Game.getGame().getScreen().getHeight() / 32 + 2;
        //
        int[] indices = new int[6 * screenxTiles * screenyTiles];
        this.vertices = new float[12 * screenxTiles * screenyTiles];
        this.uvs = new float[8 * screenxTiles * screenyTiles];
        int startX = 0;
        int startY = 0;
        for (int i = startX; i < startX + screenxTiles; i++) {
            for (int j = startY; j < startY + screenyTiles; j++) {
                int spritesIndex = i + j * tileMap.getWidth() < 0 ? 0 : i + j * tileMap.getWidth();
                int index = (i - startX) + (j - startY) * screenxTiles;
                System.arraycopy(
                        new int[] { index * 4, index * 4 + 1, index * 4 + 2, index * 4 + 2, index * 4 + 3, index * 4 },
                        0, indices, index * 6, 6);
            }
        }
        vertexBuffer = new VertexBufferObject(vertices, indices, uvs, Game.getGame().getGraphics());
    }

    /**
     * @return
     */
    public final VertexBufferObject getRenderData() {
        int startX = (int) Math.floor(Game.getGame().getScene().cameras.getActiveCamera().getEntity().getX() / 32)
                - screenxTiles / 2 + 1;
        int startY = (int) Math.floor(Game.getGame().getScene().cameras.getActiveCamera().getEntity().getY() / 32)
                - screenyTiles / 2 + 1;
        if (startX != lastX || startY != lastY) {
            for (int i = startX; i < startX + screenxTiles; i++) {
                for (int j = startY; j < startY + screenyTiles; j++) {
                    int index = (i - startX) + (j - startY) * screenxTiles;
                    System.arraycopy(new float[] { //
                            i * 32, j * 32, 0, //
                            i * 32, j * 32 + 32, 0, //
                            i * 32 + 32, j * 32 + 32, 0, //
                            i * 32 + 32, j * 32, 0, }, 0, vertices, index * 12, 12);
                }
            }
            vertexBuffer.changeVertices(vertices);
        }
        lastX = startX;
        lastY = startY;
        for (int i = startX; i < startX + screenxTiles; i++) {
            for (int j = startY; j < startY + screenyTiles; j++) {
                int index = (i - startX) + (j - startY) * screenxTiles;
                if (i + j * tileMap.getWidth() > 0) {
                    System.arraycopy(tileMap.getTile(i, j).getUVCoordinates(), 0, uvs, index * 8, 8);
                } else {
                    for (int a = 0; a < 8; a++) {
                        uvs[index * 8 + a] = 0;
                    }
                }
            }
        }
        vertexBuffer.changeTexture(uvs);

        return vertexBuffer;
    }
}
