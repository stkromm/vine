package vine.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log
{
    private static final Logger LOGGER         = LoggerFactory.getLogger(Log.class);
    public static final String  LIFECYCLE_MARK = "LIFECYCLE] ";
    public static final String  DEBUG_MARK     = "DEBUG] ";
    public static final String  ERROR_MARK     = "ERROR] ";
    public static final String  BENCHMARK_MARK = "BENCHMARK] ";

    private Log()
    {

    }

    public static void debug(final String message)
    {
        if (Log.LOGGER.isDebugEnabled())
        {
            Log.LOGGER.debug(Log.DEBUG_MARK + message);
        }
    }

    public static void debug(final Object context, final String message)
    {
        if (Log.LOGGER.isDebugEnabled())
        {
            Log.LOGGER.debug(Log.DEBUG_MARK + context.getClass().getSimpleName() + " " + message, context);
        }
    }

    public static void exception(final String message, final Throwable throwable)
    {
        if (Log.LOGGER.isErrorEnabled())
        {
            Log.LOGGER.error(Log.ERROR_MARK + message, throwable);
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
            Log.lifecycleUnchecked(message);
        }
    }

    public static void lifecycleUnchecked(final String message)
    {
        Log.LOGGER.info(Log.LIFECYCLE_MARK + message);
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
            Log.benchmarkUnchecked(message);
        }
    }

    public static void benchmarkUnchecked(final String message)
    {
        Log.LOGGER.trace(Log.BENCHMARK_MARK + message);
    }

    public static void benchmark(final Object context, final String message)
    {
        if (Log.LOGGER.isTraceEnabled())
        {
            Log.LOGGER.trace(Log.BENCHMARK_MARK + context.getClass().getSimpleName() + " " + message, context);
        }
    }

    public static boolean isBenachmarkEnabled()
    {
        return Log.LOGGER.isTraceEnabled();
    }

    public static boolean isLifecycleEnabled()
    {
        return Log.LOGGER.isInfoEnabled();
    }
}
