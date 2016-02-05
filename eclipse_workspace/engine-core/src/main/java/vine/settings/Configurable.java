package vine.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Configurable {
    protected final Map<String, SettingHandler> properties = new HashMap<>();

    final void applyConfigs(Map<String, String> settings) {
        for (Entry<String, SettingHandler> property : properties.entrySet()) {
            if (settings.containsKey(property.getKey())) {
                property.getValue().apply(settings.get(property.getKey()));
            }
        }
    }
}
