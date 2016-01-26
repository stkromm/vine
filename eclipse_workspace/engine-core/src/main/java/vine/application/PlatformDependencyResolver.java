package vine.application;

import vine.display.Display;
import vine.graphics.Graphics;
import vine.input.Input;
import vine.window.Window;
import vine.window.WindowCreationException;

/**
 * @author Steffen
 *
 */
public class PlatformDependencyResolver {

    private PlatformDependencyResolver() {

    }

    /**
     * @return The platform dependent implementation of window.
     */
    public static Window getPlatformWindow(Display display) {
        try {
            Window window = (Window) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLFWWindow")
                    .newInstance();
            window.init(display);
            return window;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | WindowCreationException e) {
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
            System.exit(0);
        }
        return null;
    }

    /**
     * @param window
     * @return The platform dependent implementation of input.
     */
    public static Input getInput(Window window) {
        try {
            Input input = (Input) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLFWInput")
                    .newInstance();
            input.listenToWindow(window.getContext());
            return input;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.exit(0);
        }
        return null;
    }
}
