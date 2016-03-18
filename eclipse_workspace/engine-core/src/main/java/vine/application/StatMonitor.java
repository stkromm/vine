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

/**
 * @author Steffen
 *
 */
public final class StatMonitor {
    static final Logger LOGGER = LoggerFactory.getLogger(StatMonitor.class);
    private static long lastFrame = System.nanoTime();
    private static float fps = 60;

    private StatMonitor() {

    }

    /**
     * @return The average frames per second the game is rendered with.
     */
    public static float getFPS() {
        return fps;
    }

    /**
     * @return The memory, used by the application.
     */
    public static long getUsedMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static void logGC() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            LOGGER.debug("garbage for:" + gcbeans);
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            // use an anonymously generated listener for this example
            // - proper code should really use a named class
            NotificationListener listener = new NotificationListener() {
                long totalGcDuration = 0;

                @Override
                public void handleNotification(Notification notification, Object handback) {
                    if (notification.getType()
                            .equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                        GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo
                                .from((CompositeData) notification.getUserData());
                        String gctype = info.getGcAction();
                        if ("end of minor GC".equals(gctype)) {
                            gctype = "Young Gen GC";
                        } else if ("end of major GC".equals(gctype)) {
                            gctype = "Old Gen GC";
                        }
                        LOGGER.debug(gctype + ": - " + info.getGcInfo().getId() + " " + info.getGcName() + " (from "
                                + info.getGcCause() + ") " + info.getGcInfo().getDuration()
                                + " microseconds; start-end times " + info.getGcInfo().getStartTime() + "-"
                                + info.getGcInfo().getEndTime());
                        LOGGER.debug("GcInfo CompositeType: " + info.getGcInfo().getCompositeType());
                        LOGGER.debug("GcInfo MemoryUsageAfterGc: " + info.getGcInfo().getMemoryUsageAfterGc());
                        LOGGER.debug("GcInfo MemoryUsageBeforeGc: " + info.getGcInfo().getMemoryUsageBeforeGc());

                        Map<String, MemoryUsage> membefore = info.getGcInfo().getMemoryUsageBeforeGc();
                        Map<String, MemoryUsage> mem = info.getGcInfo().getMemoryUsageAfterGc();
                        for (Entry<String, MemoryUsage> entry : mem.entrySet()) {
                            String name = entry.getKey();
                            MemoryUsage memdetail = entry.getValue();
                            long memCommitted = memdetail.getCommitted();
                            long memMax = memdetail.getMax();
                            long memUsed = memdetail.getUsed();
                            MemoryUsage before = membefore.get(name);
                            long beforepercent = ((before.getUsed() * 1000L) / before.getCommitted());
                            long percent = ((memUsed * 1000L) / before.getCommitted());
                            LOGGER.debug(name + (memCommitted == memMax ? "(fully expanded)" : "(still expandable)")
                                    + "used: " + (beforepercent / 10) + "." + (beforepercent % 10) + "%->"
                                    + (percent / 10) + "." + (percent % 10) + "%(" + ((memUsed / 1048576) + 1)
                                    + "MB) / ");
                        }
                        totalGcDuration += info.getGcInfo().getDuration();
                        long percent = totalGcDuration * 1000L / info.getGcInfo().getEndTime();
                        LOGGER.debug("GC cumulated overhead " + (percent / 10) + "." + (percent % 10) + "%");
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    /**
     * 
     */
    public static void newFrame() {
        float frameDuration = (System.nanoTime() - lastFrame) / (float) 1e9;
        fps = 0.9f * fps + 0.1f * (1f / frameDuration);
        lastFrame = System.nanoTime();
    }
}
