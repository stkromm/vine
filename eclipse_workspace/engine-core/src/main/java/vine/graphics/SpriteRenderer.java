package vine.graphics;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import vine.assets.AssetManager;
import vine.game.scene.GameEntity;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.math.Matrix4f;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public class SpriteRenderer {
    public static final Shader DEFAULT_SHADER = AssetManager.loadSync("frag", Shader.class);

    private final Set<GameEntity> opaqueSprites = new LinkedHashSet<>();
    private final Set<GameEntity> translucentSprites = new TreeSet<>(
            (o1, o2) -> o1.getZOrder() >= o2.getZOrder() ? -1 : 1);
    private final Matrix4f cameraTransformation = Matrix4f.identity();
    private final float[] tempVertices = new float[12];
    private final float[] tempColors = new float[4];
    private final int[] tempIndices = new int[0];
    private final VertexAttributeBuffer vertexPositions = new VertexAttributeBuffer(this.tempVertices,
            VertexAttribute.POSITION);
    private final VertexAttributeBuffer vertexTextureCoords = new VertexAttributeBuffer(new float[8],
            VertexAttribute.TEXTURE);
    private final VertexAttributeBuffer vertexColors = new VertexAttributeBuffer(this.tempColors,
            VertexAttribute.COLOR);
    private VertexBufferObject opaqueSpriteVBO = new VertexBufferObject(0, this.vertexPositions,
            this.vertexTextureCoords, this.vertexColors);
    Texture2D currentTexture = null;

    /**
     * @param entity
     *            A entity, that will be rendered
     */
    public void submit(final GameEntity entity) {
        if (this.currentTexture == null || !this.currentTexture.equals(entity.getSprite().getTexture())) {
            if (this.vertexPositions.getPosition() != 0) {
                this.flush();
            }
            this.currentTexture = entity.getSprite().getTexture();
        }
        final float worldHeight = entity.getSprite().getSize().getY();
        final float worldWidth = entity.getSprite().getSize().getX();
        this.tempVertices[0] = entity.getXCoord();
        this.tempVertices[1] = entity.getYCoord();
        this.tempVertices[2] = entity.getZOrder();
        this.tempVertices[3] = entity.getXCoord();
        this.tempVertices[4] = entity.getYCoord() + worldHeight;
        this.tempVertices[5] = entity.getZOrder() + 0.1f;
        this.tempVertices[6] = entity.getXCoord() + worldWidth;
        this.tempVertices[7] = entity.getYCoord() + worldHeight;
        this.tempVertices[8] = entity.getZOrder() + 0.1f;
        this.tempVertices[9] = entity.getXCoord() + worldWidth;
        this.tempVertices[10] = entity.getYCoord();
        this.tempVertices[11] = entity.getZOrder();
        this.tempColors[0] = entity.getColor().getColor();
        this.tempColors[1] = entity.getColor().getColor();
        this.tempColors[2] = entity.getColor().getColor();
        this.tempColors[3] = entity.getColor().getColor();
        this.vertexColors.append(this.tempColors);
        this.vertexPositions.append(this.tempVertices);
        this.vertexTextureCoords.append(entity.getSprite().getUVCoordinates());
    }

    /**
     * @param scene
     *            The scene, that is rendered
     * @param screen
     *            The screen, that is used to contain the rendered image
     */
    public final void renderScene(final Scene scene, final Screen screen) {
        SpriteRenderer.DEFAULT_SHADER.bind();
        scene.calculateVisibleEntities();
        this.translucentSprites.clear();
        this.opaqueSprites.retainAll(scene.getVisibleEntities());
        for (final GameEntity entity : scene.getVisibleEntities()) {
            if (entity.getColor().getTranslucency() > 0f) {
                this.translucentSprites.add(entity);
            } else {
                this.opaqueSprites.add(entity);
            }
        }
        final Vector3f vector = scene.cameras.getActiveCamera().getTranslation();
        this.cameraTransformation.elements[0 + 3 * 4] = -vector.getX();
        this.cameraTransformation.elements[1 + 3 * 4] = -vector.getY();
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, this.cameraTransformation);
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX,
                screen.getOrthographicProjection());
        if (this.opaqueSpriteVBO.getCount() / 6 != this.opaqueSprites.size()) {
            final int newSize = this.opaqueSprites.size();
            this.vertexPositions.resize(12 * newSize);
            this.vertexTextureCoords.resize(8 * newSize);
            this.vertexColors.resize(16 * newSize);
            this.opaqueSpriteVBO = new VertexBufferObject(newSize * 6, this.vertexPositions, this.vertexTextureCoords,
                    this.vertexColors);
        }
        this.opaqueSpriteVBO.bind();

        for (final GameEntity entity : this.opaqueSprites) {
            this.submit(entity);
        }

        this.flush();
        this.opaqueSpriteVBO.unbind();
        SpriteRenderer.DEFAULT_SHADER.unbind();
        this.currentTexture = null;
    }

    private void flush() {
        final int i = this.vertexTextureCoords.getPosition() / 8;
        this.vertexTextureCoords.reallocate();
        this.vertexPositions.reallocate();
        this.vertexColors.reallocate();
        this.currentTexture.bind();
        this.opaqueSpriteVBO.draw(i * 6);
    }
}
