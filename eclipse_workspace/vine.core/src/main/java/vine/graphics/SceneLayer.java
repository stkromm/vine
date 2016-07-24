package vine.graphics;

import vine.math.Intersection;

import java.util.ArrayList;
import java.util.List;

import vine.game.Layer;
import vine.game.World;
import vine.game.primitive.BoxPrimitive;
import vine.game.scene.GameEntity;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.graphics.renderer.PrimitiveRenderer;
import vine.graphics.renderer.SpriteBatch;
import vine.graphics.renderer.TileMapRenderer;

public class SceneLayer implements Layer
{
    private final Scene           scene;
    private final SpriteBatch     spriteBatch;
    private final TileMapRenderer terrainRenderer;

    public SceneLayer(final World game)
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

    float[]            textureUVs = PrimitiveRenderer.DEFAULT_TEXTURE.getUvQuad(24, 24, 1, 1);
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

            if (Intersection.intersectAabbAabb(
                    scene.getWorld().getScreen().getWidth(),
                    scene.getWorld().getScreen().getHeight() + 100,
                    entity.getPosition().getX() - posX,
                    entity.getPosition().getY() - posY,
                    entity.getBoundingBoxExtends().getX(),
                    entity.getBoundingBoxExtends().getY(),
                    null))
            {
                for (final Renderable renderable : entity.getRenderables())
                {
                    renderable.onRender(spriteBatch);
                }
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
                            entity.getZPosition(),
                            entity.getColor().getColor());
                }
            }
        }
        spriteBatch.finish();
    }
}
