package vine.game.scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vine.application.GameLifecycle;
import vine.game.Camera;
import vine.game.Game;
import vine.game.screen.Screen;
import vine.gameplay.component.StaticSprite;
import vine.gameplay.entity.PlayerPawn;
import vine.graphics.GraphicsProvider;
import vine.graphics.SceneRenderer;
import vine.tilemap.TileMap;

/**
 * @author Steffen
 *
 */
public class Scene {
    private Chunk[][] chunks;
    /**
     * 
     */
    public final CameraManager cameras = new CameraManager();

    /**
     * 
     */
    private final Set<GameEntity> entities = new HashSet<>();

    private final Set<GameEntity> visibleSet = new HashSet<>();

    private static final int HALF_CHUNK_WIDTH = 700;
    private static final int HALF_CHUNK_HEIGHT = 400;

    /**
     * @return All entities that are rendered by this layer
     */
    public Set<GameEntity> getEntities() {
        return entities;
    }

    public Chunk getChunk(int i, int j) {
        return chunks[i][j];
    }

    private long time;

    public void update(final float delta) {
        for (final GameEntity entity : entities) {
            entity.updatePhysics(delta * 0.001f);
        }
    }

    /**
     * @return
     */
    public Set<GameEntity> getVisibleEntities() {
        if (System.currentTimeMillis() - time > 43) {
            int x = (int) cameras.getActiveCamera().getEntity().getX() / 1400;
            int y = (int) cameras.getActiveCamera().getEntity().getY() / 800;
            x = 10 <= x ? 10 - 1 : x;
            y = 10 <= y ? 10 - 1 : y;
            x = x < 0 ? 0 : x;
            y = y < 0 ? 0 : y;
            int xMod = (int) cameras.getActiveCamera().getEntity().getX() % 1400;
            int yMod = (int) cameras.getActiveCamera().getEntity().getY() % 800;
            visibleSet.clear();
            visibleSet.addAll(chunks[x][y].entities.values());
            if (HALF_CHUNK_WIDTH > xMod && x > 0) {
                visibleSet.addAll(chunks[x - 1][y].entities.values());
            } else if (x < 9) {
                visibleSet.addAll(chunks[x + 1][y].entities.values());
            }
            if (HALF_CHUNK_HEIGHT > yMod && y > 0) {
                visibleSet.addAll(chunks[x][y - 1].entities.values());
            } else if (y < 9) {
                visibleSet.addAll(chunks[x][y + 1].entities.values());
            }
            if (xMod <= HALF_CHUNK_WIDTH && yMod <= HALF_CHUNK_HEIGHT && x > 0 && y > 0) {
                visibleSet.addAll(chunks[x - 1][y - 1].entities.values());
            } else if (xMod <= HALF_CHUNK_WIDTH && yMod > HALF_CHUNK_HEIGHT && x > 0 && y < 9) {
                visibleSet.addAll(chunks[x - 1][y + 1].entities.values());
            } else if (xMod > HALF_CHUNK_WIDTH && yMod > HALF_CHUNK_HEIGHT && x < 9 && y < 9) {
                visibleSet.addAll(chunks[x + 1][y + 1].entities.values());
            } else if (x < 9 && y > 0) {
                visibleSet.addAll(chunks[x + 1][y - 1].entities.values());
            }
            time = System.currentTimeMillis();
        }
        return visibleSet;
    }

    /**
     * Adds the given entity to this layer.
     * 
     * @param entity
     *            Entity that will be rendered with this layer.
     */
    public final void add(final GameEntity entity) {
        if (!entities.contains(entity)) {
            entities.add(entity);
        }
        entity.addToScene(this);
    }

    /**
     * @param level
     *            The asset name of the level
     * @return A newly created Scene.
     * @throws SceneCreationException
     */
    public void loadScene(final String level, SceneRenderer renderer, Screen screen) {
        chunks = new Chunk[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                chunks[i][j] = new Chunk();
            }
        }
        TileMap map = Game.instantiate(TileMap.class, Integer.valueOf(2000), Integer.valueOf(1500));
        add(map);
        renderer.submit(map, GraphicsProvider.getGraphics(), screen);
        for (int i = 0; i < 1000; i++) {
            final GameEntity entity = Game.instantiate(GameEntity.class, Integer.valueOf((int) (Math.random() * 1000)),
                    Integer.valueOf((int) (Math.random() * 1000)));
            final StaticSprite sprite = Game.instantiate(StaticSprite.class, SceneRenderer.DEFAULT_TEXTURE,
                    Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(16), Integer.valueOf(32));
            entity.addComponent(sprite);
            add(entity);
        }
        final PlayerPawn entity = Game.instantiate(PlayerPawn.class, Integer.valueOf(1), Integer.valueOf(2));
        final StaticSprite sprite = Game.instantiate(StaticSprite.class, SceneRenderer.DEFAULT_TEXTURE,
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(16), Integer.valueOf(32));
        entity.addComponent(sprite);
        final Camera camera = cameras.instantiateCamera();
        entity.addComponent(camera);
        cameras.activate(camera);
        add(entity);
    }

    /**
     * @author Steffen
     *
     */
    public class CameraManager {
        private final List<Camera> managedCameras = new ArrayList<>();
        private Camera activeCamera;

        /**
         * 
         */
        protected CameraManager() {
            // Default Constructor visible, so only Scene can instantiate a
            // camera manager.
        }

        /**
         * @return The camera thats viewport is currently used to render.
         */
        public Camera getActiveCamera() {
            return activeCamera;
        }

        /**
         * @param camera
         *            Camera, that should be managed
         */
        public void removeCamera(final Camera camera) {
            managedCameras.remove(camera);
        }

        /**
         * Use this method to create new cameras.
         * 
         * @return A camera that is usable in this Scene
         */
        public Camera instantiateCamera() {
            final Camera camera = Game.instantiate(Camera.class);
            managedCameras.add(camera);
            return camera;
        }

        /**
         * @param camera
         *            The camera to activate
         */
        public void activate(final Camera camera) {
            if (managedCameras.contains(camera)) {
                activeCamera = camera;
            }
        }
    }

}
