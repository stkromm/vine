package vine.platform.lwjgl3;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import vine.device.display.Display;

/**
 * Implements a Display with the GLFW API. Doesn't work, if there is no GLFW
 * window existing.
 * 
 * @author Steffen
 *
 */
public final class GLFWDisplay implements Display
{
    // Get the resolution of the primary monitor
    private GLFWVidMode               vidmode;
    private long                      currentMonitor  = GLFW.glfwGetPrimaryMonitor();
    private final GLFWMonitorCallback monitorCallback = GLFWMonitorCallback.create((l, e) -> changeMonitor(l, e));

    /**
     * Default display constructor, uses the system primary monitor.
     */
    public GLFWDisplay()
    {
        this(GLFW.glfwGetPrimaryMonitor());
    }

    /**
     * @param preferredMonitor
     *            The monitor, used to contain windows.
     */
    public GLFWDisplay(final long preferredMonitor)
    {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!GLFW.glfwInit())
        {
            throw new IllegalStateException("Failed to init glfw");
        }
        GLFW.glfwSetMonitorCallback(monitorCallback);
        final GLFWVidMode prefVidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (prefVidMode == null)
        {
            currentMonitor = GLFW.glfwGetPrimaryMonitor();
            vidmode = prefVidMode;
        } else
        {
            currentMonitor = preferredMonitor;
            vidmode = prefVidMode;
        }
    }

    private void changeMonitor(final long monitor, final int event)
    {
        if (monitor == currentMonitor && event == GLFW.GLFW_DISCONNECTED)
        {
            vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        }
    }

    @Override
    public String[] getMonitorNames()
    {
        final PointerBuffer monitors = GLFW.glfwGetMonitors();
        final String[] monitorNames = new String[monitors.capacity()];
        for (int i = 0; i < monitors.capacity(); i++)
        {
            monitorNames[i] = GLFW.glfwGetMonitorName(monitors.get(i));
        }
        return monitorNames;
    }

    @Override
    public int getWidth()
    {
        return vidmode.width();
    }

    @Override
    public int getRedBits()
    {
        return vidmode.redBits();
    }

    @Override
    public int getHeight()
    {
        return vidmode.height();
    }

    @Override
    public int getRefreshRate()
    {
        return vidmode.refreshRate();
    }

    @Override
    public int getBlueBits()
    {
        return vidmode.blueBits();
    }

    @Override
    public int getGreenBits()
    {
        return vidmode.greenBits();
    }

    @Override
    public String getMonitorName()
    {
        return GLFW.glfwGetMonitorName(GLFW.glfwGetPrimaryMonitor());
    }

    @Override
    public long getPrimaryMonitor()
    {
        return GLFW.glfwGetPrimaryMonitor();
    }
}
