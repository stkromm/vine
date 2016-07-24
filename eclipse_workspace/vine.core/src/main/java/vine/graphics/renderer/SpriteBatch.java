package vine.graphics.renderer;

import vine.math.matrix.MutableMat4f;
import vine.math.vector.Vec3f;

import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.graphics.DrawPrimitive;
import vine.graphics.RgbaTexture;
import vine.graphics.Shader;
import vine.graphics.ShaderUniforms;
import vine.graphics.Texture;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;
import vine.io.assets.AssetManager;

/**
 * @author Steffen
 *
 */
public class SpriteBatch
{
    public static final Shader          DEFAULT_SHADER       = AssetManager.loadSync("frag", Shader.class);
    public static final RgbaTexture     DEFAULT_TEXTURE      = AssetManager.loadSync("hero", RgbaTexture.class);

    private final MutableMat4f          cameraTransformation = new MutableMat4f();

    private final float[]               tempVertices         = new float[12];
    private final float[]               tempColors           = new float[4];

    private final VertexAttributeBuffer vertexPositions      = new VertexAttributeBuffer(tempVertices,
            VertexAttribute.POSITION);
    private final VertexAttributeBuffer vertexTextureCoords  = new VertexAttributeBuffer(new float[8],
            VertexAttribute.TEXTURE);
    private final VertexAttributeBuffer vertexColors         = new VertexAttributeBuffer(tempColors,
            VertexAttribute.COLOR);
    private VertexBufferObject          vbo                  = new VertexBufferObject(0, DrawPrimitive.TRIANGLE,
            vertexPositions, vertexTextureCoords, vertexColors);
    public Texture                      currentTexture       = null;
    private int                         position             = 0;

    public void submit(
            final Texture texture,
            final float[] uvs,
            final float x,
            final float y,
            final float width,
            final float height,
            final float z,
            final int color)
    {
        if (!texture.equals(currentTexture))
        {
            flush();
            currentTexture = texture;
        }
        position += 2;
        if (vbo.getCount() < position)
        {
            ensureCapacity();
        }
        tempVertices[0] = x;
        tempVertices[1] = y;
        tempVertices[2] = z;
        tempVertices[3] = x;
        tempVertices[4] = y + height;
        tempVertices[5] = z + 0.1f;
        tempVertices[6] = x + width;
        tempVertices[7] = y + height;
        tempVertices[8] = z + 0.1f;
        tempVertices[9] = x + width;
        tempVertices[10] = y;
        tempVertices[11] = z;
        tempColors[0] = tempColors[1] = tempColors[2] = tempColors[3] = color;
        vertexColors.append(tempColors);
        vertexPositions.append(tempVertices);
        vertexTextureCoords.append(uvs);
    }

    public void ensureCapacity()
    {
        final int entitiesCount = position;
        vertexColors.resize(4 * entitiesCount);
        vertexPositions.resize(12 * entitiesCount);
        vertexTextureCoords.resize(8 * entitiesCount);
        vbo = new VertexBufferObject(entitiesCount, DrawPrimitive.TRIANGLE, vertexPositions, vertexTextureCoords,
                vertexColors);
        vbo.bind();
    }

    /**
     * @param scene
     *            The scene, that is rendered
     * @param screen
     *            The screen, that is used to contain the rendered image
     */
    public final void prepare(final Scene scene)
    {
        final Screen screen = scene.getWorld().getScreen();
        SpriteBatch.DEFAULT_SHADER.bind();
        final Vec3f vector = scene.getCameras().getActiveCamera().getTranslation();
        cameraTransformation.getElements()[0] = 1;
        cameraTransformation.getElements()[4 + 1] = 1;
        cameraTransformation.getElements()[8 + 2] = 1;
        cameraTransformation.getElements()[12 + 3] = 1;
        cameraTransformation.setTranslation(-vector.getX(), -vector.getY(), -vector.getZ());
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, cameraTransformation);
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        vbo.bind();
    }

    public void finish()
    {
        flush();
    }

    private void flush()
    {
        if (position == 0)
        {
            return;
        }
        final int i = vertexPositions.getPosition() / 12;
        vertexTextureCoords.reallocate();
        vertexPositions.reallocate();
        vertexColors.reallocate();
        currentTexture.bind();
        vbo.draw(i);
        position = 0;
    }
}
