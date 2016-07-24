package vine.platform.lwjgl3;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import vine.device.display.Display;
import vine.graphics.Icon;
import vine.util.BufferConverter;
import vine.util.Clipboard;
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
    private static final int            GL_TRUE   = 1;

    private int                         xPos;
    private int                         yPos;

    private int                         width;
    private int                         height;

    private int                         bufferWidth;
    private int                         bufferHeight;

    private long                        window;

    private WindowMode                  mode;

    private String                      title     = "My Game";

    // Need hard references, otherwise the callbacks get garbage collected
    private final GLFWErrorCallback     errorCallback;
    private GLFWFramebufferSizeCallback framebufferCallback;
    private GLFWWindowCloseCallback     closeCallback;
    private GLFWWindowPosCallback       positionCallback;
    private GLFWWindowSizeCallback      resizeCallback;

    private WindowContextCallback       contextChangeCallback;

    private final Display               display;
    private final Clipboard             clipboard = new Clipboard()
                                                  {

                                                      @Override
                                                      public String getContent()
                                                      {
                                                          return GLFW.glfwGetClipboardString(window);
                                                      }

                                                      @Override
                                                      public void setContent(final String clipboardContent)
                                                      {
                                                          GLFW.glfwSetClipboardString(window, clipboardContent);
                                                      }
                                                  };

    /**
     * Initializes GLFW.
     */
    public GLFWWindow(final Display display)
    {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!GLFW.glfwInit())
        {
            throw new IllegalStateException("Failed to init glfw");
        }
        this.display = display;
        errorCallback = GLFWErrorCallback.createPrint();
        GLFW.glfwSetErrorCallback(errorCallback);
        // Create GLFW window
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        if (window == 0)
        {
            window = GLFW.glfwCreateWindow(display.getWidth() / 2, display.getHeight() / 2, title, 0, 0);
        }
        if (window == 0)
        {
            throw new IllegalStateException("Failed to init glfw window");
        }
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
    }

    @Override
    public boolean isResizeable()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_RESIZABLE);
    }

    @Override
    public boolean isVisible()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_VISIBLE);
    }

    @Override
    public boolean isFocused()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_FOCUSED);
    }

    @Override
    public boolean isDecorated()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_DECORATED);
    }

    @Override
    public boolean isIconified()
    {
        return GLFWWindow.GL_TRUE == GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_ICONIFIED);
    }

    @Override
    public boolean requestedClose()
    {
        return GLFW.glfwWindowShouldClose(window);
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public int getPosX()
    {
        return xPos;
    }

    @Override
    public int getPosY()
    {
        return yPos;
    }

    @Override
    public int getFramebufferWidth()
    {
        return bufferWidth;
    }

    @Override
    public int getFramebufferHeight()
    {
        return bufferHeight;
    }

    @Override
    public long getContext()
    {
        return window;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public void setTitle(final String title)
    {
        GLFW.glfwSetWindowTitle(window, title);
        this.title = title;
    }

    @Override
    public void setSizeCallback(final SizeCallback callback)
    {
        if (resizeCallback != null)
        {
            resizeCallback.free();
        }
        resizeCallback = GLFWWindowSizeCallback.create((win, w, h) ->
        {
            callback.onSizeChange(w, h);
            width = w;
            height = h;
        });
        GLFW.glfwSetWindowSizeCallback(window, resizeCallback);
    }

    @Override
    public void setPositionCallback(final PositionCallback callback)
    {
        if (positionCallback != null)
        {
            positionCallback.free();
        }
        positionCallback = GLFWWindowPosCallback.create((win, w, h) ->
        {
            callback.onPositionChange(w, h);
            xPos = w;
            yPos = h;
        });
        GLFW.glfwSetWindowPosCallback(window, positionCallback);
    }

    @Override
    public void setFramebufferSizeCallback(final FramebufferSizeCallback callback)
    {
        if (framebufferCallback != null)
        {
            framebufferCallback.free();
        }
        framebufferCallback = GLFWFramebufferSizeCallback.create((win, w, h) ->
        {
            callback.onFramebufferSizeChange(w, h);
            bufferWidth = w;
            bufferHeight = h;
        });
        GLFW.glfwSetFramebufferSizeCallback(window, framebufferCallback);
    }

    @Override
    public void setCloseCallback(final CloseCallback callback)
    {
        if (closeCallback != null)
        {
            closeCallback.free();
        }
        closeCallback = GLFWWindowCloseCallback.create(context -> callback.onCloseRequest());
        GLFW.glfwSetWindowCloseCallback(window, closeCallback);
    }

    @Override
    public void show()
    {
        GLFW.glfwShowWindow(window);
    }

    @Override
    public void setWindowSize(final int width, final int height)
    {
        GLFW.glfwSetWindowSize(window, width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void setWindowPosition(final int x, final int y)
    {
        GLFW.glfwSetWindowPos(window, x, y);
        xPos = x;
        yPos = y;
    }

    @Override
    public void setWindowContextCallback(final WindowContextCallback callback)
    {
        contextChangeCallback = callback;
    }

    @Override
    public void close()
    {
        GLFW.glfwDestroyWindow(window);
        // Terminate GLFW and release the GLFWErrorCallback
        GLFW.glfwTerminate();
    }

    @Override
    public void hide()
    {
        GLFW.glfwHideWindow(window);
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
            goFullscreen();
        break;
        case WINDOWED:
            goWindowed();
        break;
        case WINDOWED_FULLSCREEN:
            goWindowed();
        break;
        default:
        break;
        }
    }

    private void goWindowed()
    {
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        width = display.getWidth() / 3 * 2;
        height = display.getHeight() / 3 * 2;
        mode = WindowMode.WINDOWED;
        switchToNewWindow(width, height);

        GLFW.glfwSetWindowPos(window, display.getWidth() / 3, display.getHeight() / 3);
    }

    private void goFullscreen()
    {
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        width = display.getWidth();
        height = display.getHeight();
        mode = WindowMode.FULLSCREEN;
        switchToNewWindow(display.getWidth(), display.getHeight());
        GLFW.glfwSetWindowPos(window, 0, 0);
    }

    private void switchToNewWindow(final int width, final int height)
    {
        // Release callbacks of the old window
        if (framebufferCallback != null)
        {
            framebufferCallback.free();
        }
        if (closeCallback != null)
        {
            closeCallback.free();
        }
        GLFW.glfwWindowHint(GLFW.GLFW_ACCUM_RED_BITS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_ACCUM_GREEN_BITS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_ACCUM_BLUE_BITS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_ACCUM_ALPHA_BITS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_AUX_BUFFERS, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, display.getRedBits());
        GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, display.getGreenBits());
        GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, display.getBlueBits());
        GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, 8);
        GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 16);
        GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 16);
        GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, display.getRefreshRate());

        // Create and switch to new window
        final long newWindow = GLFW.glfwCreateWindow(width, height, title, 0, window);
        GLFW.glfwDestroyWindow(window);
        window = newWindow;
        GLFW.glfwMakeContextCurrent(window);

        // Configure new window
        GLFW.glfwSetWindowAspectRatio(window, 16, 9);
        GLFW.glfwShowWindow(window);
        GLFW.glfwSwapInterval(0);

        // Set callbacks to new window
        GLFW.glfwSetWindowCloseCallback(window, closeCallback);
        GLFW.glfwSetFramebufferSizeCallback(window, framebufferCallback);
        GLFW.glfwSetWindowPosCallback(window, positionCallback);
        GLFW.glfwSetWindowSizeCallback(window, resizeCallback);
        GLFW.glfwSetWindowCloseCallback(window, closeCallback);

        if (contextChangeCallback != null)
        {
            contextChangeCallback.onContextChange(window);
        }
    }

    @Override
    public Clipboard getClipboard()
    {
        return clipboard;
    }

    Buffer      iconBuffer;
    GLFWImage[] iconImage;

    public void setWindowIcon(final Icon icon)
    {
        if (iconBuffer != null)
        {
            for (int i = 0; i < iconBuffer.capacity(); i++)
            {
                iconBuffer.get(i).free();
            }
            iconBuffer.free();
            iconImage = null;
        }
        iconImage = new GLFWImage[icon.getLevels()];
        iconBuffer = GLFWImage.create(icon.getLevels());
        for (int i = icon.getLevels() - 1; i >= 0; i--)
        {
            iconImage[i] = GLFWImage.create();
            iconImage[i].set(
                    icon.getLevel(i).getSize(),
                    icon.getLevel(i).getSize(),
                    BufferConverter.createByteBuffer(icon.getLevel(i).getData()));
            iconBuffer.put(i, iconImage[i]);
        }

        GLFW.glfwSetWindowIcon(window, iconBuffer);
    }
}
