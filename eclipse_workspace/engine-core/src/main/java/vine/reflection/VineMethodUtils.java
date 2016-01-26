package vine.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steffen
 *
 */
public final class VineMethodUtils {
    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(VineMethodUtils.class);

    private VineMethodUtils() {
    }

    /**
     * @param method
     *            The method to invoke
     * @param object
     *            The object, on which the method gets invoked
     * @param params
     *            The parameters, with which the method gets invoked on the
     *            object.
     */
    public static void invokeMethodOn(final Method method, final Object object, final Object... params) {
        try {
            method.invoke(object, params);
        } catch (IllegalAccessException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("The construct method you've implemented in the class " + object.getClass().toString()
                        + " is not public.", e);
            }
        } catch (IllegalArgumentException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("The supplied arguments " + Arrays.toString(params)
                        + " don't match the defined argument list of the given class " + object.getClass().toString()
                        + " construct method.\n Be sure to supply the right number of arguments in the order they are declared in the construct method.",
                        e);
            }
        } catch (InvocationTargetException e) {
            LOGGER.error("Class has not construct method. Have you passed a GameObject inherited class?", e);
        }
    }
}
