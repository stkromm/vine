package vine.platform.lwjgl3;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import vine.display.Display;
import vine.window.Window;
import vine.window.WindowMode;

/**
 * Lwjgl, Open-GL Desktop platform implementation of Window adapter.
 * 
 * @author Steffen
 *
 */
public final class GLFWWindow implements Window
{
    private static final int            GL_TRUE = 1;

    private int                         xPos;
    private int                         yPos;

    private int                         width;
    private int                         height;

    private int                         bufferWidth;
    private int                         bufferHeight;

    private long                        window;

    private WindowMode                  mode;

    private String                      title   = "My Game";

    // Need hard references, otherwise the callbacks get garbage collected
    private final GLFWErrorCallback     errorCallback;
    private GLFWFramebufferSizeCallback framebufferCallback;
    private GLFWWindowCloseCallback     closeCallback;
    private GLFWWindowPosCallback       positionCallback;
    private GLFWWindowSizeCallback      resizeCallback;

    private WindowContextCallback       contextChangeCallback;

    private final Display               display;

    /**
     * Initializes GLFW.
     */
    public GLFWWindow(final Display display)
    {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (GLFW.glfwInit() != GLFW.GLFW_TRUE)
        {
            throw new IllegalStateException("Failed to init glfw");
        }
        this.display = display;
        this.errorCallback = GLFWErrorCallback.createPrint();
        GLFW.glfwSetErrorCallback(this.errorCallback);
        // Create GLFW window
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        if (this.window == 0)
        {
            this.window = GLFW.glfwCreateWindow(display.getWidth() / 2, display.getHeight() / 2, this.title, 0, 0);
        }
        if (this.window == 0)
        {
            throw new IllegalStateException("Failed to init glfw window");
        }
        GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        GLFW.glfwMakeContextCurrent(this.window);
        GLFW.glfwSwapInterval(1);
    }

