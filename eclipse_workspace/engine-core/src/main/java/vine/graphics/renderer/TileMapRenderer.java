package vine.graphics.renderer;

import java.util.Arrays;

import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.game.tilemap.Tile;
import vine.game.tilemap.UniformTileMap;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;
import vine.graphics.shader.ShaderUniforms;
import vine.math.Matrix4f;
import vine.math.Vector3f;
import vine.math.VineMath;
import vine.util.GridCalculator;

public class TileMapRenderer
{
    private final Matrix4f              cameraTransformation = Matrix4f.identity();
    private final VertexBufferObject    vertexBuffer;
    private final VertexAttributeBuffer verts;
    private final VertexAttributeBuffer tcs;
    private UniformTileMap              tileMap;

    private final int                   screenxTiles;
    private final int                   screenyTiles;

    private final float[]               zeroUvs;
    private final float[]               uvs;
    private final float[]               colors;
    private final VertexAttributeBuffer cols;
    private final float[]               colorTemp            = new float[4];

    public TileMapRenderer(final Screen screen)
    {
        this.screenxTiles = screen.getWidth() / 32 + 2;
        this.screenyTiles = screen.getHeight() / 32 + 2;
        //
        this.uvs = new float[8 * this.screenxTiles * this.screenyTiles];
        this.colors = new float[4 * this.screenxTiles * this.screenyTiles];
        this.verts = new VertexAttributeBuffer(
                GridCalculator.calculateVertexGrid(32, 32, this.screenxTiles, this.screenyTiles),
                VertexAttribute.POSITION);
        this.tcs = new VertexAttributeBuffer(this.uvs, VertexAttribute.TEXTURE);
        this.cols = new VertexAttributeBuffer(this.colors, VertexAttribute.COLOR);
        this.vertexBuffer = new VertexBufferObject(this.screenxTiles * this.screenyTiles * 6, this.verts, this.tcs,
                this.cols);
        this.zeroUvs = new float[this.uvs.length];
    }

    public final void submit(final UniformTileMap map, final Screen screen)
    {
        this.tileMap = map;
    }

    public final void renderScene(final Scene scene)
    {
        SpriteRenderer.DEFAULT_SHADER.bind();
        final Screen screen = scene.getWorld().getScreen();
        final Vector3f vector = scene.cameras.getActiveCamera().getTranslation();
        this.cameraTransformation.setTranslation(//
                -screen.getWidth() / 2 - vector.getX() % 32f, //
                -screen.getHeight() / 2f - vector.getY() % 32f, //
                0);
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, this.cameraTransformation);
        //
        final int cameraX = (int) vector.getX() / 32 - this.screenxTiles / 2 + 1;
        final int cameraY = (int) vector.getY() / 32 - this.screenyTiles / 2 + 1;
        System.arraycopy(this.zeroUvs, 0, this.uvs, 0, this.uvs.length);
        final int minX = VineMath.clampPositive(-cameraX);
        final int minY = VineMath.clampPositive(-cameraY);
        final int width = this.screenxTiles
                - VineMath.clampPositive(cameraX + this.screenxTiles - this.tileMap.getWidth());
        final int height = this.screenyTiles
                - VineMath.clampPositive(cameraY + this.screenyTiles - this.tileMap.getHeight());

        for (int i = width - 1; i >= minX; i--)
        {
            for (int j = height - 1; j >= minY; j--)
            {
                final int index = i + j * this.screenxTiles;
                final Tile tile = this.tileMap.getTile(i + cameraX, j + cameraY);
                System.arraycopy(tile.getSprite().getUVCoordinates(), 0, this.uvs, index * 8, 8);
                Arrays.fill(this.colorTemp, tile.getColor());
                System.arraycopy(this.colorTemp, 0, this.colors, index * 4, 4);
            }
        }
        this.tcs.append(this.uvs);
        this.cols.append(this.colors);
        this.tcs.reallocate();
        this.cols.reallocate();
        this.tileMap.getTexture().bind();
        this.vertexBuffer.bind();
        this.vertexBuffer.draw();
        SpriteRenderer.DEFAULT_SHADER.unbind();
    }
}
