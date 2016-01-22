package vine.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic method performed on files and filepaths.
 * 
 * @author Steffen
 *
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * Loads the content of the file as a string.
     * 
     * @param file
     *            The file path
     * @return The file text string
     */
    public static String loadFileAsString(String file) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer + '\n');
            }
            reader.close();
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Failed to load resource file!", e);
        }
        return result.toString();
    }

}
