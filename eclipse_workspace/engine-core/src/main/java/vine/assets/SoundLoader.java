package vine.assets;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import vine.sound.SoundClip;

public class SoundLoader extends AssetLoader<SoundClip, AssetLoaderParameters<SoundClip>> {

    @Override
    public SoundClip loadSync(AssetManager manager, String fileName, FileHandle file,
            AssetLoaderParameters<SoundClip> parameter, vine.assets.AssetLoader.ProgressCallback progess) {
        AudioInputStream audioIn;
        try {
            final InputStream f = new FileInputStream(fileName);
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(f));
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
        final FloatControl gainControl = (FloatControl) localClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(3.0f); // Reduce volume by 10 decibels.
        return new SoundClip(localClip, audioIn);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file,
            AssetLoaderParameters<SoundClip> parameter, vine.assets.AssetLoader.FinishCallback callback,
            vine.assets.AssetLoader.ProgressCallback progessCallback) {

    }

}
