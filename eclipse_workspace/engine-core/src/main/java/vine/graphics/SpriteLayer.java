package vine.graphics;

import java.util.ArrayList;
import java.util.List;

import vine.event.EventListener;
import vine.game.Layer;
import vine.game.World;
import vine.game.primitive.BoxPrimitive;
import vine.game.scene.GameEntity;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.graphics.renderer.PrimitiveRenderer;
import vine.graphics.renderer.SpriteBatch;
import vine.graphics.renderer.TileMapRenderer;
import vine.math.Intersection;

public class SpriteLayer implements Layer
{
    private final Scene           scene;
    private final SpriteBatch     spriteBatch;
    private final TileMapRenderer terrainRenderer;

    public SpriteLayer(final World game)
    {
        scene = game.getScene();
        spriteBatch = new SpriteBatch();
        terrainRenderer = new TileMapRenderer(game.getScreen(), game);
    }

    @Override
    public String getName()
    {
        return "Scene";
    }

    float[]            textureUVs = PrimitiveRenderer.DEFAULT_TEXTURE.getPackedUVSquad(24, 24, 1, 1);
    List<BoxPrimitive> bps        = new ArrayList<>();

    @Override
    public void render(final Screen screen)
    {
        terrainRenderer.submit(scene.getMap());
        terrainRenderer.prepare(scene.getWorld().getScreen());
        final GameEntity cameraEntity = scene.getCameras().getActiveCamera().getEntity();
        final float posX = cameraEntity.getXPosition() - scene.getWorld().getScreen().getWidth() / 2;
        final float posY = cameraEntity.getYPosition() - scene.getWorld().getScreen().getHeight() / 2 - 50;
        spriteBatch.prepare(scene);
        for (final GameEntity entity : scene.getEntities())
        {
            if (entity.isDestroyed() || entity.getColor().getAlpha() > 0.99f)
            {
                continue;
            }

            if (Intersection.doesAABBIntersectAABB(
                    scene.getWorld().getScreen().getWidth(),
                    scene.getWorld().getScreen().getHeight() + 100,
                    entity.getPosition().getX() - posX,
                    entity.getPosition().getY() - posY,
                    entity.getBoundingBoxExtends().getX(),
                    entity.getBoundingBoxExtends().getY()))
            {
                for (final Renderable renderable : entity.getRenderables())
                {
                    renderable.onRender(spriteBatch);
                }
            }
        }
        spriteBatch.finish();
        spriteBatch.prepare(scene);
        for (final GameEntity entity : scene.getEntities())
        {
            if (entity.isDestroyed() || entity.getColor().getAlpha() > 0.99f)
            {
                continue;
            }

            if (Intersection.doesAABBIntersectAABB(
                    scene.getWorld().getScreen().getWidth(),
                    scene.getWorld().getScreen().getHeight() + 100,
                    entity.getPosition().getX() - posX,
                    entity.getPosition().getY() - posY,
                    entity.getBoundingBoxExtends().getX(),
                    entity.getBoundingBoxExtends().getY()))
            {
                bps.clear();
                entity.getComponents(BoxPrimitive.class, bps);
                for (final BoxPrimitive c : bps)
                {
                    spriteBatch.submit(
                            PrimitiveRenderer.DEFAULT_TEXTURE,
                            textureUVs,
                            c.getTransform().getWorldPosition().getX(),
                            c.getTransform().getWorldPosition().getY(),
                            c.getExtends().getX(),
                            c.getExtends().getY(),
                            0,
                            entity.getColor().getColor());
                }

            }
        }
        spriteBatch.finish();
    }

    @Override
    public EventListener getListener()
    {
        return null;
    }
}
