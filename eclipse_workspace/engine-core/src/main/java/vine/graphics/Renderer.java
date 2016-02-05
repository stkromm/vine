package vine.graphics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.assets.ShaderLoader;
import vine.assets.TextureLoader;
import vine.game.Game;
import vine.gameplay.entity.GameEntity;
import vine.math.Matrix4f;
import vine.tilemap.TileMap;
import vine.tilemap.TileMapRenderData;

/**
 * @author Steffen
 *
 */
public class Renderer {
    /**
     * Used logger for gameplay logs.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(Renderer.class);
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

    private float[] tc = new float[8];
    private float[] verts = new float[12];
    private int[] indices = new int[100000];
    private float[] tcs = new float[8010];
    private float[] vertices = new float[12100];
    private VertexBufferObject vert;
    VertexBufferObject render;

    /**
     * @param entities
     *            A entity, that will be rendered throughout next render pass.
     * @return this, so you can use this method statically in lambda
     *         expressions.
     */
    public void submit(final Set<GameEntity> entities) {
        int[] indice = new int[] { 0, 1, 2, 2, 3, 0 };
        if (vert == null) {
            for (int i = 0; i < 2000; i++) {
                for (int b = 0; b < indice.length; b++) {
                    indices[i * indice.length + b] = i * 4 + indice[b];
                }
            }
            vert = new VertexBufferObject(vertices, indices, tcs, Game.getGame().getGraphics());
        }
        charSprites.addAll(entities);
        int siz = 0;
        for (GameEntity entity : charSprites) {
            siz += entity.getSprites().size();
        }

        int i = 0;
        for (GameEntity entity : charSprites) {
            for (Sprite sprite : entity.getSprites()) {
                verts = sprite.getVertices();
                for (int a = 0; a < 4; a++) {
                    vertices[i * 12 + 3 * a + 0] = verts[3 * a] + entity.getX();
                    vertices[i * 12 + 3 * a + 1] = verts[3 * a + 1] + entity.getY();
                    vertices[i * 12 + 3 * a + 2] = verts[3 * a + 2] + entity.getZ();
                }
                System.arraycopy(sprite.getUVCoordinates(), 0, tcs, i * 8, 8);
                for (int b = 0; b < indice.length; b++) {
                    indices[i * indice.length + b] = i * 4 + indice[b];
                }
                i++;
            }

        }
        vert.changeTexture(tcs);
        vert.changeVertices(vertices);
    }

    public void submit(TileMap map) {
        DEFAULT_SHADER.bind();
        tileMap = map;
        tileMapRender = new TileMapRenderData(map);
    }

    public final void drawMap(final Matrix4f projectionMatrix, final Matrix4f viewMatrix) {
        DEFAULT_SHADER.setUniformMat4f("pr_matrix", projectionMatrix);
        DEFAULT_SHADER.setUniformMat4f("vw_matrix", viewMatrix);
        tileMap.getTexture().bind();
        render = tileMapRender.getRenderData();
        render.render();
        render.unbind();
        tileMap.getTexture().unbind();
    }

    /**
     * @param scene
     *            The scene thats used to render
     * 
     */
    public void drawEntities(final Matrix4f projectionMatrix, final Matrix4f viewMatrix) {
        DEFAULT_SHADER.setUniformMat4f("pr_matrix", projectionMatrix);
        DEFAULT_SHADER.setUniformMat4f("vw_matrix", viewMatrix);

        DEFAULT_TEXTURE.bind();
        vert.render();
        vert.unbind();
        DEFAULT_TEXTURE.unbind();
    }

    public void clear() {
        charSprites.clear();
    }
}
