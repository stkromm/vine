package vine.window;

import java.util.Arrays;
import java.util.List;

import vine.settings.Configurable;

public class WindowConfig extends Configurable {

    public WindowConfig(final Window window) {
        if (window == null) {
            throw new IllegalArgumentException("The window of WindowConfig must be valid.");
        }
        final SettingHandler mode = windowModeHandler(window);
        this.properties.put(mode.getKey(), mode);
    }

    private final SettingHandler windowModeHandler(final Window window) {
        return new SettingHandler() {
            private WindowMode value = WindowMode.WINDOWED;

            private final String[] options = new String[] { WindowMode.FULLSCREEN.toString(),
                    WindowMode.WINDOWED.toString(), WindowMode.WINDOWED_FULLSCREEN.toString() };

            @Override
            public final void apply(final String value) {
                final WindowMode mode = WindowMode.valueOf(value);
                if (mode != null) {
                    window.setWindowMode(WindowMode.valueOf(value));
                    this.value = mode;
                }
            }

            @Override
            public final String getKey() {
                return "Window_Mode";
            }

            @Override
            public final String getCurrentValue() {
                return value.toString();
            }

            @Override
            public final List<String> getOptions() {
                return Arrays.asList(options);
            }
        };
    }

}
