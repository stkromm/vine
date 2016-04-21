package vine.graphics.renderer;

import java.util.List;

import vine.assets.AssetManager;
import vine.game.gui.Widget;
import vine.game.screen.Screen;
import vine.graphics.Font;
import vine.graphics.Texture;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;
import vine.graphics.shader.Shader;
import vine.graphics.shader.ShaderUniforms;
import vine.math.Matrix4f;

public class GUIRenderer
{
    /**
     * 
     */
    public static final Shader          DEFAULT_SHADER       = AssetManager.loadSync("frag", Shader.class);

    private final Matrix4f              cameraTransformation = Matrix4f.identity();
    private VertexBufferObject          opaqueSpriteVBO;
    private final VertexAttributeBuffer vertexPositions      = new VertexAttributeBuffer(new float[12],
            VertexAttribute.POSITION);
    private final VertexAttributeBuffer vertexTextureCoords  = new VertexAttributeBuffer(new float[8],
            VertexAttribute.TEXTURE);
    private final VertexAttributeBuffer vertexColors         = new VertexAttributeBuffer(new float[4],
            VertexAttribute.COLOR);
    Font                                font                 = AssetManager.loadSync("font", Font.class);
    Texture                             currentTexture;
    int                                 size;

    /**
     * @param entity
     *            A entity, that will be rendered
     */
    public void submit(final Widget widget)
    {
        if (this.currentTexture == null || !this.currentTexture.equals(widget.getTexture()))
        {
            if (this.vertexPositions.getPosition() != 0)
            {
                this.flush();
            }
            this.currentTexture = widget.getTexture();
        }
        this.vertexColors.append(widget.getColor());
        this.vertexPositions.append(widget.getPosition());
        this.vertexTextureCoords.append(widget.getTextureCoords());
    }

    /**
     * @param scene
     *            The scene, that is rendered
     * @param screen
     *            The screen, that is used to contain the rendered image
     */
    public final void renderGUI(final List<Widget> widgets, final Screen screen)
    {
        GUIRenderer.DEFAULT_SHADER.bind();
        this.cameraTransformation.getElements()[0 + 3 * 4] = -screen.getWidth() / 2f;
        this.cameraTransformation.getElements()[1 + 3 * 4] = -screen.getHeight() / 2f;
        GUIRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, this.cameraTransformation);
        GUIRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        int sum = 1;
        for (final Widget widget : widgets)
        {
            sum += widget.getCount();
        }
        if (this.size < sum)
        {
            this.size = sum;
            final int newSize = this.size;
            this.vertexPositions.resize(12 * newSize);
            this.vertexTextureCoords.resize(8 * newSize);
            this.vertexColors.resize(4 * newSize);
            this.opaqueSpriteVBO = new VertexBufferObject(this.size, this.vertexPositions, this.vertexTextureCoords,
                    this.vertexColors);
        }
        this.opaqueSpriteVBO.bind();
        for (final Widget entity : widgets)
        {
            this.submit(entity);
        }
        this.flush();
        GUIRenderer.DEFAULT_SHADER.unbind();
        this.currentTexture = null;
    }

    /**
     * @param scene
     *            The scene thats used to render
     */
    private void flush()
    {
        final int i = this.vertexPositions.getPosition() / 8;
        this.vertexTextureCoords.reallocate();
        this.vertexPositions.reallocate();
        this.vertexColors.reallocate();
        this.font.bind();
        this.opaqueSpriteVBO.draw(i);
    }
}
