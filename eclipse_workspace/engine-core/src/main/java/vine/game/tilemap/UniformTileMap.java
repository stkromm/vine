package vine.game.tilemap;

import vine.graphics.Texture;

/**
 * @author Steffen
 *
 */
public class UniformTileMap
{
    private final int     tileWidth;
    private final int     tileHeight;
    private final int     xTiles;
    private final int     yTiles;
    private final Tile[]  tiles;
    private final Texture chipset;

    /**
     * @param width
     *            Number of tiles in the width
     * @param height
     *            Number of tiles in the height
     */
    public UniformTileMap(final int width, final Tile[] tiles, Texture chipset)
    {
        this.chipset = chipset;
        this.xTiles = width;
        this.yTiles = tiles.length / width;
        this.tileWidth = 32;
        this.tileHeight = 32;
        this.tiles = tiles;
    }

    /**
     * @return The used chipset
     */
    public final Texture getTexture()
    {
        return this.chipset;
    }

    /**
     * @param i
     *            x Coord in Tiles
     * @param j
     *            y Coord in Tiles
     * @return The corresponding tile or null
     */
    public Tile getTile(final int i, final int j)
    {
        final int index = i + j * this.xTiles;
        return index >= this.tiles.length || index < 0 ? null : this.tiles[index];
    }

    public boolean blocksDynamic(final int i, final int j)
    {
        final Tile tile = this.getTile(i, j);
        if (tile == null)
        {
            return true;
        }
        return tile.blocksDynamic();
    }

    /**
     * @return Number of x tiles
     */
    public int getWidth()
    {
        return this.xTiles;
    }

    /**
     * @return Number of y tiles
     */
    public int getHeight()
    {
        return this.yTiles;
    }

    /**
     * @return Pixel width of a tile
     */
    public int getTileWidth()
    {
        return this.tileWidth;
    }

    /**
     * @return Pixel height of a tile
     */
    public int getTileHeight()
    {
        return this.tileHeight;
    }
}
