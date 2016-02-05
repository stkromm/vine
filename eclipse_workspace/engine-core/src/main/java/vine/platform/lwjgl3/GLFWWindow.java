package vine.platform.lwjgl3;

import static org.lwjgl.glfw.GLFW.GLFW_ACCUM_ALPHA_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_ACCUM_BLUE_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_ACCUM_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_ACCUM_RED_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_ALPHA_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_AUX_BUFFERS;
import static org.lwjgl.glfw.GLFW.GLFW_BLUE_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_DEPTH_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_FOCUSED;
import static org.lwjgl.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_ICONIFIED;
import static org.lwjgl.glfw.GLFW.GLFW_RED_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_STENCIL_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAspectRatio;
import static org.lwjgl.glfw.GLFW.glfwSetWindowCloseCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

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
public final class GLFWWindow implements Window {
    private static final int TRUE = 1;
    private int xPos;
    private int yPos;

    private int width;
    private int height;

    private int bufferWidth;
    private int bufferHeight;

    private long window = 0;

    private WindowMode mode;

    private String title = "My Game";

    // Need hard references, otherwise the callbacks get garbage collected
    private GLFWErrorCallback errorCallback;
    private GLFWFramebufferSizeCallback framebufferCallback;
    private GLFWWindowCloseCallback closeCallback;
    private GLFWWindowPosCallback positionCallback;
    private GLFWWindowSizeCallback resizeCallback;

    private WindowContextCallback contextChangeCallback;

    private final Display display;

    /**
     * Initializes GLFW.
     */
    public GLFWWindow(Display display) {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (glfwInit() != GLFW_TRUE) {
            throw new IllegalStateException("Failed to init glfw");
        }
        this.display = display;
        errorCallback = GLFWErrorCallback.createPrint();
        glfwSetErrorCallback(errorCallback);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
        if (window == 0) {
            window = glfwCreateWindow(display.getWidth() / 2, display.getHeight() / 2, title, 0, 0);
        }
        if (window == 0) {
            throw new IllegalStateException("Failed to init glfw window");
        }
    }

    @Override
    public final boolean isResizeable() {
        return TRUE == glfwGetWindowAttrib(window, GLFW_RESIZABLE);
    }

    @Override
    public final boolean isVisible() {
        return TRUE == glfwGetWindowAttrib(window, GLFW_VISIBLE);
    }

    @Override
    public final boolean isFocused() {
        return TRUE == glfwGetWindowAttrib(window, GLFW_FOCUSED);
    }

    @Override
    public final boolean isDecorated() {
        return TRUE == glfwGetWindowAttrib(window, GLFW_DECORATED);
    }

    @Override
    public boolean isIconified() {
        return TRUE == glfwGetWindowAttrib(window, GLFW_ICONIFIED);
    }

