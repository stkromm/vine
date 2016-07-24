package vine.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import vine.util.Log;

/**
 * @author Steffen
 *
 */
public final class ReflectUtils
{
    private ReflectUtils()
    {
    }

    public static <T> boolean setField(final Class<T> clazz, final Field field, final Object value, final Object target)
    {
        try
        {
            final String fieldName = field.getName();
            final Method setter = clazz.getMethod(
                    "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                    field.getType());
            setter.setAccessible(true);
            ReflectUtils.invokeMethodOn(setter, target, value);
            return true;
        } catch (final IllegalArgumentException exception)
        {
            Log.exception("Auto-generated catch block", exception);

        } catch (final NoSuchMethodException exception)
        {
            Log.exception("Auto-generated catch block", exception);

        } catch (final SecurityException exception)
        {
            Log.exception("Auto-generated catch block", exception);

        }
        return false;
    }

    public static Object toObject(final Class clazz, final String value)
    {
        if (clazz.toString().equals("byte"))
        {
            return Boolean.parseBoolean(value);
        }
        if (Boolean.class.isAssignableFrom(clazz))
        {
            return Boolean.parseBoolean(value);
        }
        if (clazz.toString().equals("byte"))
        {
            return Byte.parseByte(value);
        }
        if (Byte.class.isAssignableFrom(clazz))
        {
            return Byte.parseByte(value);
        }
        if (clazz.toString().equals("short"))
        {
            return Short.parseShort(value);
        }
        if (Short.class.isAssignableFrom(clazz))
        {
            return Short.parseShort(value);
        }
        if (clazz.toString().equals("int"))
        {
            return Integer.parseInt(value);
        }
        if (clazz.isAssignableFrom(Integer.class))
        {
            return Integer.parseInt(value);
        }
        if (clazz.toString().equals("long"))
        {
            return Long.parseLong(value);
        }
        if (Long.class.isAssignableFrom(clazz))
        {
            return Long.parseLong(value);
        }
        if (clazz.toString().equals("float"))
        {
            return Float.parseFloat(value);
        }
        if (Float.class.isAssignableFrom(clazz))
        {
            return Float.parseFloat(value);
        }
        if (clazz.toString().equals("double"))
        {
            return Double.parseDouble(value);
        }
        if (clazz.isAssignableFrom(Double.class))
        {
            return Double.parseDouble(value);
        }
        return value;
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
    public static void invokeMethodOn(final Method method, final Object object, final Object... params)
    {
        try
        {
            method.invoke(object, params);
        } catch (final IllegalAccessException e)
        {
            Log.exception(
                    "The construct method you've implemented in the class " + object.getClass().toString()
                            + " is not public.",
                    e);
        } catch (final IllegalArgumentException e)
        {
            Log.exception(
                    "The supplied arguments " + Arrays.toString(params)
                            + " don't match the defined argument list of the given class "
                            + object.getClass().toString()
                            + " construct method.\n Be sure to supply the right number of arguments in the order they are declared in the construct method.",
                    e);
        } catch (final InvocationTargetException e)
        {
            Log.exception("Class has not construct method. Have you passed a GameObject inherited class?", e);
        }
    }
}
