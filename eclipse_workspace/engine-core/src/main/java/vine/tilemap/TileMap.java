package vine.tilemap;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.game.Game;
import vine.game.scene.GameEntity;
import vine.gameplay.component.AnimatedSprite;
import vine.gameplay.component.StaticSprite;
import vine.graphics.SceneRenderer;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;

/**
 * @author Steffen
 *
 */
public class TileMap extends GameEntity {

    protected int xTiles;
    protected int yTiles;
    protected Sprite[] tiles;

    /**
     * @param width
     * @param height
     */
    public void construct(final int width, final int height) {
        this.xTiles = width;
        this.yTiles = height;
        final float[] uv1 = SceneRenderer.DEFAULT_CHIPSET.getUVSquad(16, 16, 16, 16);
        final float[] uv2 = SceneRenderer.DEFAULT_CHIPSET.getUVSquad(96, 32, 16, 16);
        final Sprite sprite2 = Game.instantiate(AnimatedSprite.class,
                new AnimationState(new AnimationClip(SceneRenderer.DEFAULT_CHIPSET, new AnimationFrame(uv1, 500),
                        new AnimationFrame(uv2, 1000)), "idle", 2));
        final Sprite sprite3 = Game.instantiate(StaticSprite.class, SceneRenderer.DEFAULT_CHIPSET, 16, 0, 16, 16);
        tiles = new Sprite[xTiles * yTiles];
        for (int i = 0; i < xTiles * yTiles; i++) {
            if (i % xTiles > 20 && i % xTiles < 50) {
                tiles[i] = sprite3;
            } else {
                tiles[i] = sprite2;
            }
        }
    }

    public final Texture2D getTexture() {
        return SceneRenderer.DEFAULT_CHIPSET;
    }

    public Sprite getTile(final int i, final int j) {
        return tiles[i + j * xTiles];
    }

    public int getWidth() {
        return xTiles;
    }

    public int getHeight() {
        return yTiles;
    }
}