    @Override
    public boolean requestedClose() {
        return glfwWindowShouldClose(window) == TRUE;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getPosX() {
        return xPos;
    }

    @Override
    public int getPosY() {
        return yPos;
    }

    @Override
    public int getFramebufferWidth() {
        return bufferWidth;
    }

    @Override
    public int getFramebufferHeight() {
        return bufferHeight;
    }

    @Override
    public long getContext() {
        return window;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(final String title) {
        glfwSetWindowTitle(window, title);
        this.title = title;
    }

    @Override
    public void setSizeCallback(final SizeCallback callback) {
        if (resizeCallback != null) {
            resizeCallback.release();
        }
        resizeCallback = GLFWWindowSizeCallback.create((win, w, h) -> {
            callback.onSizeChange(w, h);
            this.width = w;
            this.height = h;
        });
        glfwSetWindowSizeCallback(window, resizeCallback);
    }

    @Override
    public void setPositionCallback(final PositionCallback callback) {
        if (positionCallback != null) {
            positionCallback.release();
        }
        positionCallback = GLFWWindowPosCallback.create((win, w, h) -> {
            callback.onPositionChange(w, h);
            this.xPos = w;
            this.yPos = h;
        });
        glfwSetWindowPosCallback(window, positionCallback);
    }

    @Override
    public void setFramebufferSizeCallback(final FramebufferSizeCallback callback) {
        if (framebufferCallback != null) {
            framebufferCallback.release();
        }
        framebufferCallback = GLFWFramebufferSizeCallback.create((win, w, h) -> {
            callback.onFramebufferSizeChange(w, h);
            this.bufferWidth = w;
            this.bufferHeight = h;
        });
        glfwSetFramebufferSizeCallback(window, framebufferCallback);
    }

    @Override
    public void setCloseCallback(final CloseCallback callback) {
        if (closeCallback != null) {
            closeCallback.release();
        }
        closeCallback = GLFWWindowCloseCallback.create(context -> callback.onCloseRequest());
        glfwSetWindowCloseCallback(window, closeCallback);
    }

    @Override
    public void show() {
        glfwShowWindow(window);
    }

    @Override
    public void setWindowSize(final int width, final int height) {
        glfwSetWindowSize(window, width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void setWindowPosition(final int x, final int y) {
        glfwSetWindowPos(window, x, y);
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public void setWindowContextCallback(WindowContextCallback callback) {
        this.contextChangeCallback = callback;
    }

    @Override
    public void close() {
        glfwDestroyWindow(window);
        // Terminate GLFW and release the GLFWErrorCallback
        glfwTerminate();
    }

    @Override
    public void hide() {
        glfwHideWindow(window);
    }

    @Override
    public void setWindowMode(WindowMode mode) {
        if (this.mode == mode) {
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

    private final void goWindowed() {
        glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_ACCUM_RED_BITS, 0);
        glfwWindowHint(GLFW_ACCUM_GREEN_BITS, 0);
        glfwWindowHint(GLFW_ACCUM_BLUE_BITS, 0);
        glfwWindowHint(GLFW_ACCUM_ALPHA_BITS, 0);
        glfwWindowHint(GLFW_AUX_BUFFERS, 0);
        glfwWindowHint(GLFW_SAMPLES, 0);
        glfwWindowHint(GLFW_RED_BITS, display.getRedBits());
        glfwWindowHint(GLFW_GREEN_BITS, display.getGreenBits());
        glfwWindowHint(GLFW_BLUE_BITS, display.getBlueBits());
        glfwWindowHint(GLFW_ALPHA_BITS, 8);
        glfwWindowHint(GLFW_DEPTH_BITS, 24);
        glfwWindowHint(GLFW_STENCIL_BITS, 8);
        glfwWindowHint(GLFW_REFRESH_RATE, display.getRefreshRate());
        this.width = display.getWidth() / 3 * 2;
        this.height = display.getHeight() / 3 * 2;
        this.mode = WindowMode.WINDOWED;
        performWindowChange(width, height);

        glfwSetWindowPos(window, display.getWidth() / 3, display.getHeight() / 3);
    }

    private final void goFullscreen() {
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_ACCUM_RED_BITS, 0);
        glfwWindowHint(GLFW_ACCUM_GREEN_BITS, 0);
        glfwWindowHint(GLFW_ACCUM_BLUE_BITS, 0);
        glfwWindowHint(GLFW_ACCUM_ALPHA_BITS, 0);
        glfwWindowHint(GLFW_AUX_BUFFERS, 0);
        glfwWindowHint(GLFW_SAMPLES, 0);
        glfwWindowHint(GLFW_RED_BITS, display.getRedBits());
        glfwWindowHint(GLFW_GREEN_BITS, display.getGreenBits());
        glfwWindowHint(GLFW_BLUE_BITS, display.getBlueBits());
        glfwWindowHint(GLFW_ALPHA_BITS, 8);
        glfwWindowHint(GLFW_DEPTH_BITS, 24);
        glfwWindowHint(GLFW_STENCIL_BITS, 8);
        glfwWindowHint(GLFW_REFRESH_RATE, display.getRefreshRate());
        this.width = display.getWidth();
        this.height = display.getHeight();
        this.mode = WindowMode.FULLSCREEN;
        performWindowChange(display.getWidth(), display.getHeight());

        glfwSetWindowPos(window, 0, 0);
    }

    private final void performWindowChange(int width, int height) {
        long newWindow = glfwCreateWindow(width, height, title, 0, window);
        glfwDestroyWindow(window);
        window = newWindow;
        glfwSetWindowAspectRatio(window, 16, 9);
        glfwSetWindowCloseCallback(window, closeCallback);
        glfwSetFramebufferSizeCallback(window, framebufferCallback);
        glfwSetWindowPosCallback(window, positionCallback);
        glfwSetWindowSizeCallback(window, resizeCallback);
        glfwSetWindowCloseCallback(window, closeCallback);
        // Make the window visible
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        if (contextChangeCallback != null) {
            contextChangeCallback.onContextChange(window);
        }
    }
}
