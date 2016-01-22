package vine.application;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steffen
 *
 */
public class StatMonitor {
    private static volatile long lastUpdateTime = System.nanoTime();
    private static volatile long lastFrameTime = System.nanoTime();
    private static final List<Long> frameDurations = new ArrayList<>();
    private static final List<Long> upDurations = new ArrayList<>();

    private StatMonitor() {

    }

    /**
     * @return The average updates per seconds the game is calculated with.
     */
    public static float getUps() {
        long frames = 0;
        for (int i = 0; i < upDurations.size(); i++) {
            frames += upDurations.get(i).longValue();
        }
        return 10000000000000f / frames / upDurations.size();
    }

    /**
     * @return The average frames per second the game is rendered with.
     */
    public static float getFPS() {
        long frames = 0;
        for (int i = 0; i < frameDurations.size(); i++) {
            frames += frameDurations.get(i).longValue();
        }
        return 10000000000000f / frames / frameDurations.size();
    }

    /**
     * @return The memory, used by the application.
     */
    public static long getUsedMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * 
     */
    public static void newUp() {
        if (upDurations.size() >= 100) {
            upDurations.remove(0);
        }
        upDurations.add(new Long(System.nanoTime() - lastUpdateTime));
        lastUpdateTime = System.nanoTime();
    }

    /**
     * 
     */
    public static void newFrame() {
        if (frameDurations.size() >= 100) {
            frameDurations.remove(0);
        }
        frameDurations.add(new Long(System.nanoTime() - lastFrameTime));
        lastFrameTime = System.nanoTime();
    }
}
