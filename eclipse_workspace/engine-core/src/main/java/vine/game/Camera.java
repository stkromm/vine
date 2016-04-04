package vine.game;

import vine.application.GamePlayer;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public class Camera extends Component {

    private final Vector3f translation = new Vector3f(0, 0, 0);
    private float shakeIntensity;
    private double shakeScaling = 1;
    private float shakeDuration;
    private float remainingShakeDuration = 0;
    private boolean smooth;

    @Override
    public void update(final float delta) {
        super.update(delta);
        if (this.remainingShakeDuration > 0) {
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
    public void shake(final float duration, final float shakeIntensity, final int shakes, final boolean smooth) {
        this.remainingShakeDuration = duration;
        this.shakeIntensity = Math.min(5, Math.max(0, shakeIntensity));
        this.shakeScaling = Math.PI * shakes / duration * 2;
        this.shakeDuration = duration;
        this.smooth = smooth;
    }

    /**
     * @return The world translation of the camera.
     */
    public final Vector3f getTranslation() {
        if (this.remainingShakeDuration != 0) {
            float shakeOffset = GamePlayer.getRunningGame().getScreen().getWidth() * 0.01f * this.shakeIntensity;
            if (this.smooth) {
                double easeInEaseOut = Math.pow((this.remainingShakeDuration / this.shakeDuration - 0.5f) * 2, 2) * (-1)
                        + 1;
                shakeOffset *= easeInEaseOut;
            }
            this.translation.setX((float) (shakeOffset * Math.sin(this.remainingShakeDuration * this.shakeScaling)
                    + this.entity.getXCoord()));
        } else {
            this.translation.setX(this.entity.getXCoord());
        }
        this.translation.setY(this.entity.getYCoord());
        return this.translation;
    }
}
