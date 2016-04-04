package vine.window.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import vine.platform.lwjgl3.GLFWDisplay;
import vine.platform.lwjgl3.GLFWWindow;
import vine.window.Window;

public class WindowTest {
    Window window = null;

    @Before
    public void setup() {
        window = new GLFWWindow(new GLFWDisplay());
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testWindowTitle() {
        window.setTitle("test");
        assertTrue(window.getTitle().equals("test"));
    }

    @Test
    public void testWindowSize() {
        window.setWindowSize(500, 500);
        assertTrue(window.getHeight() == 500);
        assertTrue(window.getWidth() == 500);
    }

    @Test
    public void testVisibility() {
        window.show();
        assertTrue(window.isVisible());
        window.hide();
        assertTrue(!window.isVisible());
        window.show();
        assertTrue(window.isVisible());
    }
}
