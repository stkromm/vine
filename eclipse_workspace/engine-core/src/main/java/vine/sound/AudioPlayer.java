package vine.sound;

import javax.sound.sampled.Clip;

/**
 * @author Steffen
 *
 */
public class AudioPlayer
{
    SoundClip clip;

    /**
     * @param clip
     *            The played clip
     */
    public void setClip(final SoundClip clip)
    {
        this.clip = clip;
    }

    /**
     * 
     */
    public void playOnce()
    {
        if (this.clip.clip.isRunning())
        {
            this.clip.clip.stop();
        }
        this.clip.clip.setFramePosition(0);
        this.clip.clip.start();
    }

    /**
     * 
     */
    public void playLooped()
    {
        if (this.clip.clip.isRunning())
        {
            this.clip.clip.stop();
        }
        this.clip.clip.setFramePosition(0);
        this.clip.clip.loop(Clip.LOOP_CONTINUOUSLY);
        this.clip.clip.start();
    }

    /**
     * 
     */
    public void stop()
    {
        this.clip.clip.stop();
        this.clip.clip.setFramePosition(0);
    }

    /**
     * 
     */
    public void resume()
    {
        this.clip.clip.start();
    }

    /**
     * 
     */
    public void pause()
    {
        this.clip.clip.stop();
    }
}
