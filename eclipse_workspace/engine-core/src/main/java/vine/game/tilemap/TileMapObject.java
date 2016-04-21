package vine.game.tilemap;

import vine.game.scene.GameEntity;

public class TileMapObject extends GameEntity
{
    private UniformTileMap map;

    public void construct(final UniformTileMap map)
    {
        this.map = map;
    }

    public UniformTileMap getMap()
    {
        return this.map;
    }
}
