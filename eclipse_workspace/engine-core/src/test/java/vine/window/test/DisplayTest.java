package vine.window.test;

import java.awt.GraphicsEnvironment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vine.display.Display;
import vine.platform.lwjgl3.GLFWDisplay;

public class DisplayTest
{

    Display display;

    @Before
    public void setUp()
    {
        this.display = new GLFWDisplay();
    }

    @Test
    public void testRefreshRate()
    {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Assert.assertTrue(
                env.getDefaultScreenDevice().getDisplayMode().getRefreshRate() == this.display.getRefreshRate());
    }

    @Test
    public void testBitValues()
    {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Assert.assertTrue(
                env.getDefaultScreenDevice().getDisplayMode().getBitDepth() == this.display.getBlueBits()
                        + this.display.getGreenBits() + this.display.getRedBits() + this.display.getRedBits());
    }
}