package vine.tilemap;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.game.Game;
import vine.gameplay.component.AnimatedSprite;
import vine.gameplay.component.StaticSprite;
import vine.gameplay.entity.GameEntity;
import vine.graphics.Renderer;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;

public class TileMap extends GameEntity {

    private int xTiles;
    private int yTiles;

    private Sprite[] tiles;

    /**
     * @param width
     * @param height
     */
    public void construct(final int width, final int height) {
        this.xTiles = width;
        this.yTiles = height;
        float[] uv1 = Renderer.DEFAULT_CHIPSET.getUVSquad(16, 16, 16, 16);
        float[] uv2 = Renderer.DEFAULT_CHIPSET.getUVSquad(96, 32, 16, 16);
        Sprite sprite2 = Game.instantiate(AnimatedSprite.class, Integer.valueOf(16), Integer.valueOf(16),
                new AnimationState(new AnimationClip(Renderer.DEFAULT_CHIPSET, new AnimationFrame(uv1, 500),
                        new AnimationFrame(uv2, 1000)), "idle", 2));
        Sprite sprite3 = Game.instantiate(StaticSprite.class, Integer.valueOf(16), Integer.valueOf(16),
                Renderer.DEFAULT_CHIPSET, 16, 0, 16, 16);
        tiles = new Sprite[xTiles * yTiles];
        for (int i = 0; i < xTiles * yTiles; i++) {
            if (i % xTiles > 20 && i % xTiles < 50) {
                tiles[i] = sprite3;
            } else {
                tiles[i] = sprite2;
            }
        }

        Game.getGame().getScene().setMap(this);
    }

    public final Texture2D getTexture() {
        return Renderer.DEFAULT_CHIPSET;
    }

    public Sprite getTile(int i, int j) {
        return tiles[i + j * xTiles];
    }

    public int getWidth() {
        return xTiles;
    }

    public int getHeight() {
        return yTiles;
    }
}
