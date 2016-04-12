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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.GarbageCollectionNotificationInfo;

import vine.time.Stopwatch;

/**
 * @author Steffen
 *
 */
public final class PerformanceMonitor {
    static final Logger LOGGER = LoggerFactory.getLogger(PerformanceMonitor.class);
    private static long applicationStart = System.currentTimeMillis();
    private static Stopwatch stopwatch = new Stopwatch(true);
    private static float fps = 60;

    private PerformanceMonitor() {

    }

    /**
     * @return The average frames per second the game is rendered with.
     */
    public static float getFPS() {
        return PerformanceMonitor.fps;
    }

    public static long getTimeSinceStart() {
        return System.currentTimeMillis() - PerformanceMonitor.applicationStart;
    }

    /**
     * 
     */
    public static void captureFrame() {
        final float frameDuration = PerformanceMonitor.stopwatch.stop();
        PerformanceMonitor.fps = 0.75f * PerformanceMonitor.fps + 0.25f * (1000000000 / frameDuration);
    }

    /**
     * 
     */
    public static void logGargabeCollector() {
        final List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory
                .getGarbageCollectorMXBeans();
        for (final GarbageCollectorMXBean gcbean : gcbeans) {
            PerformanceMonitor.LOGGER.debug("garbage for:" + gcbeans);
            final NotificationEmitter emitter = (NotificationEmitter) gcbean;
            // use an anonymously generated listener for this example
            // - proper code should really use a named class
            final NotificationListener listener = new NotificationListener() {
                long totalGcDuration = 0;

                @Override
                public void handleNotification(Notification notification, Object handback) {
                    if (notification.getType()
                            .equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                        final GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo
                                .from((CompositeData) notification.getUserData());
                        String gctype = info.getGcAction();
                        if ("end of minor GC".equals(gctype)) {
                            gctype = "Young Gen GC";
                        } else if ("end of major GC".equals(gctype)) {
                            gctype = "Old Gen GC";
                        }
                        PerformanceMonitor.LOGGER.debug(gctype + ": - " + info.getGcInfo().getId() + " "
                                + info.getGcName() + " (from " + info.getGcCause() + ") "
                                + info.getGcInfo().getDuration() + " microseconds; start-end times "
                                + info.getGcInfo().getStartTime() + "-" + info.getGcInfo().getEndTime());
                        PerformanceMonitor.LOGGER.debug("GcInfo CompositeType: " + info.getGcInfo().getCompositeType());
                        PerformanceMonitor.LOGGER
                                .debug("GcInfo MemoryUsageAfterGc: " + info.getGcInfo().getMemoryUsageAfterGc());
                        PerformanceMonitor.LOGGER
                                .debug("GcInfo MemoryUsageBeforeGc: " + info.getGcInfo().getMemoryUsageBeforeGc());

                        final Map<String, MemoryUsage> membefore = info.getGcInfo().getMemoryUsageBeforeGc();
                        final Map<String, MemoryUsage> mem = info.getGcInfo().getMemoryUsageAfterGc();
                        for (final Entry<String, MemoryUsage> entry : mem.entrySet()) {
                            final String name = entry.getKey();
                            final MemoryUsage memdetail = entry.getValue();
                            final long memCommitted = memdetail.getCommitted();
                            final long memMax = memdetail.getMax();
                            final long memUsed = memdetail.getUsed();
                            final MemoryUsage before = membefore.get(name);
                            final long beforepercent = before.getUsed() * 1000L / before.getCommitted();
                            final long percent = memUsed * 1000L / before.getCommitted();
                            PerformanceMonitor.LOGGER.debug(name
                                    + (memCommitted == memMax ? "(fully expanded)" : "(still expandable)") + "used: "
                                    + beforepercent / 10 + "." + beforepercent % 10 + "%->" + percent / 10 + "."
                                    + percent % 10 + "%(" + (memUsed / 1048576 + 1) + "MB) / ");
                        }
                        this.totalGcDuration += info.getGcInfo().getDuration();
                        final long percent = this.totalGcDuration * 1000L / info.getGcInfo().getEndTime();
                        PerformanceMonitor.LOGGER
                                .debug("GC cumulated overhead " + percent / 10 + "." + percent % 10 + "%");
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

}
