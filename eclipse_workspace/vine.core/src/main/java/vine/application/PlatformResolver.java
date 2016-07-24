package vine.application;

import java.lang.reflect.InvocationTargetException;

import vine.device.display.Display;
import vine.device.input.Input;
import vine.graphics.Graphics;
import vine.util.Log;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public final class PlatformResolver
{
    private PlatformResolver()
    {

    }

    /**
     * @param classPath
     * @param display
     *            The display that is used to contain the window
     * @return The platform dependent implementation of window.
     */
    public static Window getWindow(final String className, final Display display)
    {
        try
        {
            final Class<?> windowClass = ClassLoader.getSystemClassLoader().loadClass(className);
            for (final Class<?> c : windowClass.getInterfaces())
            {
                if (c.equals(Window.class))
                {
                    return (Window) windowClass.getConstructor(Display.class).newInstance(display);
                }
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            Log.exception("Could not load platform dependent window class", e);
        }
        return null;
    }

    /**
     * @return The platform dependent implementation of display.
     */
    public static Display getDisplay(final String className)
    {
        try
        {
            return (Display) ClassLoader.getSystemClassLoader().loadClass(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            Log.exception("Could not load platform dependent display class", e);
        }
        return null;
    }

    /**
     * @return The platform dependent implementation of graphics.
     */
    public static Graphics getGraphics()
    {
        try
        {
            return (Graphics) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLGraphics")
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            Log.exception("Could not load platform dependent graphics class", e);
        }
        return null;
    }

    /**
     * @param window
     *            The window, that is used to generate input events on.
     * @return The platform dependent implementation of input.
     */
    public static Input getInput(final String className, final Window window)
    {
        try
        {
            final Input input = (Input) ClassLoader.getSystemClassLoader().loadClass(className).newInstance();
            input.listenToWindow(window.getContext());
            return input;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            Log.exception("Could not load platform dependent input class", e);
        }
        return null;
    }
}
