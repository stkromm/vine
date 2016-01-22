package vine.platform.lwjgl3;

import java.util.ResourceBundle;

@SuppressWarnings("javadoc")
public class Messages {
    private static final String BUNDLE_NAME = "com.vine.platform.desktop.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages() {
    }

    public static String getString(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }
}
