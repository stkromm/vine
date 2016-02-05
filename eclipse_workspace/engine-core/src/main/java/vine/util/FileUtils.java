package vine.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic method performed on files and filepaths.
 * 
 * @author Steffen
 *
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Loads the content of the file as a string.
     * 
     * @param path
     *            The file path
     * @return The file text string
     */
    public static String loadFileAsText(final String path) {
        return new String(readBytes(path));
    }

    public static byte[] readBytes(final String path, final int offset) {
        try (InputStream reader = Files.newInputStream(Paths.get(path))) {
            reader.skip(offset);
            byte[] data = new byte[reader.available()];
            reader.read(data);
            return data;
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
        }
        return new byte[] {};
    }

    public static byte[] readBytes(final String path, final int offset, final int length) {
        final byte[] data = new byte[length];
        try (InputStream reader = Files.newInputStream(Paths.get(path))) {
            reader.read(data, offset, length);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
        }
        return data;
    }

    public static byte[] readBytes(final String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
        }
        return new byte[] {};
    }
}
