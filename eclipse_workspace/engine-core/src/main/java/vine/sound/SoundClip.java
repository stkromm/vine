package vine.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;

import vine.assets.Asset;

/**
 * @author Steffen
 *
 */
public class SoundClip implements Asset {

    final Clip clip;
    final AudioInputStream audioIn;

    /**
     * @param clip
     *            The played audio clip
     * @param audioIn
     *            The audio stream
     */
    public SoundClip(Clip clip, AudioInputStream audioIn) {
        this.clip = clip;
        this.audioIn = audioIn;
    }
}
