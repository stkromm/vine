package vine.window.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import vine.platform.lwjgl3.GLFWDisplay;
import vine.platform.lwjgl3.GLFWWindow;
import vine.window.Window;

public class WindowTest
{
    Window                         window;
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp()
    {
        this.window = new GLFWWindow(new GLFWDisplay());
    }

    @Test
    public void testWindowTitle()
    {
        this.window.setTitle("test");
        Assert.assertTrue(this.window.getTitle().equals("test"));
    }

    @Test
    public void testWindowSize()
    {
        this.window.setWindowSize(500, 500);
        Assert.assertTrue(this.window.getHeight() == 500);
        Assert.assertTrue(this.window.getWidth() == 500);
    }

    @Test
    public void testVisibility()
    {
        this.window.show();
        Assert.assertTrue(this.window.isVisible());
        this.window.hide();
        Assert.assertTrue(!this.window.isVisible());
        this.window.show();
        Assert.assertTrue(this.window.isVisible());
    }
}
