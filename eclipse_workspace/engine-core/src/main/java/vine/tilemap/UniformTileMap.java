package vine.tilemap;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;
import vine.assets.AssetManager;
import vine.game.World;
import vine.gameplay.component.AnimatedSprite;
import vine.gameplay.component.StaticSprite;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;

/**
 * @author Steffen
 *
 */
public class UniformTileMap {
    int tileWidth;
    int tileHeight;
    int xTiles;
    int yTiles;
    Sprite[] tiles;
    public static final Texture2D DEFAULT_CHIPSET = AssetManager.loadSync("chipset", Texture2D.class);

    /**
     * @param width
     *            Number of tiles in the width
     * @param height
     *            Number of tiles in the height
     */
    public UniformTileMap(World world, final int width, final int height) {
        this.xTiles = width;
        this.yTiles = height;
        this.tileWidth = 32;
        this.tileHeight = 32;
        final float[] uv1 = UniformTileMap.DEFAULT_CHIPSET.getUVSquad(16, 16, 16, 16);
        final float[] uv2 = UniformTileMap.DEFAULT_CHIPSET.getUVSquad(96, 32, 16, 16);
        final AnimationStateManager animation = new AnimationStateManager(new AnimationState[] { new AnimationState(
                new AnimationClip(new AnimationFrame(uv1, 500), new AnimationFrame(uv2, 1000)), "idle", 2) });
        final Sprite sprite2 = world.instantiate(AnimatedSprite.class, animation, UniformTileMap.DEFAULT_CHIPSET,
                Integer.valueOf(32), Integer.valueOf(32));
        final Sprite sprite3 = world.instantiate(StaticSprite.class, UniformTileMap.DEFAULT_CHIPSET,
                Integer.valueOf(16), Integer.valueOf(0), Integer.valueOf(16), Integer.valueOf(16), Integer.valueOf(32),
                Integer.valueOf(32));
        this.tiles = new Sprite[this.xTiles * this.yTiles];
        for (int i = 0; i < this.tiles.length; i++) {
            if (Math.random() > 0.1) {
                this.tiles[i] = sprite3;
            } else {
                this.tiles[i] = sprite2;
            }
        }
    }

    /**
     * @return The used chipset
     */
    public final Texture2D getTexture() {
        return UniformTileMap.DEFAULT_CHIPSET;
    }

    /**
     * @param i
     *            x Coord in Tiles
     * @param j
     *            y Coord in Tiles
     * @return The corresponding tile
     */
    public Sprite getTile(final int i, final int j) {
        final int index = i + j * this.xTiles;
        return index >= this.tiles.length || index < 0 ? null : this.tiles[index];
    }

    /**
     * @return Number of x tiles
     */
    public int getWidth() {
        return this.xTiles;
    }

    /**
     * @return Number of y tiles
     */
    public int getHeight() {
        return this.yTiles;
    }

    /**
     * @return Pixel width of a tile
     */
    public int getTileWidth() {
        return this.tileWidth;
    }

    /**
     * @return Pixel height of a tile
     */
    public int getTileHeight() {
        return this.tileHeight;
    }

}
