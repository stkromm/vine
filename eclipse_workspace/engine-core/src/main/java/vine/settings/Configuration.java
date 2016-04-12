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
        return !this.dirty;
    }

    public void addConfigurable(final Configurable configurable) {
        if (configurable == null) {
            return;
        }
        this.configurables.add(configurable);
        this.dirty = true;
    }

    public void apply() {
        if (!this.dirty) {
            return;
        }
        for (final Configurable setting : this.configurables) {
            setting.applyConfigs(this.settings);
        }
        this.dirty = false;
    }

    public void save() {
        this.cleanSettings();
        ConfigSerializer.saveIniToFile(this.settings, this.iniPath);
    }

    public void putSetting(final String key, final String value) {
        this.dirty = true;
        this.settings.put(key, value);
    }

    public void load() {
        ConfigurationParser.parseIniFile(this.iniPath).forEach((key, value) -> this.putSetting(key, value));
        this.cleanSettings();
    }
}
