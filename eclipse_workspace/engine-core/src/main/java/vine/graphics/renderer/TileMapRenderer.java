package vine.graphics.renderer;

import java.util.Arrays;

import vine.game.World;
import vine.game.screen.Screen;
import vine.game.tilemap.Tile;
import vine.game.tilemap.UniformTileMap;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;
import vine.graphics.shader.ShaderUniforms;
import vine.math.GMath;
import vine.math.matrix.Mat4f;
import vine.math.vector.Vec3f;
import vine.util.GridCalculator;

public class TileMapRenderer implements Renderer
{
    private final Mat4f           cameraTransformation = Mat4f.identity();
    private VertexBufferObject    vertexBuffer;
    private VertexAttributeBuffer verts;
    private VertexAttributeBuffer tcs;
    private UniformTileMap        tileMap;

    private final int             screenxTiles;
    private final int             screenyTiles;

    private final World           world;
    private float[]               zeroUvs;
    private float[]               uvs;
    private float[]               colors;
    private VertexAttributeBuffer cols;
    private final float[]         colorTemp            = new float[4];

    public TileMapRenderer(final Screen screen, final World world)
    {
        this.screenxTiles = screen.getWidth() / 32 + 2;
        this.screenyTiles = screen.getHeight() / 32 + 2;
        //
        this.world = world;
    }

    public void submit(final UniformTileMap map)
    {
        if (this.tileMap != null)
        {
            return;
        }
        this.tileMap = map;
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

    @Override
    public void finish()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void prepare(final Screen screen)
    {
        SpriteBatch.DEFAULT_SHADER.bind();
        final Vec3f vector = this.world.getScene().getCameras().getActiveCamera().getTranslation();
        this.cameraTransformation.setTranslation(//
                -screen.getWidth() / 2 - vector.getX() % 32f, //
                -screen.getHeight() / 2f - vector.getY() % 32f, //
                0);
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, this.cameraTransformation);
        //
        final int cameraX = (int) vector.getX() / 32 - this.screenxTiles / 2 + 1;
        final int cameraY = (int) vector.getY() / 32 - this.screenyTiles / 2 + 1;
        System.arraycopy(this.zeroUvs, 0, this.uvs, 0, this.uvs.length);
        final int minX = GMath.clampPositive(-cameraX);
        final int minY = GMath.clampPositive(-cameraY);
        final int width = this.screenxTiles
                - GMath.clampPositive(cameraX + this.screenxTiles - this.tileMap.getWidth());
        final int height = this.screenyTiles
                - GMath.clampPositive(cameraY + this.screenyTiles - this.tileMap.getHeight());

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
        SpriteBatch.DEFAULT_SHADER.unbind();
    }

    @Override
    public void render()
    {
        //
    }
}
