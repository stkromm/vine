package vine.settings;

import java.util.HashMap;
import java.util.Map;

import vine.util.FileUtils;

/**
 * @author Steffen
 *
 */
final class ConfigurationParser {
    private static final int INI_LINE_FIELDS = 2;

    private ConfigurationParser() {
        
    }
    /**
     * @param path
     * @return
     */
    public static final Map<String, String> parseIniFile(final String path) {
        final String[] lines = FileUtils.loadFileAsText(path).split("\\r?\\n");
        final Map<String, String> values = new HashMap<>();
        for (String line : lines) {
            final String[] configPair = line.split(":");
            if (configPair.length == INI_LINE_FIELDS) {
                values.put(configPair[0].trim(), configPair[1].trim());
            }
        }
        return values;
    }

}
