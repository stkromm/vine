package vine.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;

public class SoundClip {

    final Clip clip;
    AudioInputStream audioIn;

    public SoundClip(Clip clip, AudioInputStream audioIn) {
        this.clip = clip;
        this.audioIn = audioIn;
    }
}
