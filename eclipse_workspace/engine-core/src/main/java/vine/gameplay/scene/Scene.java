package vine.gameplay.scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vine.game.Game;
import vine.gameplay.component.Camera;
import vine.gameplay.component.StaticSprite;
import vine.gameplay.entity.GameEntity;
import vine.gameplay.entity.PlayerPawn;
import vine.graphics.Graphics;
import vine.graphics.Renderer;
import vine.math.Matrix4f;
import vine.tilemap.TileMap;

/**
 * @author Steffen
 *
 */
public class Scene {

    /**
     * 
     */
    protected final Renderer renderer = new Renderer();
    /**
     * 
     */
    public final CameraManager cameras = new CameraManager();
    private final Graphics graphics = Game.getGame().getGraphics();
    private Matrix4f cameraTranslation = Matrix4f.identity();

    /**
     * 
     */
    protected final Set<GameEntity> entities = new HashSet<>();

    /**
     * @return All entities that are rendered by this layer
     */
    public Set<GameEntity> getEntities() {
        return entities;
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
    }

    public void setMap(TileMap map) {
        renderer.submit(map);
        this.add(map);
    }

    public final void render() {
        renderer.clear();
        renderer.submit(entities);
        cameraTranslation.elements[0 + 3 * 4] = -cameras.getActiveCamera().getEntity().getX();
        cameraTranslation.elements[1 + 3 * 4] = -cameras.getActiveCamera().getEntity().getY();
        renderer.drawMap(Game.getGame().getScreen().getOrthographicProjection(), cameraTranslation);
        renderer.drawEntities(Game.getGame().getScreen().getOrthographicProjection(), cameraTranslation);
    }

    /**
     * @param level
     *            The asset name of the level
     * @return A newly created Scene.
     * @throws SceneCreationException
     */
    public void loadScene(final String level) throws SceneCreationException {

        Game.instantiate(TileMap.class, Integer.valueOf(2000), Integer.valueOf(1500));
        final PlayerPawn entity = Game.instantiate(PlayerPawn.class, Integer.valueOf(1), Integer.valueOf(2));
        final StaticSprite sprite = Game.instantiate(StaticSprite.class, Integer.valueOf(32), Integer.valueOf(64),
                Renderer.DEFAULT_TEXTURE, Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(16),
                Integer.valueOf(32));
        entity.addComponent(sprite);
        final Camera camera = cameras.instantiateCamera();
        entity.addComponent(camera);
        cameras.activate(camera);
        add(entity);
        entity.setScene(this);
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
