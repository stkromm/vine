package vine.graphics.renderer;

import vine.math.matrix.Mat4f;
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

public class PrimitiveRenderer
{
    public static final Shader          DEFAULT_SHADER       = AssetManager.loadSync("frag", Shader.class);
    public static final RgbaTexture     DEFAULT_TEXTURE      = AssetManager.loadSync("hero", RgbaTexture.class);

    private final Mat4f                 cameraTransformation = Mat4f.identity();

    private final float[]               tempVertices         = new float[6];
    private final float[]               tempColors           = new float[2];

    private final VertexAttributeBuffer vertexPositions      = new VertexAttributeBuffer(tempVertices,
            VertexAttribute.POSITION);
    private final VertexAttributeBuffer vertexTextureCoords  = new VertexAttributeBuffer(new float[8],
            VertexAttribute.TEXTURE);
    private final VertexAttributeBuffer vertexColors         = new VertexAttributeBuffer(tempColors,
            VertexAttribute.COLOR);
    private VertexBufferObject          vbo                  = new VertexBufferObject(0, DrawPrimitive.TRIANGLE,
            vertexPositions, vertexTextureCoords, vertexColors);
    public Texture                      currentTexture       = SpriteBatch.DEFAULT_TEXTURE;

    public void drawLine(final float startX, final float startY, final float endX, final float endY)
    {
        ensureCapacity(2);
        tempVertices[0] = startX;
        tempVertices[1] = startY;
        tempVertices[2] = 1;
        tempVertices[3] = endX;
        tempVertices[4] = endY;
        tempVertices[5] = 1;
        tempColors[0] = tempColors[1] = 0xFFFFFF;
        vertexColors.append(tempColors);
        vertexPositions.append(tempVertices);
    }

    private void ensureCapacity(final int newElements)
    {
        if (vbo.getCount() <= vertexPositions.getPosition() + newElements)
        {
            int i = vertexPositions.getPosition() + newElements;
            i *= 2;
            vertexColors.resize(i);
            vertexPositions.resize(i);
            vertexTextureCoords.resize(i);
            vbo = new VertexBufferObject(i, DrawPrimitive.TRIANGLE, vertexPositions, vertexTextureCoords, vertexColors);
        }
    }

    public void drawCircle(final float pointX, final float pointY, final float radius)
    {
        // TODO Implement
    }

    public final void prepare(final Scene scene)
    {
        final Screen screen = scene.getWorld().getScreen();
        SpriteBatch.DEFAULT_SHADER.bind();
        final Vec3f vector = scene.getCameras().getActiveCamera().getTranslation();
        cameraTransformation.getElements()[3] = -vector.getX();
        cameraTransformation.getElements()[7] = -vector.getY();
        cameraTransformation.getElements()[11] = -vector.getZ();
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, cameraTransformation);
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        vbo.bind();
    }

    public void finish()
    {
        flush();
        SpriteBatch.DEFAULT_SHADER.unbind();
    }

    private void flush()
    {
        final int i = vertexTextureCoords.getPosition() / 8;
        vertexTextureCoords.reallocate();
        vertexPositions.reallocate();
        vertexColors.reallocate();
        currentTexture.bind();
        vbo.draw(i);
    }
}
