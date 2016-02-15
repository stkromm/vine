package vine.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Configurable {
    protected final Map<String, SettingHandler> properties = new HashMap<>();

    final void applyConfigs(final Map<String, String> settings) {
        for (final Entry<String, SettingHandler> property : properties.entrySet()) {
            if (settings.containsKey(property.getKey())) {
                property.getValue().apply(settings.get(property.getKey()));
            }
        }
    }
}
