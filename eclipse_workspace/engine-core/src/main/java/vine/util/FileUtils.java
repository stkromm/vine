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

    /**
     * @param path
     *            The absoulte path of a file
     * @param offset
     *            Start offset in the bytes of the file.
     * @return the bytes of the file of the given path with the given offset
     */
    public static byte[] readBytes(final String path, final int offset) {
        try (InputStream reader = Files.newInputStream(Paths.get(path))) {
            reader.skip(offset);
            final byte[] data = new byte[reader.available()];
            reader.read(data);
            return data;
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
        }
        return new byte[] {};
    }

    /**
     * @param path
     *            The absolute path of a file
     * @param offset
     *            Start offset in the bytes of the file.
     * @param length
     *            caps the number of read bytes
     * @return the bytes of the file of the given path until the given length
     */
    public static byte[] readBytes(final String path, final int offset, final int length) {
        final byte[] data = new byte[length];
        try (InputStream reader = Files.newInputStream(Paths.get(path))) {
            reader.read(data, offset, length);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
        }
        return data;
    }

    /**
     * @param path
     *            The absolute path of a file
     * @return The bytes of the file corresponding to the given path
     */
    public static byte[] readBytes(final String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
        }
        return new byte[] {};
    }
}
