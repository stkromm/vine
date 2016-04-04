package vine.tilemap;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.assets.TextureLoader;
import vine.game.Game;
import vine.game.GameObject;
import vine.gameplay.component.AnimatedSprite;
import vine.gameplay.component.StaticSprite;
import vine.graphics.SpriteRenderer;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;

/**
 * @author Steffen
 *
 */
public class TileMap extends GameObject {
    int tileWidth;
    int tileHeight;
    int xTiles;
    int yTiles;
    Sprite[] tiles;
    public static final Texture2D DEFAULT_CHIPSET = new TextureLoader().loadSync(null, "res/test/chipset.png", null,
            null, null);

    /**
     * @param width
     *            Number of tiles in the width
     * @param height
     *            Number of tiles in the height
     */
    public void construct(final int width, final int height) {
        this.xTiles = width;
        this.yTiles = height;
        this.tileWidth = 32;
        this.tileHeight = 32;
        final float[] uv1 = DEFAULT_CHIPSET.getUVSquad(16, 16, 16, 16);
        final float[] uv2 = DEFAULT_CHIPSET.getUVSquad(96, 32, 16, 16);
        final Sprite sprite2 = Game
                .instantiate(
                        AnimatedSprite.class, new AnimationState(new AnimationClip(DEFAULT_CHIPSET,
                                new AnimationFrame(uv1, 500), new AnimationFrame(uv2, 1000)), "idle", 2),
                Integer.valueOf(32), Integer.valueOf(32));
        final Sprite sprite3 = Game.instantiate(StaticSprite.class, DEFAULT_CHIPSET, Integer.valueOf(16),
                Integer.valueOf(0), Integer.valueOf(16), Integer.valueOf(16), Integer.valueOf(32), Integer.valueOf(32));
        this.tiles = new Sprite[this.xTiles * this.yTiles];
        for (int i = 0; i < this.tiles.length; i++) {
            if (i % 5 != 1 || i / 1000 % 2 != 0) {
                this.tiles[i] = sprite3;
            } else {
                this.tiles[i] = sprite2;
            }
        }
    }

    /**
     * @return The used chipset
     */
    @SuppressWarnings("static-method")
    public final Texture2D getTexture() {
        return DEFAULT_CHIPSET;
    }

    /**
     * @param i
     *            x Coord in Tiles
     * @param j
     *            y Coord in Tiles
     * @return The corresponding tile
     */
    public Sprite getTile(final int i, final int j) {
        return this.tiles[i + j * this.xTiles];
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
