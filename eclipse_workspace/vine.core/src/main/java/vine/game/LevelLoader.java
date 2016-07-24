package vine.game;

import vine.math.GMath;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;
import vine.game.primitive.BoxPrimitive;
import vine.game.scene.GameEntity;
import vine.game.tilemap.Tile;
import vine.game.tilemap.TileMapSceneProxy;
import vine.game.tilemap.UniformTileMap;
import vine.gameplay.AnimatedSprite;
import vine.gameplay.PlayerPawn;
import vine.gameplay.StaticSprite;
import vine.graphics.Color;
import vine.graphics.RgbaTexture;
import vine.graphics.Sprite;
import vine.io.assets.AssetManager;
import vine.physics.RigidBody;
import vine.util.Log;

public class LevelLoader
{

    public static void loadScene(final String level, final World world)
    {
        Log.lifecycle("Load Scene");
        final RgbaTexture chipset = AssetManager.loadSync("chipset", RgbaTexture.class);
        float[] uv1 = chipset.getUvQuad(16, 16, 16, 16);
        float[] uv2 = chipset.getUvQuad(96, 32, 16, 16);
        final AnimationStateManager animation = new AnimationStateManager(new AnimationState[] { new AnimationState(
                new AnimationClip(new AnimationFrame(uv1, 500), new AnimationFrame(uv2, 1000)), "idle", 2) });
        final AnimatedSprite water = new AnimatedSprite(animation, chipset, 32, 32);
        final Sprite grass = new StaticSprite(chipset, 0, 0, 16, 16, 32, 32);
        final Sprite earth = new StaticSprite(chipset, 32, 48, 16, 16, 32, 32);
        final Tile[] indices = new Tile[200 * 200];
        for (int i = 0; i < indices.length; i++)
        {
            if (GMath.randomFloat(1) > 0.3)
            {
                indices[i] = new Tile(grass, new Color(0, 0, 0, 0), false, 0.5f);
            } else if (GMath.randomFloat(1) > 0.2)
            {
                indices[i] = new Tile(earth, new Color(0, 0, 0, 0), false, 0.5f);
            } else
            {
                indices[i] = new Tile(water, new Color(0, 0, 0, 0), false, 0.5f);
            }
        }
        final TileMapSceneProxy to = world
                .instantiate(TileMapSceneProxy.class, new UniformTileMap(200, indices, chipset)).get();
        world.getScene().addEntity(to);
        world.getScene().addMap(to);
        world.getPhysics().addPhysicBody(to);
        to.attachComponent(water);
        final PlayerPawn entity = world.instantiate(PlayerPawn.class, "player").get();
        final Camera camera = world.getScene().getCameras().createCamera();
        entity.attachComponent(camera);
        world.getScene().getCameras().activate(camera);
        final RgbaTexture tex = AssetManager.loadSync("herosheet", RgbaTexture.class);
        uv1 = tex.getUvQuad(0, 0, 24, 32);
        uv2 = tex.getUvQuad(24, 0, 24, 32);
        float[] uv3 = tex.getUvQuad(48, 0, 24, 32);
        final AnimationClip moveUpwards = new AnimationClip(new AnimationFrame(uv2, 200), new AnimationFrame(uv1, 400),
                new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        uv1 = tex.getUvQuad(0, 64, 24, 32);
        uv2 = tex.getUvQuad(24, 64, 24, 32);
        uv3 = tex.getUvQuad(48, 64, 24, 32);
        final AnimationClip moveDownwards = new AnimationClip(new AnimationFrame(uv2, 200),
                new AnimationFrame(uv1, 400), new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        uv1 = tex.getUvQuad(0, 32, 24, 32);
        uv2 = tex.getUvQuad(24, 32, 24, 32);
        uv3 = tex.getUvQuad(48, 32, 24, 32);
        final AnimationClip moveRight = new AnimationClip(new AnimationFrame(uv2, 200), new AnimationFrame(uv1, 400),
                new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        uv1 = tex.getUvQuad(0, 96, 24, 32);
        uv2 = tex.getUvQuad(24, 96, 24, 32);
        uv3 = tex.getUvQuad(48, 96, 24, 32);
        final AnimationClip moveLeft = new AnimationClip(new AnimationFrame(uv2, 200), new AnimationFrame(uv1, 400),
                new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        final AnimationClip idleDown = new AnimationClip(new AnimationFrame(tex.getUvQuad(24, 64, 24, 32), 100f));
        final AnimationClip idleUp = new AnimationClip(new AnimationFrame(tex.getUvQuad(24, 0, 24, 32), 100f));
        final AnimationClip idleLeft = new AnimationClip(new AnimationFrame(tex.getUvQuad(24, 96, 24, 32), 100f));
        final AnimationClip idleRight = new AnimationClip(new AnimationFrame(tex.getUvQuad(24, 32, 24, 32), 100f));
        final AnimationStateManager animation1 = new AnimationStateManager(new AnimationState[] {
                new AnimationState(idleUp, "idle-up", 1), new AnimationState(idleRight, "idle-right", 1),
                new AnimationState(idleDown, "idle-down", 1), new AnimationState(idleLeft, "idle-left", 1),
                new AnimationState(moveUpwards, "up", 1), new AnimationState(moveDownwards, "down", 1),
                new AnimationState(moveRight, "right", 1), new AnimationState(moveLeft, "left", 1) });
        animation1.changeState("idle-down");
        world.getScene().addEntity(entity);
        final AnimatedSprite sprite = new AnimatedSprite(animation1, tex, 48, 64);
        entity.attachComponent(sprite);
        final BoxPrimitive bp = new BoxPrimitive();
        entity.attachComponent(bp);
        // TODO Collision is offset too
        // bp.getTransform().translate(12, 0);
        final RigidBody rb = new RigidBody();
        rb.addPrimitive(bp);
        rb.setInvMass(0);
        rb.ignoreMass();
        rb.setDamping(1);
        entity.attachComponent(rb);
        for (int i = 0; i < 200000; i++)
        {
            final GameEntity entity1 = world.getScene()
                    .spawn(GameEntity.class, GMath.randomInteger(400), GMath.randomInteger(400), false).get();
            final StaticSprite sprite1 = new StaticSprite(AssetManager.loadSync("hero", RgbaTexture.class), 0, 0, 16,
                    32, 32, 64);
            entity1.attachComponent(sprite1);
            /*
             * bp = new BoxPrimitive(); entity1.attachComponent(bp); rb = new
             * RigidBody(); rb.addPrimitive(bp); rb.setInvMass(0);
             * rb.ignoreMass(); entity1.attachComponent(rb);
             */
            // entity1.attachComponent(new EnemyAI());
        }

        Log.debug("Finished level loading");
        Log.debug("GameObject count:" + GameObject.ReferenceManager.OBJECTS.size());
    }
}
