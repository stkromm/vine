package vine.math;

import java.util.ResourceBundle;

/**
 * @author Steffen
 *
 */
public final class Messages
{
    private static final String         BUNDLE_NAME     = "vine.math.messages";                 //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages()
    {
    }

    /**
     * @param key
     *            key of the string
     * @return The string of the property file
     */
    public static String getString(final String key)
    {
        return RESOURCE_BUNDLE.getString(key);
    }
}
