package vine.graphics.renderer;

import java.lang.ref.WeakReference;
import java.util.List;

import vine.game.gui.Widget;
import vine.game.screen.Screen;
import vine.graphics.DrawPrimitive;
import vine.graphics.Font;
import vine.graphics.Shader;
import vine.graphics.ShaderUniforms;
import vine.graphics.Texture;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;
import vine.io.assets.AssetManager;
import vine.math.matrix.Mat4f;

public class GUIRenderer
{
    /**
     * 
     */
    public static final Shader          DEFAULT_SHADER       = AssetManager.loadSync("frag", Shader.class);

    private final Mat4f                 cameraTransformation = Mat4f.identity();
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
        if (currentTexture == null || !currentTexture.equals(widget.getTexture()))
        {
            if (vertexPositions.getPosition() != 0)
            {
                flush();
            }
            currentTexture = widget.getTexture();
        }
        vertexColors.append(widget.getColor());
        vertexPositions.append(widget.getPosition());
        vertexTextureCoords.append(widget.getTextureCoords());
    }

    /**
     * @param scene
     *            The scene, that is rendered
     * @param screen
     *            The screen, that is used to contain the rendered image
     */
    public final void renderGUI(final List<WeakReference<Widget>> widgets, final Screen screen)
    {
        GUIRenderer.DEFAULT_SHADER.bind();
        cameraTransformation.getElements()[0 + 3 * 4] = -screen.getWidth() / 2f;
        cameraTransformation.getElements()[1 + 3 * 4] = -screen.getHeight() / 2f;
        GUIRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, cameraTransformation);
        GUIRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        int sum = 1;
        for (final WeakReference<Widget> widget : widgets)
        {
            sum += widget.get().getCount();
        }
        if (size < sum)
        {
            size = sum;
            final int newSize = size;
            vertexPositions.resize(12 * newSize);
            vertexTextureCoords.resize(8 * newSize);
            vertexColors.resize(4 * newSize);
            opaqueSpriteVBO = new VertexBufferObject(size, DrawPrimitive.TRIANGLE, vertexPositions, vertexTextureCoords,
                    vertexColors);
        }
        opaqueSpriteVBO.bind();
        for (final WeakReference<Widget> entity : widgets)
        {
            submit(entity.get());
        }
        flush();
        GUIRenderer.DEFAULT_SHADER.unbind();
        currentTexture = null;
    }

    /**
     * @param scene
     *            The scene thats used to render
     */
    private void flush()
    {
        final int i = vertexPositions.getPosition() / 8;
        vertexTextureCoords.reallocate();
        vertexPositions.reallocate();
        vertexColors.reallocate();
        font.bind();
        opaqueSpriteVBO.draw(i);
    }
}
