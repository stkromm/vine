package vine.screen.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.game.screen.GameScreen;
import vine.game.screen.Screen;
import vine.platform.lwjgl3.GLFWDisplay;
import vine.platform.lwjgl3.GLFWWindow;
import vine.window.Window;
import vine.window.WindowCreationException;

public class ScreenTest {

    @Test
    public void testScreenCreation() throws WindowCreationException {
        Window window = new GLFWWindow(new GLFWDisplay());
        window.setWindowSize(1200, 750);
        Screen screen = new GameScreen(window, 1000, 750);
        assertTrue(screen.getViewport().getLeftOffset() == screen.getViewport().getRightOffset());
        assertTrue(screen.getViewport().getLeftOffset() == 100);
        assertTrue(screen.getViewport().getTopOffset() == screen.getViewport().getBottomOffset());
        assertTrue(screen.getViewport().getBottomOffset() == 0);
        assertTrue(screen.getAspect() == 0.75f);
        assertTrue(screen.getWidth() == 1000);
        assertTrue(screen.getHeight() == 750);
        assertTrue(screen.getUnitsPerPixel() == 1);
        assertTrue(screen.worldToScreenCoord(5) == 5 / screen.getUnitsPerPixel());
        assertTrue(screen.getOrthographicProjection() != null);
    }
}