package vine.application;

import java.lang.reflect.InvocationTargetException;

import vine.display.Display;
import vine.graphics.Graphics;
import vine.input.Input;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public final class PlatformDependencyResolver {

    private PlatformDependencyResolver() {

    }

    /**
     * @param display
     *            The display that is used to contain the window
     * @return The platform dependent implementation of window.
     */
    public static Window getPlatformWindow(final Display display) {
        try {
            return (Window) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLFWWindow")
                    .getConstructor(Display.class).newInstance(display);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            if (Application.LOGGER.isErrorEnabled()) {
                Application.LOGGER.error("Could not load platform dependent window class", e);
            }
            System.exit(0);
        }
        return null;
    }

    /**
     * @return The platform dependent implementation of display.
     */
    public static Display getDisplay() {
        try {
            return (Display) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLFWDisplay")
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            if (Application.LOGGER.isErrorEnabled()) {
                Application.LOGGER.error("Could not load platform dependent display class", e);
            }
            System.exit(0);
        }
        return null;
    }

    /**
     * @return The platform dependent implementation of graphics.
     */
    public static Graphics getGraphics() {
        try {
            return (Graphics) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLGraphics")
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            if (Application.LOGGER.isErrorEnabled()) {
                Application.LOGGER.error("Could not load platform dependent graphics class", e);
            }
            System.exit(0);
        }
        return null;
    }

    /**
     * @param window
     *            The window, that is used to generate input events on.
     * @return The platform dependent implementation of input.
     */
    public static Input getInput(final Window window) {
        try {
            final Input input = (Input) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLFWInput")
                    .newInstance();
            input.listenToWindow(window.getContext());
            return input;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            if (Application.LOGGER.isErrorEnabled()) {
                Application.LOGGER.error("Could not load platform dependent input class", e);
            }
            System.exit(0);
        }
        return null;
    }
}
