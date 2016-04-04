package vine.graphics;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.assets.ShaderLoader;
import vine.game.screen.Screen;
import vine.gui.Widget;
import vine.math.Matrix4f;
import vine.math.Vector3f;

public class GUIRenderer {

    /**
     * Used logger for gameplay logs.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(SpriteRenderer.class);
    /**
     * 
     */
    public static final Shader DEFAULT_SHADER = new ShaderLoader().loadSync(null, "res/test/frag.shader", null, null,
            null);

    private final Set<Widget> submittedWidgets = new LinkedHashSet<>();
    private int[] indices;
    private float[] tcs;
    private float[] vertices;
    private float[] colors;
    private VertexBufferObject vert;
    private VertexBufferObject render;
    int q = 0;

    /**
     * @param widgets
     *            A entity, that will be rendered throughout next render pass.
     */
    public void submit(final List<Widget> widgets) {
        q = Math.max(q, submittedWidgets.size());
        submittedWidgets.retainAll(widgets);

        if (submittedWidgets.size() != widgets.size()) {
            submittedWidgets.addAll(widgets);
            if (q < submittedWidgets.size()) {
                vertices = new float[12 * submittedWidgets.size()];
                tcs = new float[8 * submittedWidgets.size()];
                indices = new int[6 * submittedWidgets.size()];
                colors = new float[16 * submittedWidgets.size()];
                int[] indice = new int[] { 0, 1, 2, 2, 3, 0 };
                int i = 0;
                for (Widget widget : submittedWidgets) {
                    for (int b = 0; b < indice.length; b++) {
                        indices[i * indice.length + b] = i * 4 + indice[b];
                    }
                    for (int c = 0; c < 4; c++) {
                        colors[i * 16 + c * 4] = widget.getColor().getX();
                        colors[i * 16 + c * 4 + 1] = widget.getColor().getY();
                        colors[i * 16 + c * 4 + 2] = widget.getColor().getZ();
                        colors[i * 16 + c * 4 + 3] = -widget.getTransparency();
                    }
                    i++;
                }
                System.out.println("Needed to create new buffer for GUI");
                vert = new VertexBufferObject(vertices, indices, tcs, colors);
            }
        }

        int i = 0;
        for (final Widget widget : submittedWidgets) {
            float worldHeight = 200;
            float worldWidth = 200;
            vertices[i * 12 + 0] = widget.getPosition().getX();
            vertices[i * 12 + 1] = widget.getPosition().getY();
            vertices[i * 12 + 2] = 0.9f;
            vertices[i * 12 + 3] = widget.getPosition().getX();
            vertices[i * 12 + 4] = widget.getPosition().getY() + worldHeight;
            vertices[i * 12 + 5] = 0.9f;
            vertices[i * 12 + 6] = widget.getPosition().getX() + worldWidth;
            vertices[i * 12 + 7] = widget.getPosition().getY() + worldHeight;
            vertices[i * 12 + 8] = 0.9f;
            vertices[i * 12 + 9] = widget.getPosition().getX() + worldWidth;
            vertices[i * 12 + 10] = widget.getPosition().getY();
            vertices[i * 12 + 11] = 0.9f;
            i++;
        }
        vert.changeTexture(tcs, 0, tcs.length);
        vert.changeVertices(vertices, 0, vertices.length);
        vert.changeIndices(i);
    }

    Matrix4f cameraTransformation = Matrix4f.identity();

    public final void renderGUI(final List<Widget> widgets, final Screen screen) {
        DEFAULT_SHADER.bind();
        submit(widgets);
        cameraTransformation.elements[0 + 3 * 4] = -screen.getWidth() / 2f;
        cameraTransformation.elements[1 + 3 * 4] = screen.getHeight() / 2f - 200;
        DEFAULT_SHADER.setUniformMat4f("vw_matrix", cameraTransformation);
        DEFAULT_SHADER.setUniformMat4f("pr_matrix", screen.getOrthographicProjection());
        drawWidgets();
        DEFAULT_SHADER.unbind();
    }

    /**
     * @param scene
     *            The scene thats used to render
     * 
     */
    private void drawWidgets() {
        vert.render();
        vert.unbind();
    }
}
