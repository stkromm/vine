package vine.gameplay.scene;

import java.util.ArrayList;
import java.util.List;

import vine.game.Game;
import vine.gameplay.component.Camera;

/**
 * @author Steffen
 *
 */
public class Scene extends Layer {
    /**
     * 
     */
    public final CameraManager cameras = new CameraManager();

    @Override
    public void render() {
        entities.stream().forEach(renderer::submit);
        renderer.flushTiles(this);
        renderer.flushChars(this);
    }

    /**
     * @param level
     *            The asset name of the level
     * @return A newly created Scene.
     * @throws SceneCreationException
     */
    public static Scene createScene(final String level) throws SceneCreationException {
        return new Scene();
    }

    /**
     * @param delta
     *            The time that has passed since last update.
     */
    public void update(final float delta) {
        entities.stream().forEach(e -> e.update(delta));
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
