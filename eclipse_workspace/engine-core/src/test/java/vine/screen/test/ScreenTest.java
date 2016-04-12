package vine.screen.test;

import org.junit.Assert;
import org.junit.Test;

import vine.game.screen.GameScreen;
import vine.game.screen.Screen;
import vine.platform.lwjgl3.GLFWDisplay;
import vine.platform.lwjgl3.GLFWWindow;

public class ScreenTest {

    @Test
    public static void testScreenCreation() {
        final Screen screen = new GameScreen(new GLFWWindow(new GLFWDisplay()), 1000, 750);
        Assert.assertTrue(screen.getViewport().getLeftOffset() == screen.getViewport().getRightOffset());
        Assert.assertTrue(screen.getViewport().getLeftOffset() == 100);
        Assert.assertTrue(screen.getViewport().getTopOffset() == screen.getViewport().getBottomOffset());
        Assert.assertTrue(screen.getViewport().getBottomOffset() == 0);
        Assert.assertTrue(screen.getAspect() == 0.75f);
        Assert.assertTrue(screen.getWidth() == 1000);
        Assert.assertTrue(screen.getHeight() == 750);
        Assert.assertTrue(screen.getUnitsPerPixel() == 1);
        Assert.assertTrue(screen.worldToScreenCoord(5) == 5 / screen.getUnitsPerPixel());
        Assert.assertTrue(screen.getOrthographicProjection() != null);
    }
}
