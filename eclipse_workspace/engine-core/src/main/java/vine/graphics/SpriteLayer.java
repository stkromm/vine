package vine.graphics;

import vine.event.EventListener;
import vine.game.Layer;
import vine.game.World;
import vine.game.scene.GameEntity;
import vine.game.scene.Scene;
import vine.game.scene.SceneTracer;
import vine.game.screen.Screen;
import vine.graphics.renderer.SpriteBatch;
import vine.graphics.renderer.TileMapRenderer;

public class SpriteLayer implements Layer
{
    private final Scene           scene;
    private final SpriteBatch     spriteBatch;
    private final TileMapRenderer terrainRenderer;

    public SpriteLayer(final World game)
    {
        this.scene = game.getScene();
        this.spriteBatch = new SpriteBatch();
        this.terrainRenderer = new TileMapRenderer(game.getScreen(), game);
    }

    @Override
    public String getName()
    {
        return "Scene";
    }

    @Override
    public void render(final Screen screen)
    {
        this.terrainRenderer.submit(this.scene.getMap());
        this.terrainRenderer.prepare(this.scene.getWorld().getScreen());
        final GameEntity cameraEntity = this.scene.getCameras().getActiveCamera().getEntity();
        final float posX = cameraEntity.getXPosition() - this.scene.getWorld().getScreen().getWidth() / 2;
        final float posY = cameraEntity.getYPosition() - this.scene.getWorld().getScreen().getHeight() / 2 - 50;
        this.spriteBatch.prepare(this.scene);
        final int startChunkX = (int) (posX * SceneTracer.I_CHUNK_WIDTH);
        final int startChunkY = (int) (posY * SceneTracer.I_CHUNK_HEIGHT);
        final int endChunkX = (int) ((posX + this.scene.getWorld().getScreen().getWidth()) * SceneTracer.I_CHUNK_WIDTH);
        final int endChunkY = (int) ((posY + this.scene.getWorld().getScreen().getHeight())
                * SceneTracer.I_CHUNK_HEIGHT);
        for (int i = endChunkX - startChunkX; i >= 0; i--)
        {
            for (int j = endChunkY - startChunkY; j >= 0; j--)
            {
                for (final GameEntity entity : this.scene.getChunk(startChunkX + i, startChunkY + j).getEntities())
                {
                    if (entity.isDestroyed() || entity.getColor().getAlpha() > 0.99f)
                    {
                        continue;
                    }
                    /*
                     * if (Intersection2D.doesAabbIntersectAabb( posX, posY,
                     * this.scene.getWorld().getScreen().getWidth(),
                     * this.scene.getWorld().getScreen().getHeight() + 100,
                     * entity.getPosition().getX(), entity.getPosition().getY(),
                     * entity.getBoundingBoxExtends().getX(),
                     * entity.getBoundingBoxExtends().getY())) {
                     */
                    for (final Renderable renderable : entity.getRenderables().getIterable())
                    {
                        renderable.onRender(this.spriteBatch);
                    }
                    // }
                }
            }
        }
        this.spriteBatch.finish();

    }

    @Override
    public EventListener getListener()
    {
        return null;
    }
}
