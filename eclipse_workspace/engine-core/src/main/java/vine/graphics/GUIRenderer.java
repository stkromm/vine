package vine.graphics;

import java.util.List;

import vine.assets.AssetManager;
import vine.game.gui.Widget;
import vine.game.screen.Screen;
import vine.math.Matrix4f;

public class GUIRenderer {
    /**
     * 
     */
    public static final Shader DEFAULT_SHADER = AssetManager.loadSync("frag", Shader.class);

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

    BindableTexture currentTexture = null;

    /**
     * @param entity
     *            A entity, that will be rendered
     */
    public void submit(final Widget widget, Screen screen) {
        if (this.currentTexture == null || !this.currentTexture.equals(widget.getTexture())) {
            if (this.vertexPositions.getPosition() != 0) {
                this.flush();
            }
            this.currentTexture = widget.getTexture();
        }
        this.vertexColors.append(widget.getColor());
        this.vertexPositions.append(widget.getPosition());
        this.vertexTextureCoords.append(widget.getTextureCoords());
    }

    int size = 0;

    /**
     * @param scene
     *            The scene, that is rendered
     * @param screen
     *            The screen, that is used to contain the rendered image
     */
    public final void renderGUI(final List<Widget> widgets, final Screen screen) {
        GUIRenderer.DEFAULT_SHADER.bind();
        this.cameraTransformation.elements[0 + 3 * 4] = -screen.getWidth() / 2f;
        this.cameraTransformation.elements[1 + 3 * 4] = -screen.getHeight() / 2f;
        GUIRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, this.cameraTransformation);
        GUIRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX,
                screen.getOrthographicProjection());
        int sum = 1;
        for (final Widget widget : widgets) {
            sum += widget.getCount();
        }
        if (this.size != sum) {
            this.size = sum;
            final int newSize = this.size;
            this.tempIndices = new int[newSize * 6];
            for (int i = newSize - 1; i >= 0; i--) {
                this.tempIndices[i * 6 + 0] = i * 4 + 0;
                this.tempIndices[i * 6 + 1] = i * 4 + 1;
                this.tempIndices[i * 6 + 2] = i * 4 + 2;
                this.tempIndices[i * 6 + 3] = i * 4 + 2;
                this.tempIndices[i * 6 + 4] = i * 4 + 3;
                this.tempIndices[i * 6 + 5] = i * 4 + 0;
            }
            this.vertexPositions.resize(12 * newSize);
            this.vertexTextureCoords.resize(8 * newSize);
            this.vertexColors.resize(16 * newSize);
            this.opaqueSpriteVBO = new VertexBufferObject(this.size, this.vertexPositions, this.vertexTextureCoords,
                    this.vertexColors);
        }
        this.opaqueSpriteVBO.bind();
        for (final Widget entity : widgets) {
            this.submit(entity, screen);
        }
        this.flush();
        this.opaqueSpriteVBO.unbind();

        GUIRenderer.DEFAULT_SHADER.unbind();
    }

    Font font = AssetManager.loadSync("font", Font.class);

    /**
     * @param scene
     *            The scene thats used to render
     */
    private void flush() {
        this.vertexTextureCoords.reallocate();
        this.vertexPositions.reallocate();
        this.vertexColors.reallocate();
        this.font.bind();
        this.opaqueSpriteVBO.draw(this.size * 6);
        this.currentTexture.unbind();
    }
}
