package vine.game.tilemap;

import java.util.Map;

import vine.game.GameObject;
import vine.gameplay.component.Sprite;
import vine.graphics.Texture;
import vine.graphics.VertexArray;

public class TileMap extends GameObject {

    private int xTiles;
    private int yTiles;

    private float[] vertices;
    private Sprite[] tiles;

    private TileChunk[] chunks;
    private boolean changed = true;

    public Map<Sprite, VertexArray> getRenderData() {
        // Reduce to visible tiles
        if (changed == true) {
            // recalculate
        }
        return null;
    }

    public void setTile(int x, int y, Sprite sprite) {
        if (sprite == null || x >= xTiles || y >= yTiles) {
            return;
        } else {
            tiles[x * xTiles + y] = sprite;
        }
        changed = true;
    }

    public void shiftY(int amount, boolean repeat) {

    }

    public void shiftX(int amount, boolean repeat) {

    }

    public void setX(int numOfTiles) {

    }

    public void setY(int numOfTiles) {
    }

    private class TileChunk {
        private int x;
        private int y;
        private int width;
        private int height;
        private int gridWidth;
        private int gridHeight;
        private Map<Texture, VertexArray> cachedRenderData;
        private boolean changed = true;

        protected void setDirty() {
            this.changed = true;
        }

        protected boolean isVisibleFromViewpoint(int x, int y) {
            return false;
        }

        private Map<Texture, VertexArray> calculateRenderData() {
            return null;
        }

        protected Map<Texture, VertexArray> getRenderData() {
            if (changed) {
                cachedRenderData = calculateRenderData();
            }
            return cachedRenderData;
        }
    }
}
