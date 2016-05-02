package vine.game;

import vine.game.scene.Component;
import vine.math.vector.MutableVec3f;
import vine.math.vector.Vec3f;
import vine.util.time.TimerManager;

/**
 * @author Steffen
 *
 */
public class Camera extends Component
{

    private final MutableVec3f translation  = new MutableVec3f(0, 0, 0);
    private float              shakeIntensity;
    private double             shakeScaling = 1;
    private float              shakeDuration;
    private boolean            smooth;
    private int                shakeTimer;

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
        TimerManager.get().createTimer(duration, 1, () -> this.shakeTimer = 0);
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
        if (this.shakeTimer == 0)
        {
            this.translation.setX(this.entity.getXPosition());

        } else
        {
            final float remainingShakeDuration = TimerManager.get().getElapsedTime(this.shakeTimer);
            float shakeOffset = getEntity().getWorld().getScreen().getWidth() * 0.01f * this.shakeIntensity;
            if (this.smooth)
            {
                shakeOffset *= -Math.pow((remainingShakeDuration / this.shakeDuration - 0.5f) * 2, 2) + 1;
            }
            this.translation.setX(
                    (float) (shakeOffset * Math.sin(remainingShakeDuration * this.shakeScaling)
                            + this.entity.getXPosition()));
        }
        this.translation.setY(this.entity.getYPosition());
        return this.translation;
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
    public void onUpdate(final float delta)
    {
        // TODO Auto-generated method stub

    }
}
