package vine.game.tilemap;

import vine.assets.AssetManager;
import vine.graphics.Image;
import vine.graphics.Texture;

public final class TileMapParser
{
    public static final int INDICE_FIELD  = 4;
    public static final int WIDTH_FIELD   = 1;
    public static final int CHIPSET_FIELD = 2;
    public static final int TILE_FIELD    = 3;

    private TileMapParser()
    {

    }

    public static UniformTileMap parseTileMap(final String file)
    {
        final String[] parts = file.split("#");
        final int width = Integer.valueOf(parts[TileMapParser.WIDTH_FIELD]).intValue();
        final Texture chipset = AssetManager.loadSync(parts[TileMapParser.CHIPSET_FIELD], Image.class);
        final Tile[] tiles = TileMapParser.parseTiles(parts[TileMapParser.TILE_FIELD]);
        final UniformTileMap tileMap = new UniformTileMap(width, tiles, chipset);
        return tileMap;
    }

    public static Tile[] parseTiles(String tiles)
    {
        return null;
    }
}
