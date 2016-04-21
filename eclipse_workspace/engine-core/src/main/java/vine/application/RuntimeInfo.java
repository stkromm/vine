package vine.application;

public final class RuntimeInfo
{
    public static final String RENDER_THREAD_NAME   = "render";
    public static final String LOGIC_THREAD_NAME    = "logic";
    public static final String SHUTDOWN_THREAD_NAME = "shutdown";
    public static final String STARTUP_THREAD_NAME  = "startup";

    private RuntimeInfo()
    {

    }

    /**
     * @return The memory, used by the application.
     */
    public static long getUsedMemory()
    {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getAvailableMemory()
    {
        return Runtime.getRuntime().freeMemory();
    }

    public static long getMemory()
    {
        return Runtime.getRuntime().maxMemory();
    }

    public static int getProcessorCoreCount()
    {
        return java.lang.management.ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    }
}
