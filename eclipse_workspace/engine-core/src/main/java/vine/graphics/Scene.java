package vine.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import vine.game.Game;
import vine.gameplay.component.Camera;
import vine.gameplay.component.Sprite;
import vine.gameplay.entity.GameEntity;

/**
 * @author Steffen
 *
 */
public class Scene extends Layer {
    /**
     * 
     */
    public final CameraManager cameras = new CameraManager();

    /**
     * Empty constructor.
     */
    Scene() {

    }

    @Override
    public void render() {
        entities.stream().forEach(renderer::submit);
        renderer.flush(this);
    }

    /**
     * @author Steffen
     *
     */
    public static class SceneBuilder {
        private SceneBuilder() {

        }

        /**
         * @param level
         *            The asset name of the level
         * @return A newly created Scene.
         * @throws SceneCreationException
         */
        public static Scene createScene(String level) throws SceneCreationException {
            Scene scene = new Scene();
            return scene;
        }
    }

    /**
     * @author Steffen
     *
     */
    public class CameraManager {
        private List<Camera> managedCameras = new ArrayList<>();
        private Camera activeCamera = null;

        /**
         * 
         */
        CameraManager() {
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
        public void removeCamera(Camera camera) {
            managedCameras.remove(camera);
        }

        /**
         * Use this method to create new cameras.
         * 
         * @return A camera that is usable in this Scene
         */
        public Camera instantiateCamera() {
            Camera camera = Game.instantiate(Camera.class);
            managedCameras.add(camera);
            return camera;
        }

        /**
         * @param camera
         *            The camera to activate
         */
        public void activate(Camera camera) {
            if (managedCameras.contains(camera)) {
                activeCamera = camera;
            }
        }
    }

    /**
     * @param delta
     *            The time that has passed since last update.
     */
    public void update(float delta) {
        entities.stream().forEach(e -> e.update(delta));
    }

}