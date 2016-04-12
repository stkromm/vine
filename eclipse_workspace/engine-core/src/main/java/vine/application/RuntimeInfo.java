package vine.application;

public class RuntimeInfo {
    /**
     * @return The memory, used by the application.
     */
    public static long getUsedMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getAvailableMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static long getMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static int getProcessorCoreCount() {
        return java.lang.management.ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    }
}
