package vine.game.gui;

import vine.assets.AssetManager;
import vine.game.screen.Screen;
import vine.graphics.Image;
import vine.graphics.Texture;
import vine.math.Vec2f;
import vine.math.Vec3f;

/**
 * @author Steffen
 *
 */
public class Box extends Widget
{
    private final Vec2f   size         = new Vec2f(0.1f, 0.1f);
    private final Vec2f   position     = new Vec2f(0.5f, 0.5f);

    /**
     * 
     */
    final Vec2f           scale        = new Vec2f(1, 1);

    protected final Vec3f color        = new Vec3f(0, 0, 0);

    private float         transparency;

    boolean               selected;

    float[]               tempVertices = new float[12];

    @Override
    public float[] getPosition()
    {
        final Screen screen = this.gui.getScreen();
        final float worldHeight = this.getSize().getY();
        final float worldWidth = this.getSize().getX();
        this.tempVertices[0] = this.position.getX() * screen.getWidth();
        this.tempVertices[1] = this.position.getY() * screen.getHeight();
        this.tempVertices[2] = 0.9f;
        this.tempVertices[3] = this.position.getX() * screen.getWidth();
        this.tempVertices[4] = (this.position.getY() + worldHeight) * screen.getHeight();
        this.tempVertices[5] = 0.9f;
        this.tempVertices[6] = (this.position.getX() + worldWidth) * screen.getWidth();
        this.tempVertices[7] = (this.position.getY() + worldHeight) * screen.getHeight();
        this.tempVertices[8] = 0.9f;
        this.tempVertices[9] = (this.position.getX() + worldWidth) * screen.getWidth();
        this.tempVertices[10] = this.position.getY() * screen.getHeight();
        this.tempVertices[11] = 0.9f;
        return this.tempVertices;
    }

    float[] tempColors = new float[16];

    @Override
    public float[] getColor()
    {
        this.tempColors[0] = this.color.getX();
        this.tempColors[1] = this.color.getY();
        this.tempColors[2] = this.color.getZ();
        this.tempColors[3] = -this.transparency;
        this.tempColors[4] = this.color.getX();
        this.tempColors[5] = this.color.getY();
        this.tempColors[6] = this.color.getZ();
        this.tempColors[7] = -this.transparency;
        this.tempColors[8] = this.color.getX();
        this.tempColors[9] = this.color.getY();
        this.tempColors[10] = this.color.getZ();
        this.tempColors[11] = -this.transparency;
        this.tempColors[12] = this.color.getX();
        this.tempColors[13] = this.color.getY();
        this.tempColors[14] = this.color.getZ();
        this.tempColors[15] = -this.transparency;
        return this.tempColors;
    }

    float[] uvs = new float[] { 0, 0, 0, 1, 1, 1, 1, 0 };

    @Override
    public float[] getTextureCoords()
    {
        return this.uvs;
    }

    public static final Image DEFAULT_TEXTURE = AssetManager.loadSync("hero", Image.class);

    @Override
    public Texture getTexture()
    {
        return Box.DEFAULT_TEXTURE;
    }

    @Override
    public float getTransparency()
    {
        return this.transparency;
    }

    @Override
    public Vec2f getSize()
    {
        return this.size;
    }

    @Override
    public Vec2f getScale()
    {
        return this.scale;
    }

    @Override
    public int getCount()
    {
        return 1;
    }

}
