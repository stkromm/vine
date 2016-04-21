package vine.platform.lwjgl3;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCharModsCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import vine.input.Input;
import vine.input.InputAction;
import vine.input.InputMapper;

/**
 * @author Steffen
 *
 */
public final class GLFWInput implements Input
{
    private double                  mouseX;
    private double                  mouseY;
    private long                    context;

    private GLFWKeyCallback         keyCallback;
    private GLFWCursorPosCallback   cursorPosCallback;

    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback      scrollCallback;
    private GLFWCharCallback        charCallback;
    private GLFWCharModsCallback    charModsCallback;

    /**
     * 
     */
    public GLFWInput()
    {
        this.cursorPosCallback = GLFWCursorPosCallback.create((w, posx, posy) ->
        {
            this.mouseX = posx;
            this.mouseY = posy;
        });
    }

    @Override
    public void poll()
    {
        GLFW.glfwPollEvents();
    }

    @Override
    public void setKeyCallback(final KeyCallback callback)
    {
        if (this.keyCallback != null)
        {
            this.keyCallback.free();
        }

        this.keyCallback = GLFWKeyCallback.create((win, key, scancode, action, mods) ->
        {
            if (InputMapper.getNumberOfKeys() > key && key >= 0)
            {
                InputMapper.setKeyPressed(key, InputAction.getTypeByAction(action));
            }
            callback.keyPressed(win, key, scancode, InputAction.getTypeByAction(action), mods);
        });
        GLFW.glfwSetKeyCallback(this.context, this.keyCallback);
    }

    @Override
    public void listenToWindow(final long context)
    {
        this.context = context;
        if (this.scrollCallback != null)
        {
            GLFW.glfwSetScrollCallback(context, this.scrollCallback);
        }
        if (this.keyCallback != null)
        {
            GLFW.glfwSetKeyCallback(context, this.keyCallback);
        }
    }

    @Override
    public void setScrollCallback(final ScrollCallback callback)
    {
        if (this.scrollCallback != null)
        {
            this.scrollCallback.free();
        }
        this.scrollCallback = GLFWScrollCallback
                .create((w, offsetx, offsety) -> callback.scrolled(w, offsetx, offsety));
        GLFW.glfwSetScrollCallback(this.context, this.scrollCallback);
    }

    @Override
    public void setCharCallback(final CharCallback callback)
    {
        if (this.charCallback != null)
        {
            this.charCallback.free();
        }
        this.charCallback = GLFWCharCallback.create((w, codepoint) -> callback.charInput(w, codepoint));
        GLFW.glfwSetCharCallback(this.context, this.charCallback);
    }

    @Override
    public void setCharModCallback(final CharModCallback callback)
    {
        if (this.charModsCallback != null)
        {
            this.charModsCallback.free();
        }
        this.charModsCallback = GLFWCharModsCallback
                .create((w, codepoint, mods) -> callback.charModInput(w, codepoint, mods));
        GLFW.glfwSetCharModsCallback(this.context, this.charModsCallback);
    }

    @Override
    public void setCursorPositionCallback(final CursorPositionCallback callback)
    {
        if (this.cursorPosCallback != null)
        {
            this.cursorPosCallback.free();
        }
        this.cursorPosCallback = GLFWCursorPosCallback.create((window, posx, posy) ->
        {
            this.mouseX = posx;
            this.mouseY = posy;
            callback.changedCursorPosition(window, posx, posy);
        });
        GLFW.glfwSetCursorPosCallback(this.context, this.cursorPosCallback);
    }

    @Override
    public void setMouseButtonCallback(final MouseButtonCallback callback)
    {
        if (this.mouseButtonCallback != null)
        {
            this.mouseButtonCallback.free();
        }
        this.mouseButtonCallback = GLFWMouseButtonCallback.create((window, button, action, mods) -> callback
                .pressedMouseButton(window, button, InputAction.getTypeByAction(action), mods));
        GLFW.glfwSetMouseButtonCallback(this.context, this.mouseButtonCallback);
    }

    @Override
    public double getCursorX()
    {
        return this.mouseX;
    }

    @Override
    public double getCursorY()
    {
        return this.mouseY;
    }

}
