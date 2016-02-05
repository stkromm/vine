package vine.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClip {

    private final Clip clip;
    private AudioInputStream audioIn;

    public SoundClip(String path) {
        try {
            InputStream file = new FileInputStream(new File("E:\\Sound\\music.wav"));
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(file));
        } catch (UnsupportedAudioFileException | IOException e) {
            audioIn = null;
        }
        Clip localClip = null;
        try {
            localClip = AudioSystem.getClip();
            localClip.open(audioIn);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        clip = localClip;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(3.0f); // Reduce volume by 10 decibels.
    }

    public void playOnce() {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void playLooped() {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }
}
