package vine.platform.lwjgl3;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import vine.display.Display;

/**
 * Implements a Display with the GLFW API. Doesn't work, if there is no GLFW
 * window existing.
 * 
 * @author Steffen
 *
 */
public final class GLFWDisplay implements Display {
    // Get the resolution of the primary monitor
    private GLFWVidMode vidmode;
    private long currentMonitor = GLFW.glfwGetPrimaryMonitor();
    private final GLFWMonitorCallback monitorCallback = GLFWMonitorCallback.create((l, e) -> this.changeMonitor(l, e));

    /**
     * Default display constructor, uses the system primary monitor.
     */
    public GLFWDisplay() {
        this(GLFW.glfwGetPrimaryMonitor());
    }

    /**
     * @param preferredMonitor
     *            The monitor, used to contain windows.
     */
    public GLFWDisplay(long preferredMonitor) {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (GLFW.glfwInit() != GLFW.GLFW_TRUE) {
            throw new IllegalStateException("Failed to init glfw");
        }
        GLFW.glfwSetMonitorCallback(this.monitorCallback);
        final GLFWVidMode prefVidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (prefVidMode != null) {
            this.currentMonitor = preferredMonitor;
            this.vidmode = prefVidMode;
        } else {
            this.currentMonitor = GLFW.glfwGetPrimaryMonitor();
            this.vidmode = prefVidMode;
        }
    }

    private void changeMonitor(long monitor, int event) {
        if (monitor == this.currentMonitor && event == GLFW.GLFW_DISCONNECTED) {
            this.vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        }
    }

    @Override
    public String[] getMonitorNames() {
        final PointerBuffer monitors = GLFW.glfwGetMonitors();
        final String[] monitorNames = new String[monitors.capacity()];
        for (int i = 0; i < monitors.capacity(); i++) {
            monitorNames[i] = GLFW.glfwGetMonitorName(monitors.get(i));
        }
        return monitorNames;
    }

    @Override
    public int getWidth() {
        return this.vidmode.width();
    }

    @Override
    public int getRedBits() {
        return this.vidmode.redBits();
    }

    @Override
    public int getHeight() {
        return this.vidmode.height();
    }

    @Override
    public int getRefreshRate() {
        return this.vidmode.refreshRate();
    }

    @Override
    public int getBlueBits() {
        return this.vidmode.blueBits();
    }

    @Override
    public int getGreenBits() {
        return this.vidmode.greenBits();
    }

    @Override
    public String getMonitorName() {
        return GLFW.glfwGetMonitorName(GLFW.glfwGetPrimaryMonitor());
    }

    @Override
    public long getPrimaryMonitor() {
        return GLFW.glfwGetPrimaryMonitor();
    }
}
