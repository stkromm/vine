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
import vine.math.Mat4f;
import vine.math.Vec3f;

public class PrimitiveRenderer
{
    public static final Shader          DEFAULT_SHADER       = AssetManager.loadSync("frag", Shader.class);
    public static final Image           DEFAULT_TEXTURE      = AssetManager.loadSync("hero", Image.class);

    private final Mat4f                 cameraTransformation = Mat4f.identity();

    private final float[]               tempVertices         = new float[6];
    private final float[]               tempColors           = new float[2];

    private final VertexAttributeBuffer vertexPositions      = new VertexAttributeBuffer(this.tempVertices,
            VertexAttribute.POSITION);
    private final VertexAttributeBuffer vertexTextureCoords  = new VertexAttributeBuffer(new float[8],
            VertexAttribute.TEXTURE);
    private final VertexAttributeBuffer vertexColors         = new VertexAttributeBuffer(this.tempColors,
            VertexAttribute.COLOR);
    private VertexBufferObject          vbo                  = new VertexBufferObject(0, this.vertexPositions,
            this.vertexTextureCoords, this.vertexColors);
    public Texture                      currentTexture       = SpriteBatch.DEFAULT_TEXTURE;

    public void drawLine(float startX, float startY, float endX, float endY)
    {
        this.ensureCapacity(2);
        this.tempVertices[0] = startX;
        this.tempVertices[1] = startY;
        this.tempVertices[2] = 1;
        this.tempVertices[3] = endX;
        this.tempVertices[4] = endY;
        this.tempVertices[5] = 1;
        this.tempColors[0] = this.tempColors[1] = 0xFFFFFF;
        this.vertexColors.append(this.tempColors);
        this.vertexPositions.append(this.tempVertices);
    }

    private void ensureCapacity(int newElements)
    {
        if (this.vbo.getCount() <= this.vertexPositions.getPosition() + newElements)
        {
            int i = this.vertexPositions.getPosition() + newElements;
            i *= 2;
            this.vertexColors.resize(i);
            this.vertexPositions.resize(i);
            this.vertexTextureCoords.resize(i);
            this.vbo = new VertexBufferObject(i, this.vertexPositions, this.vertexTextureCoords, this.vertexColors);
        }
    }

    public void drawCircle(float pointX, float pointY, float radius)
    {
        // TODO Implement
    }

    public final void prepare(final Scene scene)
    {
        final Screen screen = scene.getWorld().getScreen();
        SpriteBatch.DEFAULT_SHADER.bind();
        final Vec3f vector = scene.cameras.getActiveCamera().getTranslation();
        this.cameraTransformation.setTranslation(-vector.getX(), -vector.getY(), -vector.getZ());
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, this.cameraTransformation);
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        this.vbo.bind();
    }

    public void finish()
    {
        this.flush();
        SpriteBatch.DEFAULT_SHADER.unbind();
    }

    private void flush()
    {
        final int i = this.vertexTextureCoords.getPosition() / 8;
        this.vertexTextureCoords.reallocate();
        this.vertexPositions.reallocate();
        this.vertexColors.reallocate();
        this.currentTexture.bind();
        this.vbo.draw(i);
    }
}
