package vine.graphics.renderer;

import vine.assets.AssetManager;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.graphics.Image;
import vine.graphics.Texture;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;
import vine.graphics.shader.Shader;
import vine.graphics.shader.ShaderUniforms;
import vine.math.matrix.Mat4f;
import vine.math.vector.Vec3f;

/**
 * @author Steffen
 *
 */
public class SpriteBatch
{
    public static final Shader          DEFAULT_SHADER       = AssetManager.loadSync("frag", Shader.class);
    public static final Image           DEFAULT_TEXTURE      = AssetManager.loadSync("hero", Image.class);

    private final Mat4f                 cameraTransformation = Mat4f.identity();

    private final float[]               tempVertices         = new float[12];
    private final float[]               tempColors           = new float[4];

    private final VertexAttributeBuffer vertexPositions      = new VertexAttributeBuffer(this.tempVertices,
            VertexAttribute.POSITION);
    private final VertexAttributeBuffer vertexTextureCoords  = new VertexAttributeBuffer(new float[8],
            VertexAttribute.TEXTURE);
    private final VertexAttributeBuffer vertexColors         = new VertexAttributeBuffer(this.tempColors,
            VertexAttribute.COLOR);
    private VertexBufferObject          vbo                  = new VertexBufferObject(0, this.vertexPositions,
            this.vertexTextureCoords, this.vertexColors);
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
        if (!texture.equals(this.currentTexture))
        {
            flush();
            this.currentTexture = texture;
        }
        this.position += 2;
        if (this.vbo.getCount() < this.position)
        {
            ensureCapacity();
        }
        this.tempVertices[0] = x;
        this.tempVertices[1] = y;
        this.tempVertices[2] = z;
        this.tempVertices[3] = x;
        this.tempVertices[4] = y + height;
        this.tempVertices[5] = z + 0.1f;
        this.tempVertices[6] = x + width;
        this.tempVertices[7] = y + height;
        this.tempVertices[8] = z + 0.1f;
        this.tempVertices[9] = x + width;
        this.tempVertices[10] = y;
        this.tempVertices[11] = z;
        this.tempColors[0] = this.tempColors[1] = this.tempColors[2] = this.tempColors[3] = color;
        this.vertexColors.append(this.tempColors);
        this.vertexPositions.append(this.tempVertices);
        this.vertexTextureCoords.append(uvs);
    }

    public void ensureCapacity()
    {
        final int entitiesCount = this.position;
        this.vertexColors.resize(4 * entitiesCount);
        this.vertexPositions.resize(12 * entitiesCount);
        this.vertexTextureCoords.resize(8 * entitiesCount);
        this.vbo = new VertexBufferObject(entitiesCount, this.vertexPositions, this.vertexTextureCoords,
                this.vertexColors);
        this.vbo.bind();
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
        this.cameraTransformation.setTranslation(-vector.getX(), -vector.getY(), -vector.getZ());
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, this.cameraTransformation);
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        this.vbo.bind();
    }

    public void finish()
    {
        flush();
    }

    private void flush()
    {
        if (this.position == 0)
        {
            return;
        }
        final int i = this.vertexPositions.getPosition() / 12;
        this.vertexTextureCoords.reallocate();
        this.vertexPositions.reallocate();
        this.vertexColors.reallocate();
        this.currentTexture.bind();
        this.vbo.draw(i);
        this.position = 0;
    }
}
