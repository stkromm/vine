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
import vine.util.Log;

public class SoundLoader extends AssetLoader<SoundClip, AssetLoaderParameters<SoundClip>>
{

    @SuppressWarnings("resource")
    @Override
    public SoundClip loadSync(final AssetPointer pointer, final AssetLoaderParameters<SoundClip> parameter)
    {
        AudioInputStream audioIn;
        try
        {
            final InputStream f = new FileInputStream(pointer.getPath());
            f.skip(pointer.getOffset());
            f.mark(f.available() - pointer.getLength());
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(f));
        } catch (UnsupportedAudioFileException | IOException e)
        {
            audioIn = null;
        }
        Clip localClip = null;
        try
        {
            localClip = AudioSystem.getClip();
            localClip.open(audioIn);
        } catch (LineUnavailableException | IOException e)
        {
            Log.exception("Failed to load sound " + pointer.getPath(), e);
        }
        if (localClip == null)
        {
            return null;
        } else
        {
            final FloatControl gainControl = (FloatControl) localClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(3.0f); // Reduce volume by 10 decibels.
            return new SoundClip(localClip, audioIn);
        }
    }

    @Override
    public void loadAsync(
            final AssetPointer pointer,
            final AssetLoaderParameters<SoundClip> parameter,
            final vine.assets.AssetLoader.FinishCallback<SoundClip> callback,
            final vine.assets.AssetLoader.ProgressCallback progessCallback)
    {
        //
    }

}
