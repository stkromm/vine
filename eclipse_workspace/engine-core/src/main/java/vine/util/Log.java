package vine.util;

import java.util.Formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a proxy for logging with a smarter syntax. Use placeholder string
 * with parameter lists to avoid string concatenation for cases where logging is
 * disabled. Therefore no guarding of the log statements is needed.
 * 
 * @author Steffen
 *
 */
public final class Log
{
    private static final Logger    LOGGER         = LoggerFactory.getLogger(Log.class);
    private static final String    LIFECYCLE_MARK = "LIFECYCLE] ";
    private static final String    DEBUG_MARK     = "DEBUG] ";
    private static final String    ERROR_MARK     = "ERROR] ";
    private static final String    BENCHMARK_MARK = "BENCHMARK] ";
    private static final Formatter formatter      = new Formatter();

    private Log()
    {

    }

    public static void debug(final float value)
    {
        if (Log.LOGGER.isDebugEnabled())
        {
            Log.LOGGER.debug(Log.DEBUG_MARK + value);
        }
    }

    public static void debug(final String message, final Object... params)
    {
        if (Log.LOGGER.isDebugEnabled())
        {
            if (params.length == 0)
            {
                Log.LOGGER.debug(Log.DEBUG_MARK + " " + message);
            } else
            {

                Log.LOGGER.debug(Log.DEBUG_MARK + " " + Log.formatter.format(message, params));
            }
        }
    }

    public static void debug(final Object context, final String message, final Object... params)
    {
        if (Log.LOGGER.isDebugEnabled())
        {
            if (params.length > 0)
            {
                Log.LOGGER.debug(Log.DEBUG_MARK + context.getClass().getSimpleName() + " " + message, context);
            } else
            {

                Log.LOGGER.debug(
                        Log.DEBUG_MARK + context.getClass().getSimpleName() + " "
                                + Log.formatter.format(message, params),
                        context);
            }
        }
    }

    public static void exception(final String message, final Throwable throwable, final Object... params)
    {
        if (Log.LOGGER.isErrorEnabled())
        {
            Log.LOGGER.error(Log.ERROR_MARK + Log.formatter.format(message, params), throwable);
        }
    }

    public static void exception(final Object context, final String message, final Throwable throwable)
    {
        if (Log.LOGGER.isErrorEnabled())
        {
            Log.LOGGER.error(Log.ERROR_MARK + message, throwable, context);
        }
    }

    public static void lifecycle(final String message)
    {
        if (Log.LOGGER.isInfoEnabled())
        {
            Log.LOGGER.info(message);
        }
    }

    public static void lifecycle(final Object context, final String message, final Object... params)
    {
        if (Log.LOGGER.isInfoEnabled())
        {
            Log.LOGGER.info(
                    Log.LIFECYCLE_MARK + context.getClass().getSimpleName() + " "
                            + Log.formatter.format(message, params),
                    context);
        }
    }

    public static void lifecycle(final Object context, final String message)
    {
        if (Log.LOGGER.isInfoEnabled())
        {
            Log.LOGGER.info(Log.LIFECYCLE_MARK + context.getClass().getSimpleName() + " " + message, context);
        }
    }

    public static void benchmark(final String message)
    {
        if (Log.LOGGER.isTraceEnabled())
        {
            Log.LOGGER.trace(message);
        }
    }

    public static void benchmark(final Object context, final String message)
    {
        if (Log.LOGGER.isTraceEnabled())
        {
            Log.LOGGER.trace(Log.BENCHMARK_MARK + context.getClass().getSimpleName() + " " + message, context);
        }
    }
}
