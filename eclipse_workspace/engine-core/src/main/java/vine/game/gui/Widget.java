package vine.game.gui;

import vine.game.GameObject;
import vine.graphics.Texture;
import vine.math.vector.Vec2f;

public class Widget extends GameObject
{
    protected GameUserInterface gui;

    public void addToScreen(final GameUserInterface gui)
    {
        this.gui = gui;
    }

    public float[] getPosition()
    {
        return new float[0];
    }

    public float[] getColor()
    {
        return new float[0];
    }

    public float[] getTextureCoords()
    {
        return new float[0];
    }

    public Texture getTexture()
    {
        return null;
    }

    public float getTransparency()
    {
        return 0;
    }

    public Vec2f getSize()
    {
        return new Vec2f(0, 0);
    }

    public Vec2f getScale()
    {
        return new Vec2f(0, 0);
    }

    public int getCount()
    {
        return 0;
    }

    public boolean isSelected()
    {
        return false;
    }

    @Override
    public void onUpdate(final float delta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void begin()
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void construct()
    {
        // TODO Auto-generated method stub

    }

}