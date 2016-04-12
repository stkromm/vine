package vine.game.gui;

import vine.game.GameObject;
import vine.graphics.BindableTexture;
import vine.math.Vector2f;

public class Widget extends GameObject {
    protected GameUserInterface gui;

    public void addToScreen(GameUserInterface gui) {
        this.gui = gui;
    }

    public float[] getPosition() {
        return new float[0];
    }

    public float[] getColor() {
        return new float[0];
    }

    public float[] getTextureCoords() {
        return new float[0];
    }

    public BindableTexture getTexture() {
        return null;
    }

    public float getTransparency() {
        return 0;
    }

    public Vector2f getSize() {
        return new Vector2f(0, 0);
    }

    public Vector2f getScale() {
        return new Vector2f(0, 0);
    }

    public int getCount() {
        return 0;
    }

    public boolean isSelected() {
        return false;
    }
}