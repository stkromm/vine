package vine.game;

import vine.game.scene.Component;
import vine.graphics.renderer.SpriteBatch;
import vine.math.Vec3f;

/**
 * @author Steffen
 *
 */
public class Camera extends Component
{

    private final Vec3f translation  = new Vec3f(0, 0, 0);
    private float       shakeIntensity;
    private double      shakeScaling = 1;
    private float       shakeDuration;
    private float       remainingShakeDuration;
    private boolean     smooth;

    @Override
    public void onUpdate(final float delta)
    {
        if (this.remainingShakeDuration > 0)
        {
            this.remainingShakeDuration = Math.max(this.remainingShakeDuration - delta / 1000, 0);
        }
    }

    /**
     * @param duration
     *            The duration in seconds of the shake
     * @param shakeIntensity
     *            The intensity. 0 means no shake at all. No value higher than 1
     *            is recommended.
     * @param shakes
     *            Number of shakes in the given duration. 1 Shake means
     *            translation right and left and back to middle.
     * @param smooth
     *            Should the shake ease in and out?
     */
    public void shake(final float duration, final float shakeIntensity, final int shakes, final boolean smooth)
    {
        this.remainingShakeDuration = duration;
        this.shakeIntensity = Math.min(5, Math.max(0, shakeIntensity));
        this.shakeScaling = Math.PI * shakes / duration * 2;
        this.shakeDuration = duration;
        this.smooth = smooth;
    }

    /**
     * @return The world translation of the camera.
     */
    public final Vec3f getTranslation()
    {
        if (this.entity == null)
        {
            return this.translation;
        }
        if (this.remainingShakeDuration == 0)
        {
            this.translation.setX(this.entity.getXPosition());

        } else
        {
            float shakeOffset = this.getEntity().getWorld().getScreen().getWidth() * 0.01f * this.shakeIntensity;
            if (this.smooth)
            {
                final double easeInEaseOut = -Math.pow((this.remainingShakeDuration / this.shakeDuration - 0.5f) * 2, 2)
                        + 1;
                shakeOffset *= easeInEaseOut;
            }
            this.translation.setX(
                    (float) (shakeOffset * Math.sin(this.remainingShakeDuration * this.shakeScaling)
                            + this.entity.getXPosition()));
        }
        this.translation.setY(this.entity.getYPosition());
        return this.translation;
    }

    @Override
    public void onUpdatePhysics(float delta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAttach()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDetach()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeactivation()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivation()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRender(SpriteBatch batcher)
    {
        // TODO Auto-generated method stub

    }
}
