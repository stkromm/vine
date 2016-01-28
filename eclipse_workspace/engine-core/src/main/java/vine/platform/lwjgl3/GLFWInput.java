package vine.platform.lwjgl3;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCharModsCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCharModsCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import vine.input.Input;

/**
 * @author Steffen
 *
 */
public final class GLFWInput implements Input {
    private double mouseX;
    private double mouseY;
    private long context;

    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;

    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;
    private GLFWCharCallback charCallback;
    private GLFWCharModsCallback charModsCallback;

    /**
     * 
     */
    public GLFWInput() {
        cursorPosCallback = GLFWCursorPosCallback.create((w, posx, posy) -> {
            this.mouseX = posx;
            this.mouseY = posy;
        });
    }

    @Override
    public void pollEvents() {
        glfwPollEvents();
    }

    @Override
    public void setKeyCallback(final KeyCallback callback) {
        if (keyCallback != null) {
            keyCallback.release();
        }
        keyCallback = GLFWKeyCallback
                .create((win, key, scancode, action, mods) -> callback.keyPressed(win, key, scancode, action, mods));
        glfwSetKeyCallback(context, keyCallback);
    }

    @Override
    public void listenToWindow(final long context) {
        this.context = context;
    }

    @Override
    public boolean isReleaseAction(final int action) {
        return action != GLFW.GLFW_RELEASE;
    }

    @Override
    public void setScrollCallback(final ScrollCallback callback) {
        if (scrollCallback != null) {
            scrollCallback.release();
        }
        scrollCallback = GLFWScrollCallback.create((w, offsetx, offsety) -> callback.scrolled(w, offsetx, offsety));
        glfwSetScrollCallback(context, scrollCallback);
    }

    @Override
    public void setCharCallback(final CharCallback callback) {
        if (charCallback != null) {
            charCallback.release();
        }
        charCallback = GLFWCharCallback.create((w, codepoint) -> callback.charInput(w, codepoint));
        glfwSetCharCallback(context, charCallback);
    }

    @Override
    public void setCharModCallback(final CharModCallback callback) {
        if (charModsCallback != null) {
            charModsCallback.release();
        }
        charModsCallback = GLFWCharModsCallback
                .create((w, codepoint, mods) -> callback.charModInput(w, codepoint, mods));
        glfwSetCharModsCallback(context, charModsCallback);
    }

    @Override
    public void setCursorPositionCallback(final CursorPositionCallback callback) {
        if (cursorPosCallback != null) {
            cursorPosCallback.release();
        }
        cursorPosCallback = GLFWCursorPosCallback.create((window, posx, posy) -> {
            this.mouseX = posx;
            this.mouseY = posy;
            callback.changedCursorPosition(window, posx, posy);
        });
        glfwSetCursorPosCallback(context, cursorPosCallback);
    }

    @Override
    public void setMouseButtonCallback(final MouseButtonCallback callback) {
        if (mouseButtonCallback != null) {
            mouseButtonCallback.release();
        }
        mouseButtonCallback = GLFWMouseButtonCallback
                .create((window, button, action, mods) -> callback.pressedMouseButton(window, button, action, mods));
        glfwSetMouseButtonCallback(context, mouseButtonCallback);
    }

    @Override
    public double getCursorX() {
        return mouseX;
    }

    @Override
    public double getCursorY() {
        return mouseY;
    }

}
