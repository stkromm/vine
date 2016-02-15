package vine.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    private final Map<String, String> settings = new HashMap<>();
    private final List<Configurable> configurables = new ArrayList<>();
    private final String iniPath;
    private boolean dirty = true;

    public Configuration(final String iniPath) {
        this.iniPath = iniPath;
    }

    private void cleanSettings() {
        // TODO implement
    }

    /**
     * @return True, if the current Settings are applied to all potential
     *         configurable components.
     */
    public boolean isApplied() {
        return !dirty;
    }

    public void addConfigurable(final Configurable configurable) {
        if (configurable == null) {
            return;
        }
        configurables.add(configurable);
        dirty = true;
    }

    public void apply() {
        if (!dirty) {
            return;
        }
        for (final Configurable setting : configurables) {
            setting.applyConfigs(settings);
        }
        dirty = false;
    }

    public void save() {
        cleanSettings();
        ConfigSerializer.saveIniToFile(settings, iniPath);
    }

    public void putSetting(final String key, final String value) {
        dirty = true;
        settings.put(key, value);
    }

    public void load() {
        ConfigurationParser.parseIniFile(iniPath).forEach((key, value) -> putSetting(key, value));
        cleanSettings();
    }
}
