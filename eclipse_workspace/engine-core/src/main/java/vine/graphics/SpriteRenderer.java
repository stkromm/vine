package vine.graphics;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.assets.ShaderLoader;
import vine.assets.TextureLoader;
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
    /**
     * 
     */
    public static final Texture2D DEFAULT_TEXTURE = new TextureLoader().loadSync(null, "res/test/hero.png", null, null,
            null);
    /**
     * 
     */
    public static final Shader DEFAULT_SHADER = new ShaderLoader().loadSync(null, "res/test/frag.shader", null, null,
            null);
    private static final Logger LOGGER = LoggerFactory.getLogger(SpriteRenderer.class);

    private final Set<GameEntity> opaqueSprites = new LinkedHashSet<>();
    private final Set<GameEntity> translucentSprites = new TreeSet<>(new Comparator<GameEntity>() {
        @Override
        public int compare(GameEntity o1, GameEntity o2) {
            return o1.getZOrder() >= o2.getZOrder() ? -1 : 1;
        }

    });
    private final Matrix4f cameraTransformation = Matrix4f.identity();
    private final float[] tempVertices = new float[12];
    private final float[] tempColors = new float[4];
    private int[] tempIndices = new int[6];
    private VertexBufferObject opaqueSpriteVBO;
    private final VertexAttributeBuffer vertexPositions = new VertexAttributeBuffer(this.tempVertices,
            VertexAttribute.POSITION);
    private final VertexAttributeBuffer vertexTextureCoords = new VertexAttributeBuffer(new float[8],
            VertexAttribute.TEXTURE);
    private final VertexAttributeBuffer vertexColors = new VertexAttributeBuffer(this.tempColors,
            VertexAttribute.COLOR);

    Texture2D currentTexture = null;

    /**
     * @param entity
     *            A entity, that will be rendered
     */
    public void submit(final GameEntity entity) {
        if (this.currentTexture == null || !this.currentTexture.equals(entity.getSprite().getTexture())) {
            if (this.vertexPositions.getPosition() != 0) {
                flush();
            }
            this.currentTexture = entity.getSprite().getTexture();
        }
        float worldHeight = entity.getSprite().getSize().getY();
        float worldWidth = entity.getSprite().getSize().getX();
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
        this.tempColors[0] = entity.getColor().getX();
        this.tempColors[1] = entity.getColor().getY();
        this.tempColors[2] = entity.getColor().getZ();
        this.tempColors[3] = -entity.getTranslucency();
        this.vertexColors.append(this.tempColors);
        this.vertexColors.append(this.tempColors);
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
        DEFAULT_SHADER.bind();
        scene.calculateVisibleEntities();
        this.translucentSprites.clear();
        this.opaqueSprites.clear();
        for (final GameEntity entity : scene.getVisibleEntities()) {
            if (entity.getTranslucency() > 0f) {
                this.translucentSprites.add(entity);
            } else {
                this.opaqueSprites.add(entity);
            }
        }
        final Vector3f vector = scene.cameras.getActiveCamera().getTranslation();
        this.cameraTransformation.elements[0 + 3 * 4] = -vector.getX();
        this.cameraTransformation.elements[1 + 3 * 4] = -vector.getY();
        DEFAULT_SHADER.setUniformMat4f("vw_matrix", this.cameraTransformation);
        DEFAULT_SHADER.setUniformMat4f("pr_matrix", screen.getOrthographicProjection());
        if (this.tempIndices.length / 6 < this.opaqueSprites.size()) {
            int newSize = this.tempIndices.length / 6;
            while (newSize < this.opaqueSprites.size()) {
                newSize *= 2;
            }
            this.tempIndices = new int[newSize * 6];
            for (int i = this.opaqueSprites.size() - 1; i >= 0; i--) {
                this.tempIndices[i * 6 + 0] = i * 4 + 0;
                this.tempIndices[i * 6 + 1] = i * 4 + 1;
                this.tempIndices[i * 6 + 2] = i * 4 + 2;
                this.tempIndices[i * 6 + 3] = i * 4 + 2;
                this.tempIndices[i * 6 + 4] = i * 4 + 3;
                this.tempIndices[i * 6 + 5] = i * 4 + 0;
            }
            LOGGER.debug("Created new vbo for character sprites");
            this.vertexPositions.resize(12 * newSize);
            this.vertexTextureCoords.resize(8 * newSize);
            this.vertexColors.resize(16 * newSize);
            this.opaqueSpriteVBO = new VertexBufferObject(this.tempIndices, this.vertexPositions,
                    this.vertexTextureCoords, this.vertexColors);
        }
        this.currentTexture = null;
        for (final GameEntity entity : this.opaqueSprites) {
            submit(entity);
        }
        flush();
        DEFAULT_SHADER.unbind();
    }

    /**
     * @param scene
     *            The scene thats used to render
     * 
     */
    private void flush() {
        this.opaqueSpriteVBO.changeIndices(this.vertexPositions.getPosition() / 12);
        this.vertexTextureCoords.reallocate();
        this.vertexPositions.reallocate();
        this.vertexColors.reallocate();
        this.currentTexture.bind();
        this.opaqueSpriteVBO.render();
        this.opaqueSpriteVBO.unbind();
        this.currentTexture.unbind();
    }
}
