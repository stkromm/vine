package vine.graphics;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.assets.ShaderLoader;
import vine.assets.TextureLoader;
import vine.game.Game;
import vine.gameplay.scene.GameEntity;
import vine.gameplay.scene.Scene;
import vine.math.Matrix4f;
import vine.tilemap.TileMap;
import vine.tilemap.TileMapRenderData;

/**
 * @author Steffen
 *
 */
public class SceneRenderer {
    /**
     * Used logger for gameplay logs.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(SceneRenderer.class);
    /**
     * 
     */
    public static final Texture2D DEFAULT_TEXTURE = new TextureLoader().loadSync(null, "res/test/hero.png", null, null);
    /**
     * 
     */
    public static final Texture2D DEFAULT_CHIPSET = new TextureLoader().loadSync(null, "res/test/chipset.png", null,
            null);
    /**
     * 
     */
    public static final Shader DEFAULT_SHADER = new ShaderLoader().loadSync(null, "res/test/frag.shader", null, null);

    /**
     * 
     */
    private TileMap tileMap;
    private TileMapRenderData tileMapRender;

    private final Set<GameEntity> charSprites = new HashSet<>();

    private final Matrix4f cameraTransformation = Matrix4f.identity();
    private int[] indices;
    private float[] tcs;
    private float[] vertices;

    private VertexBufferObject vert;
    private VertexBufferObject render;

    /**
     * @param entities
     *            A entity, that will be rendered throughout next render pass.
     * @return this, so you can use this method statically in lambda
     *         expressions.
     */
    public void submit(final Set<GameEntity> entities) {
        charSprites.retainAll(entities);
        if (charSprites.size() != entities.size()) {
            charSprites.addAll(entities);
        }
        if (vert == null || charSprites.size() > indices.length / 6) {
            vertices = new float[12 * charSprites.size()];
            tcs = new float[8 * charSprites.size()];
            indices = new int[6 * charSprites.size()];
            int[] indice = new int[] { 0, 1, 2, 2, 3, 0 };
            for (int i = charSprites.size() - 1; i >= 0; i--) {
                for (int b = 0; b < indice.length; b++) {
                    indices[i * indice.length + b] = i * 4 + indice[b];
                }
            }
            vert = new VertexBufferObject(vertices, indices, tcs, Game.getGame().getGraphics());
        }

        int i = 0;
        for (final GameEntity entity : charSprites) {
            int worldHeight = 64;// entity.getBoundingBox().getHeight();
            int worldWidth = 32;// entity.getBoundingBox().getWidth();
            vertices[i * 12 + 0] = entity.getX();
            vertices[i * 12 + 1] = entity.getY();
            vertices[i * 12 + 2] = entity.getZ();
            vertices[i * 12 + 3] = entity.getX();
            vertices[i * 12 + 4] = entity.getY() + worldHeight;
            vertices[i * 12 + 5] = entity.getZ();
            vertices[i * 12 + 6] = entity.getX() + worldWidth;
            vertices[i * 12 + 7] = entity.getY() + worldHeight;
            vertices[i * 12 + 8] = entity.getZ();
            vertices[i * 12 + 9] = entity.getX() + worldWidth;
            vertices[i * 12 + 10] = entity.getY();
            vertices[i * 12 + 11] = entity.getZ();
            if (entity.getSprite() != null) {
                System.arraycopy(entity.getSprite().getUVCoordinates(), 0, tcs, i * 8, 8);
            }
            i++;
        }
        vert.changeTexture(tcs);
        vert.changeVertices(vertices);
        vert.changeIndices(i);
    }

    public void submit(final TileMap map) {
        DEFAULT_SHADER.bind();
        tileMap = map;
        tileMapRender = new TileMapRenderData(map);
    }

    public final void renderScene(final Scene scene) {
        submit(scene.getVisibleEntities());
        cameraTransformation.elements[0 + 3 * 4] = -scene.cameras.getActiveCamera().getEntity().getX();
        cameraTransformation.elements[1 + 3 * 4] = -scene.cameras.getActiveCamera().getEntity().getY();
        drawMap(Game.getGame().getScreen().getOrthographicProjection(), cameraTransformation);
        drawEntities(Game.getGame().getScreen().getOrthographicProjection(), cameraTransformation);
    }

    private final void drawMap(final Matrix4f projectionMatrix, final Matrix4f viewMatrix) {
        DEFAULT_SHADER.setUniformMat4f("pr_matrix", projectionMatrix);
        DEFAULT_SHADER.setUniformMat4f("vw_matrix", viewMatrix);
        tileMap.getTexture().bind();
        render = tileMapRender.getRenderData(
                (int) Game.getGame().getScene().cameras.getActiveCamera().getEntity().getX(),
                (int) Game.getGame().getScene().cameras.getActiveCamera().getEntity().getY());
        render.render();
        render.unbind();
        tileMap.getTexture().unbind();
    }

    /**
     * @param scene
     *            The scene thats used to render
     * 
     */
    private void drawEntities(final Matrix4f projectionMatrix, final Matrix4f viewMatrix) {
        DEFAULT_SHADER.setUniformMat4f("pr_matrix", projectionMatrix);
        DEFAULT_SHADER.setUniformMat4f("vw_matrix", viewMatrix);

        DEFAULT_TEXTURE.bind();
        vert.render();
        vert.unbind();
        DEFAULT_TEXTURE.unbind();
    }

}