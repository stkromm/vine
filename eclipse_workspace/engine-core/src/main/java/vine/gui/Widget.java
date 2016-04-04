package vine.gui;

import vine.game.GameObject;
import vine.math.Vector2f;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public class Widget extends GameObject {
    /**
     * 
     */
    private final Vector2f position = new Vector2f(0, 0);
    /**
     * 
     */
    final Vector2f scale = new Vector2f(0, 0);

    private final Vector3f color = new Vector3f(-1, 0, -1);

    private float transparency = -1;

    boolean selected = false;

    public Vector2f getPosition() {
        return position;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getTransparency() {
        return transparency;
    }
}
