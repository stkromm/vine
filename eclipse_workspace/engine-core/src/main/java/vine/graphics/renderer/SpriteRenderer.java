package vine.graphics.renderer;

import java.util.LinkedHashSet;
import java.util.Set;

import vine.assets.AssetManager;
import vine.game.scene.GameEntity;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.graphics.Image;
import vine.graphics.Sprite;
import vine.graphics.Texture;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;
import vine.graphics.shader.Shader;
import vine.graphics.shader.ShaderUniforms;
import vine.math.Matrix4f;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public class SpriteRenderer
{
    public static final Shader          DEFAULT_SHADER       = AssetManager.loadSync("frag", Shader.class);
    public static final Image           DEFAULT_TEXTURE      = AssetManager.loadSync("hero", Image.class);
    private final Set<GameEntity>       opaqueSprites        = new LinkedHashSet<>();
    // private final Set<GameEntity> translucentSprites = new TreeSet<>(
    // (o1, o2) -> o1.getZPosition() >= o2.getZPosition() ? -1 : 1);
    private final Matrix4f              cameraTransformation = Matrix4f.identity();
    private final float[]               tempVertices         = new float[12];
    private final float[]               tempColors           = new float[4];
    private final VertexAttributeBuffer vertexPositions      = new VertexAttributeBuffer(this.tempVertices,
            VertexAttribute.POSITION);
    private final VertexAttributeBuffer vertexTextureCoords  = new VertexAttributeBuffer(new float[8],
            VertexAttribute.TEXTURE);
    private final VertexAttributeBuffer vertexColors         = new VertexAttributeBuffer(this.tempColors,
            VertexAttribute.COLOR);
    private VertexBufferObject          opaqueSpriteVBO      = new VertexBufferObject(0, this.vertexPositions,
            this.vertexTextureCoords, this.vertexColors);
    Texture                             currentTexture       = SpriteRenderer.DEFAULT_TEXTURE;

    public void submit(
            final Sprite sprite,
            final float x,
            final float y,
            final float width,
            final float height,
            final float z,
            final int color)
    {
        if (this.currentTexture == null || !this.currentTexture.equals(sprite.getTexture()))
        {
            if (this.vertexPositions.getPosition() != 0)
            {
                this.flush();
            }
            this.currentTexture = sprite.getTexture();
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
        this.vertexTextureCoords.append(sprite.getUVCoordinates());
    }

    /**
     * @param scene
     *            The scene, that is rendered
     * @param screen
     *            The screen, that is used to contain the rendered image
     */
    public final void render(final Scene scene)
    {
        final Screen screen = scene.getWorld().getScreen();
        SpriteRenderer.DEFAULT_SHADER.bind();
        final Vector3f vector = scene.cameras.getActiveCamera().getTranslation();
        this.cameraTransformation.setTranslation(-vector.getX(), -vector.getY(), -vector.getZ());
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, this.cameraTransformation);
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        if (scene.calculateVisibleEntities())
        {
            this.opaqueSprites.retainAll(scene.getVisibleEntities());
            for (final GameEntity entity : scene.getVisibleEntities())
            {
                this.opaqueSprites.add(entity);
            }

            if (this.opaqueSpriteVBO.getCount() / 6 != this.opaqueSprites.size())
            {
                this.vertexColors.resize(4 * this.opaqueSprites.size());
                this.vertexPositions.resize(12 * this.opaqueSprites.size());
                this.vertexTextureCoords.resize(8 * this.opaqueSprites.size());
                this.opaqueSpriteVBO = new VertexBufferObject(this.opaqueSprites.size() * 6, this.vertexPositions,
                        this.vertexTextureCoords, this.vertexColors);
            }
        }
        this.opaqueSpriteVBO.bind();
        for (final GameEntity entity : this.opaqueSprites)
        {
            this.submit(entity.getSprite(), entity.getXPosition(), entity.getYPosition(),
                    entity.getSprite().getSize().getX(), entity.getSprite().getSize().getY(), entity.getZPosition(),
                    entity.getColor().getColor());
        }
        this.flush();

        SpriteRenderer.DEFAULT_SHADER.unbind();
        this.currentTexture = null;
    }

    private void flush()
    {
        final int i = this.vertexTextureCoords.getPosition() / 8;
        this.vertexTextureCoords.reallocate();
        this.vertexPositions.reallocate();
        this.vertexColors.reallocate();
        this.currentTexture.bind();
        this.opaqueSpriteVBO.draw(i);
    }
}
