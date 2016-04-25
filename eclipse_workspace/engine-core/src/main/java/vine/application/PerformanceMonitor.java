package vine.application;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;

import vine.util.Log;
import vine.util.time.Stopwatch;

/**
 * @author Steffen
 *
 */
public final class PerformanceMonitor
{
    private static long      applicationStart = System.currentTimeMillis();
    private static Stopwatch stopwatch        = new Stopwatch(true);
    private static float     currentFps       = 60;
    private static long      capturedFrames;
    private static float     averageFps       = 60;
    private static float     highestFps       = Integer.MIN_VALUE;
    private static float     lowestFps        = Integer.MAX_VALUE;

    private PerformanceMonitor()
    {

    }

    /**
     * @return The average frames per second the game is rendered with.
     */
    public static float getFPS()
    {
        return PerformanceMonitor.currentFps;
    }

    public static void startFrame()
    {
        PerformanceMonitor.stopwatch.stop();
    }

    public static void endFrame()
    {
        PerformanceMonitor.capturedFrames++;
        final float frameDuration = PerformanceMonitor.stopwatch.stop();
        PerformanceMonitor.currentFps = 0.75f * PerformanceMonitor.currentFps + 0.25f * (1000000000 / frameDuration);
        PerformanceMonitor.highestFps = Math.max(PerformanceMonitor.currentFps, PerformanceMonitor.highestFps);
        PerformanceMonitor.lowestFps = Math.min(PerformanceMonitor.currentFps, PerformanceMonitor.lowestFps);
        PerformanceMonitor.averageFps = (PerformanceMonitor.averageFps * (PerformanceMonitor.capturedFrames - 1)
                + PerformanceMonitor.currentFps) / PerformanceMonitor.capturedFrames;
    }

    /**
     * 
     */
    public static void logGargabeCollector()
    {
        final List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory
                .getGarbageCollectorMXBeans();
        for (final GarbageCollectorMXBean gcbean : gcbeans)
        {
            Log.lifecycle("garbage for:" + gcbeans);
            final NotificationEmitter emitter = (NotificationEmitter) gcbean;
            // use an anonymously generated listener for this example
            // - proper code should really use a named class
            final NotificationListener listener = new NotificationListener()
            {
                long totalGcDuration;

                @Override
                public void handleNotification(final Notification notification, final Object handback)
                {
                    Thread.currentThread().setName("garbage-collection");
                    if (notification.getType()
                            .equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION))
                    {
                        final GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo
                                .from((CompositeData) notification.getUserData());
                        String gctype = info.getGcAction();
                        if ("end of minor GC".equals(gctype))
                        {
                            gctype = "Young Gen GC";
                        } else if ("end of major GC".equals(gctype))
                        {
                            gctype = "Old Gen GC";
                        }
                        Log.benchmark(gctype + ": - " + info.getGcInfo().getId() + " " + info.getGcName() + " (from "
                                + info.getGcCause() + ") " + info.getGcInfo().getDuration()
                                + " microseconds; start-end times " + info.getGcInfo().getStartTime() + "-"
                                + info.getGcInfo().getEndTime());
                        Log.benchmark("GcInfo CompositeType: " + info.getGcInfo().getCompositeType());
                        Log.benchmark("GcInfo MemoryUsageAfterGc: " + info.getGcInfo().getMemoryUsageAfterGc());
                        Log.benchmark("GcInfo MemoryUsageBeforeGc: " + info.getGcInfo().getMemoryUsageBeforeGc());

                        final Map<String, MemoryUsage> membefore = info.getGcInfo().getMemoryUsageBeforeGc();
                        final Map<String, MemoryUsage> mem = info.getGcInfo().getMemoryUsageAfterGc();
                        for (final Entry<String, MemoryUsage> entry : mem.entrySet())
                        {
                            final String name = entry.getKey();
                            final MemoryUsage memdetail = entry.getValue();
                            final long memCommitted = memdetail.getCommitted();
                            final long memMax = memdetail.getMax();
                            final long memUsed = memdetail.getUsed();
                            final MemoryUsage before = membefore.get(name);
                            final long beforepercent = before.getUsed() * 1000L / before.getCommitted();
                            final long percent = memUsed * 1000L / before.getCommitted();
                            Log.benchmark(name + (memCommitted == memMax ? "(fully expanded)" : "(still expandable)")
                                    + "used: " + beforepercent / 10 + "." + beforepercent % 10 + "%->" + percent / 10
                                    + "." + percent % 10 + "%(" + (memUsed / 1048576 + 1) + "MB) / ");
                        }
                        this.totalGcDuration += info.getGcInfo().getDuration();
                        final long percent = this.totalGcDuration * 1000L / info.getGcInfo().getEndTime();
                        Log.lifecycle("GC cumulated overhead " + percent / 10 + "." + percent % 10 + "%");
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    public static float getHighestFps()
    {
        return PerformanceMonitor.highestFps;
    }

    public static float getAverageFps()
    {
        return PerformanceMonitor.averageFps;
    }

    public static float getLowestFps()
    {
        return PerformanceMonitor.lowestFps;
    }

    public static float getRunningTime()
    {
        return System.currentTimeMillis() - PerformanceMonitor.applicationStart;
    }
}