    @Override
    public boolean isResizeable()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(this.window, GLFW.GLFW_RESIZABLE);
    }

    @Override
    public boolean isVisible()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(this.window, GLFW.GLFW_VISIBLE);
    }

    @Override
    public boolean isFocused()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(this.window, GLFW.GLFW_FOCUSED);
    }

    @Override
    public boolean isDecorated()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(this.window, GLFW.GLFW_DECORATED);
    }

    @Override
    public boolean isIconified()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(this.window, GLFW.GLFW_ICONIFIED);
    }

    @Override
    public boolean requestedClose()
    {
        return GLFW.glfwWindowShouldClose(this.window) == GLFWWindow.GL_TRUE;
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.height;
    }

    @Override
    public int getPosX()
    {
        return this.xPos;
    }

    @Override
    public int getPosY()
    {
        return this.yPos;
    }

    @Override
    public int getFramebufferWidth()
    {
        return this.bufferWidth;
    }

    @Override
    public int getFramebufferHeight()
    {
        return this.bufferHeight;
    }

    @Override
    public long getContext()
    {
        return this.window;
    }

    @Override
    public String getTitle()
    {
        return this.title;
    }

    @Override
    public void setTitle(final String title)
    {
        GLFW.glfwSetWindowTitle(this.window, title);
        this.title = title;
    }

    @Override
    public void setSizeCallback(final SizeCallback callback)
    {
        if (this.resizeCallback != null)
        {
            this.resizeCallback.free();
        }
        this.resizeCallback = GLFWWindowSizeCallback.create((win, w, h) ->
        {
            callback.onSizeChange(w, h);
            this.width = w;
            this.height = h;
        });
        GLFW.glfwSetWindowSizeCallback(this.window, this.resizeCallback);
    }

    @Override
    public void setPositionCallback(final PositionCallback callback)
    {
        if (this.positionCallback != null)
        {
            this.positionCallback.free();
        }
        this.positionCallback = GLFWWindowPosCallback.create((win, w, h) ->
        {
            callback.onPositionChange(w, h);
            this.xPos = w;
            this.yPos = h;
        });
        GLFW.glfwSetWindowPosCallback(this.window, this.positionCallback);
    }

    @Override
    public void setFramebufferSizeCallback(final FramebufferSizeCallback callback)
    {
        if (this.framebufferCallback != null)
        {
            this.framebufferCallback.free();
        }
        this.framebufferCallback = GLFWFramebufferSizeCallback.create((win, w, h) ->
        {
            callback.onFramebufferSizeChange(w, h);
            this.bufferWidth = w;
            this.bufferHeight = h;
        });
        GLFW.glfwSetFramebufferSizeCallback(this.window, this.framebufferCallback);
    }

    @Override
    public void setCloseCallback(final CloseCallback callback)
    {
        if (this.closeCallback != null)
        {
            this.closeCallback.free();
        }
        this.closeCallback = GLFWWindowCloseCallback.create(context -> callback.onCloseRequest());
        GLFW.glfwSetWindowCloseCallback(this.window, this.closeCallback);
    }

    @Override
    public void show()
    {
        GLFW.glfwShowWindow(this.window);
    }

    @Override
    public void setWindowSize(final int width, final int height)
    {
        GLFW.glfwSetWindowSize(this.window, width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void setWindowPosition(final int x, final int y)
    {
        GLFW.glfwSetWindowPos(this.window, x, y);
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public void setWindowContextCallback(final WindowContextCallback callback)
    {
        this.contextChangeCallback = callback;
    }

    @Override
    public void close()
    {
        GLFW.glfwDestroyWindow(this.window);
        // Terminate GLFW and release the GLFWErrorCallback
        GLFW.glfwTerminate();
    }

    @Override
    public void hide()
    {
        GLFW.glfwHideWindow(this.window);
    }

    @Override
    public void setWindowMode(final WindowMode mode)
    {
        if (this.mode == mode)
        {
            return;
        }
        switch (mode) {
        case FULLSCREEN:
            this.goFullscreen();
        break;
        case WINDOWED:
            this.goWindowed();
        break;
        case WINDOWED_FULLSCREEN:
            this.goWindowed();
        break;
        default:
        break;
        }
    }

    private void goWindowed()
    {
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        this.width = this.display.getWidth() / 3 * 2;
        this.height = this.display.getHeight() / 3 * 2;
        this.mode = WindowMode.WINDOWED;
        this.switchToNewWindow(this.width, this.height);

        GLFW.glfwSetWindowPos(this.window, this.display.getWidth() / 3, this.display.getHeight() / 3);
    }

    private void goFullscreen()
    {
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        this.width = this.display.getWidth();
        this.height = this.display.getHeight();
        this.mode = WindowMode.FULLSCREEN;
        this.switchToNewWindow(this.display.getWidth(), this.display.getHeight());

        GLFW.glfwSetWindowPos(this.window, 0, 0);
    }

    private void switchToNewWindow(final int width, final int height)
    {
        // Release callbacks of the old window
        if (this.framebufferCallback != null)
        {
            this.framebufferCallback.free();
        }
        if (this.closeCallback != null)
        {
            this.closeCallback.free();
        }
        GLFW.glfwWindowHint(GLFW.GLFW_ACCUM_RED_BITS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_ACCUM_GREEN_BITS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_ACCUM_BLUE_BITS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_ACCUM_ALPHA_BITS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_AUX_BUFFERS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, this.display.getRedBits());
        GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, this.display.getGreenBits());
        GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, this.display.getBlueBits());
        GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, 8);
        GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);
        GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 8);
        GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, this.display.getRefreshRate());

        // Create and switch to new window
        final long newWindow = GLFW.glfwCreateWindow(width, height, this.title, 0, this.window);
        GLFW.glfwDestroyWindow(this.window);
        this.window = newWindow;
        GLFW.glfwMakeContextCurrent(this.window);

        // Configure new window
        GLFW.glfwSetWindowAspectRatio(this.window, 16, 9);
        GLFW.glfwShowWindow(this.window);
        GLFW.glfwSwapInterval(0);

        // Set callbacks to new window
        GLFW.glfwSetWindowCloseCallback(this.window, this.closeCallback);
        GLFW.glfwSetFramebufferSizeCallback(this.window, this.framebufferCallback);
        GLFW.glfwSetWindowPosCallback(this.window, this.positionCallback);
        GLFW.glfwSetWindowSizeCallback(this.window, this.resizeCallback);
        GLFW.glfwSetWindowCloseCallback(this.window, this.closeCallback);

        if (this.contextChangeCallback != null)
        {
            this.contextChangeCallback.onContextChange(this.window);
        }
    }
}
