package vine.graphics;

import java.util.ArrayList;
import java.util.List;

import vine.game.Game;
import vine.gameplay.component.Sprite;
import vine.gameplay.entity.GameEntity;
import vine.gameplay.scene.Scene;
import vine.math.Matrix4f;
import vine.math.Vector3f;
import vine.platform.lwjgl3.GLShader;
import vine.platform.lwjgl3.GLTexture;
import vine.platform.lwjgl3.GLVertexArray;

/**
 * @author Steffen
 *
 */
public class Renderer {
    /**
     * 
     */
    public static final Texture DEFAULT_TEXTURE = new GLTexture(
            "D:\\Workspaces\\VineEngine\\eclipse_workspace\\engine-core\\res\\test\\hero.png");
    /**
     * 
     */
    public static final Texture DEFAULT_CHIPSET = new GLTexture(
            "D:\\Workspaces\\VineEngine\\eclipse_workspace\\engine-core\\res\\test\\chipset.png");
    /**
     * 
     */
    public static final Shader DEFAULT_SHADER = new GLShader();

    /**
     * 
     */
    List<Sprite> tileSprites = new ArrayList<>();
    List<Sprite> charSprites = new ArrayList<>();

    /**
     * @param entity
     *            A entity, that will be rendered throughout next render pass.
     * @return this, so you can use this method statically in lambda
     *         expressions.
     */
    public Object submit(GameEntity entity) {
        for (Sprite sprite : entity.getSprites()) {
            if (sprite.getTexture().equals(DEFAULT_CHIPSET)) {
                tileSprites.add(sprite);
            } else {
                charSprites.add(sprite);
            }
        }
        return this;
    }

    private GLVertexArray vertt = null;
    float[] tc;
    float[] verts;
    int[] indice;
    int[] indices;
    float[] tcs;
    float[] vertices;

    /**
     * @param scene
     *            The scene thats used to render
     * 
     */
    public void flushTiles(Scene scene) {
        DEFAULT_SHADER.setUniformMat4f("pr_matrix", Game.getGame().getScreen().getOrthographicProjection());
        DEFAULT_SHADER.setUniformMat4f("vw_matrix", Matrix4f.translate(
                new Vector3f(-scene.cameras.getActiveCamera().getX(), -scene.cameras.getActiveCamera().getY(), 0)));
        if (vertt == null) {
            int siz = tileSprites.size();
            vertices = new float[12 * siz];
            indices = new int[6 * siz];
            tcs = new float[8 * siz];
            indice = new int[] { 0, 1, 2, 2, 3, 0 };
            tc = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };

            for (int i = 0; i < tileSprites.size(); i++) {
                verts = tileSprites.get(i).getVertices();
                for (int a = 0; a < verts.length / 3; a++) {
                    vertices[i * verts.length + 3 * a + 0] = verts[3 * a] + tileSprites.get(i).getX();
                    vertices[i * verts.length + 3 * a + 1] = verts[3 * a + 1] + tileSprites.get(i).getY();
                    vertices[i * verts.length + 3 * a + 2] = verts[3 * a + 2] + tileSprites.get(i).getZ();
                }
                for (int b = 0; b < indice.length; b++) {
                    indices[i * indice.length + b] = i * 4 + indice[b];
                }
                tc = tileSprites.get(i).getUVCoordinates();
                for (int c = 0; c < tc.length; c++) {
                    tcs[i * tc.length + c] = tc[c];
                }
            }
            vertt = new GLVertexArray(vertices, indices, tcs);
        } else {
            int siz = tileSprites.size();
            tcs = new float[8 * siz];
            tc = tileSprites.get(0).getUVCoordinates();
            for (int i = 0; i < tileSprites.size(); i++) {
                for (int c = 0; c < tc.length; c++) {
                    tcs[i * tc.length + c] = tc[c];
                }
            }
            vertt.changeTexture(tcs);
        }
        DEFAULT_CHIPSET.bind();
        DEFAULT_SHADER.bind();
        vertt.render();
        vertt.unbind();
        DEFAULT_SHADER.unbind();
        DEFAULT_CHIPSET.unbind();
        tileSprites.clear();
    }

    private GLVertexArray vert = null;

    /**
     * @param scene
     *            The scene thats used to render
     * 
     */
    public void flushChars(Scene scene) {
        DEFAULT_SHADER.setUniformMat4f("pr_matrix", Game.getGame().getScreen().getOrthographicProjection());
        DEFAULT_SHADER.setUniformMat4f("vw_matrix", Matrix4f.translate(
                new Vector3f(-scene.cameras.getActiveCamera().getX(), -scene.cameras.getActiveCamera().getY(), 0)));
        if (vert == null) {
            int siz = charSprites.size();
            vertices = new float[12 * siz];
            indices = new int[6 * siz];
            tcs = new float[8 * siz];
            indice = new int[] { 0, 1, 2, 2, 3, 0 };
            for (int i = 0; i < charSprites.size(); i++) {
                verts = charSprites.get(i).getVertices();
                for (int a = 0; a < verts.length / 3; a++) {
                    vertices[i * verts.length + 3 * a + 0] = verts[3 * a] + charSprites.get(i).getX();
                    vertices[i * verts.length + 3 * a + 1] = verts[3 * a + 1] + charSprites.get(i).getY();
                    vertices[i * verts.length + 3 * a + 2] = verts[3 * a + 2] + charSprites.get(i).getZ();
                }
                for (int b = 0; b < indice.length; b++) {
                    indices[i * indice.length + b] = i * 4 + indice[b];
                }
                tc = charSprites.get(i).getUVCoordinates();
                for (int c = 0; c < tc.length; c++) {
                    tcs[i * tc.length + c] = tc[c];
                }
            }
            vert = new GLVertexArray(vertices, indices, tcs);
        } else {
            int siz = charSprites.size();
            vertices = new float[12 * siz];
            tcs = new float[8 * siz];
            tc = charSprites.get(0).getUVCoordinates();
            for (int i = 0; i < charSprites.size(); i++) {
                verts = charSprites.get(i).getVertices();
                for (int a = 0; a < verts.length / 3; a++) {
                    vertices[i * verts.length + 3 * a + 0] = verts[3 * a] + charSprites.get(i).getX();
                    vertices[i * verts.length + 3 * a + 1] = verts[3 * a + 1] + charSprites.get(i).getY();
                    vertices[i * verts.length + 3 * a + 2] = verts[3 * a + 2] + charSprites.get(i).getZ();
                }
                for (int c = 0; c < tc.length; c++) {
                    tcs[i * tc.length + c] = tc[c];
                }
            }
            vert.changeTexture(tcs);
            vert.changeVertices(vertices);
        }
        DEFAULT_TEXTURE.bind();
        DEFAULT_SHADER.bind();
        vert.render();
        vert.unbind();
        DEFAULT_SHADER.unbind();
        DEFAULT_TEXTURE.unbind();
        charSprites.clear();
    }
}
