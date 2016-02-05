package vine.settings;

import java.util.List;

public interface SettingHandler {
    List<String> getOptions();

    void apply(String value);

    String getKey();

    String getCurrentValue();

}
