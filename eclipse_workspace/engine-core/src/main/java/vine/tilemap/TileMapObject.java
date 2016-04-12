package vine.tilemap;

import vine.game.GameObject;

public class TileMapObject extends GameObject {
    private UniformTileMap map;

    public void construct(UniformTileMap map) {
        this.map = map;
    }

    public UniformTileMap getMap() {
        return this.map;
    }
}
