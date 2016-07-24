package vine.graphics.renderer;

import vine.math.GMath;
import vine.math.matrix.MutableMat4f;
import vine.math.vector.Vec3f;

import java.util.Arrays;

import vine.game.World;
import vine.game.screen.Screen;
import vine.game.tilemap.Tile;
import vine.game.tilemap.UniformTileMap;
import vine.graphics.DrawPrimitive;
import vine.graphics.ShaderUniforms;
import vine.graphics.VertexAttribute;
import vine.graphics.VertexAttributeBuffer;
import vine.graphics.VertexBufferObject;
import vine.util.GridCalculator;

public class TileMapRenderer implements Renderer
{
    private final MutableMat4f    cameraTransformation = new MutableMat4f();
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
        screenxTiles = screen.getWidth() / 32 + 2;
        screenyTiles = screen.getHeight() / 32 + 2;
        //
        this.world = world;
    }

    public void submit(final UniformTileMap map)
    {
        if (tileMap != null)
        {
            return;
        }
        tileMap = map;
        uvs = new float[8 * screenxTiles * screenyTiles];
        colors = new float[4 * screenxTiles * screenyTiles];
        verts = new VertexAttributeBuffer(GridCalculator.calculateVertexGrid(32, 32, screenxTiles, screenyTiles),
                VertexAttribute.POSITION);
        tcs = new VertexAttributeBuffer(uvs, VertexAttribute.TEXTURE);
        cols = new VertexAttributeBuffer(colors, VertexAttribute.COLOR);
        vertexBuffer = new VertexBufferObject(screenxTiles * screenyTiles * 6, DrawPrimitive.TRIANGLE, verts, tcs,
                cols);
        zeroUvs = new float[uvs.length];
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
        final Vec3f vector = world.getScene().getCameras().getActiveCamera().getTranslation();
        cameraTransformation.getElements()[0] = 1;
        cameraTransformation.getElements()[4 + 1] = 1;
        cameraTransformation.getElements()[8 + 2] = 1;
        cameraTransformation.getElements()[12 + 3] = 1;
        cameraTransformation.setTranslation(//
                -screen.getWidth() / 2f - vector.getX() % 32f, //
                -screen.getHeight() / 2f - vector.getY() % 32f, //
                0);
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.PROJECTION_MATRIX, screen.getProjection());
        SpriteBatch.DEFAULT_SHADER.setUniformMat4f(ShaderUniforms.VIEW_MATRIX, cameraTransformation);
        //
        final int cameraX = (int) vector.getX() / 32 - screenxTiles / 2 + 1;
        final int cameraY = (int) vector.getY() / 32 - screenyTiles / 2 + 1;
        System.arraycopy(zeroUvs, 0, uvs, 0, uvs.length);
        final int minX = GMath.clampPositive(-cameraX);
        final int minY = GMath.clampPositive(-cameraY);
        final int width = screenxTiles - GMath.clampPositive(cameraX + screenxTiles - tileMap.getWidth());
        final int height = screenyTiles - GMath.clampPositive(cameraY + screenyTiles - tileMap.getHeight());

        for (int i = width - 1; i >= minX; i--)
        {
            for (int j = height - 1; j >= minY; j--)
            {
                final int index = i + j * screenxTiles;
                final Tile tile = tileMap.getTile(i + cameraX, j + cameraY);
                System.arraycopy(tile.getSprite().getUVCoordinates(), 0, uvs, index * 8, 8);
                Arrays.fill(colorTemp, tile.getColor());
                System.arraycopy(colorTemp, 0, colors, index * 4, 4);
            }
        }
        tcs.append(uvs);
        cols.append(colors);
        tcs.reallocate();
        cols.reallocate();
        tileMap.getTexture().bind();
        vertexBuffer.bind();
        vertexBuffer.draw();
        SpriteBatch.DEFAULT_SHADER.unbind();
    }

    @Override
    public void render()
    {
        //
    }
}
