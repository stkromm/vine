package vine.graphics.renderer;

import vine.assets.AssetManager;
import vine.game.scene.GameEntity;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.graphics.RgbaImage;
import vine.graphics.Texture;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;
import vine.graphics.shader.Shader;
import vine.math.matrix.Mat4f;

public class CollisionRenderer implements Renderer
{
    public static final Shader          DEFAULT_SHADER       = AssetManager.loadSync("frag", Shader.class);
    public static final RgbaImage           DEFAULT_TEXTURE      = AssetManager.loadSync("hero", RgbaImage.class);

    private final Mat4f                 cameraTransformation = Mat4f.identity();

    private final float[]               tempVertices         = new float[6];
    private final float[]               tempColors           = new float[2];

    private final VertexAttributeBuffer vertexPositions      = new VertexAttributeBuffer(tempVertices,
            VertexAttribute.POSITION);
    private final VertexAttributeBuffer vertexTextureCoords  = new VertexAttributeBuffer(new float[8],
            VertexAttribute.TEXTURE);
    private final VertexAttributeBuffer vertexColors         = new VertexAttributeBuffer(tempColors,
            VertexAttribute.COLOR);
    private final VertexBufferObject    vbo                  = new VertexBufferObject(0, vertexPositions,
            vertexTextureCoords, vertexColors);
    public Texture                      currentTexture       = SpriteBatch.DEFAULT_TEXTURE;
    Scene                               scene;

    @Override
    public void prepare(final Screen screen)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void render()
    {
        for (final GameEntity entity : scene.getEntities())
        {
            // TODO Render geometry
        }
    }

    @Override
    public void finish()
    {
        // TODO Auto-generated method stub

    }

}
