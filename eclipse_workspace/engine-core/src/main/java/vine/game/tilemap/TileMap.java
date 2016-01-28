package vine.game.tilemap;

import java.util.Map;

import vine.game.GameObject;
import vine.gameplay.component.Sprite;
import vine.graphics.Texture;
import vine.graphics.VertexArray;

public class TileMap extends GameObject {

    private int xTiles;
    private int yTiles;

    // private float[] vertices;
    private Sprite[] tiles;

    // private TileChunk[] chunks;
    private boolean changed = true;

    /**
     * @param width
     * @param height
     */
    public void construct(final int width, final int height) {
        this.xTiles = width;
        this.yTiles = height;
        tiles = new Sprite[width * height];
    }

    /**
     * @return
     */
    public Map<Sprite, VertexArray> getRenderData() {
        // Reduce to visible tiles
        if (changed) {
            // recalculate
        }
        return null;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public Sprite getTile(final int x, final int y) {
        return tiles[x * xTiles + y];
    }

    /**
     * @param x
     * @param y
     * @param sprite
     */
    public void setTile(final int x, final int y, final Sprite sprite) {
        if (sprite == null || x >= xTiles || y >= yTiles) {
            return;
        } else {
            tiles[x * xTiles + y] = sprite;
        }
        changed = true;
    }
    /*
     * private class TileChunk { private int x; private int y; private int
     * width; private int height; private int gridWidth; private int gridHeight;
     * private Map<Texture, VertexArray> cachedRenderData; private boolean
     * changed = true;
     * 
     * public TileChunk(int x, int y, int width, int height, int gridWidth, int
     * gridHeight) { super(); this.x = x; this.y = y; this.width = width;
     * this.height = height; this.gridWidth = gridWidth; this.gridHeight =
     * gridHeight; }
     * 
     * protected void setDirty() { this.changed = true; }
     * 
     * protected boolean isVisibleFromViewpoint(final int x, final int y) {
     * return width > x*gridWidth && x*gridWidth > 0 && y*gridHeight > 0; }
     * 
     * private Map<Texture, VertexArray> calculateRenderData() { return null; }
     * 
     * protected Map<Texture, VertexArray> getRenderData() { if (changed) {
     * cachedRenderData = calculateRenderData(); } return cachedRenderData; } }
     */
}
