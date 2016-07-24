package vine.window.test;

import org.junit.Test;

import vine.graphics.Color;

public class ColorTest
{

    @Test
    public void testColor()
    {
        final Color c = new Color(128, 0, 0, 0);
        System.out.println("" + c);
        System.out.println("" + Color.convertRgbToHsv(c.getColor()));
    }
}
