package vine.game.scene;

import java.util.ArrayList;
import java.util.List;

import vine.game.Camera;

public class CameraManager
{
    private final List<Camera> managedCameras = new ArrayList<>();
    private Camera             activeCamera;

    /**
     * 
     */
    protected CameraManager()
    {
        // Default Constructor visible, so only Scene can instantiate a
        // camera manager.
    }

    /**
     * @return The camera thats viewport is currently used to render.
     */
    public Camera getActiveCamera()
    {
        return this.activeCamera;
    }

    /**
     * @param camera
     *            Camera, that should be managed
     */
    public void removeCamera(final Camera camera)
    {
        this.managedCameras.remove(camera);
    }

    /**
     * Use this method to create new cameras.
     * 
     * @return A camera that is usable in this Scene
     */
    public Camera createCamera()
    {
        final Camera camera = new Camera();
        this.managedCameras.add(camera);
        return camera;
    }

    /**
     * @param camera
     *            The camera to activate
     */
    public void activate(final Camera camera)
    {
        if (this.managedCameras.contains(camera))
        {
            this.activeCamera = camera;
        }
    }

}
