package vine.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import vine.display.Display;
import vine.graphics.Graphics;
import vine.input.Input;
import vine.window.Window;

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
    public static Window getPlatformWindow() {
        try {
            return (Window) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLFWWindow")
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
            return null;
        }
    }

    /**
     * @return The platform dependent implementation of display.
     */
    public static Display getDisplay() {
        try {
            return (Display) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLFWDisplay")
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
            return null;
        }
    }

    /**
     * @return The platform dependent implementation of graphics.
     */
    public static Graphics getGraphics() {
        try {
            return (Graphics) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLGraphics")
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
            return null;
        }
    }

    /**
     * @return The platform dependent implementation of input.
     */
    public static Input getInput() {
        try {
            return (Input) ClassLoader.getSystemClassLoader().loadClass("vine.platform.lwjgl3.GLFWInput").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
            return null;
        }
    }
}