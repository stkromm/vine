package vine.graphics;

import java.util.ArrayList;
import java.util.List;

import vine.game.Game;
import vine.gameplay.component.Sprite;
import vine.gameplay.entity.GameEntity;
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
    public static final Shader DEFAULT_SHADER = new GLShader();
    /**
     * 
     */
    GLVertexArray vert;

    /**
     * 
     */
    List<Sprite> sprites = new ArrayList<>();

    /**
     * @param entity
     *            A entity, that will be rendered throughout next render pass.
     * @return this, so you can use this method statically in lambda
     *         expressions.
     */
    public Object submit(GameEntity entity) {
        sprites.addAll(entity.getSprites());
        return this;
    }

    /**
     * @param scene The scene thats used to render
     * 
     */
    public void flush(Scene scene) {
        DEFAULT_SHADER.setUniformMat4f("pr_matrix", Game.getGame().getScreen().getOrthographicProjection());
        DEFAULT_SHADER.setUniformMat4f("vw_matrix", Matrix4f.translate(
                new Vector3f(-scene.cameras.getActiveCamera().getX(), -scene.cameras.getActiveCamera().getY(), 0)));
        int siz = sprites.size();
        float[] vertices = new float[12 * siz];
        int[] indices = new int[6 * siz];
        float[] tcs = new float[8 * siz];
        int[] indice = new int[] { 0, 1, 2, 2, 3, 0 };
        float[] tc = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };
        float[] verts;
        for (int i = 0; i < sprites.size(); i++) {
            verts = sprites.get(i).getVertices();
            for (int a = 0; a < verts.length / 3; a++) {
                vertices[i * verts.length + 3 * a + 0] = verts[3 * a] + sprites.get(i).getX();
                vertices[i * verts.length + 3 * a + 1] = verts[3 * a + 1] + sprites.get(i).getY();
                vertices[i * verts.length + 3 * a + 2] = verts[3 * a + 2] + sprites.get(i).getZ();
            }
            for (int b = 0; b < indice.length; b++) {
                indices[i * indice.length + b] = i * 4 + indice[b];
            }
            for (int c = 0; c < tc.length; c++) {
                tcs[i * tc.length + c] = tc[c];
            }
        }
        vert = new GLVertexArray(vertices, indices, tcs);
        DEFAULT_TEXTURE.bind();
        DEFAULT_SHADER.bind();
        vert.render();
        vert.unbind();
        DEFAULT_SHADER.unbind();
        DEFAULT_TEXTURE.unbind();
        sprites.clear();
    }
}