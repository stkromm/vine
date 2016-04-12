package vine.graphics;

import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.math.Matrix4f;
import vine.math.Vector3f;
import vine.tilemap.TileMapRenderData;
import vine.tilemap.UniformTileMap;

public class TileMapRenderer {
    private UniformTileMap tileMap;
    private TileMapRenderData tileMapRender;
    private final Matrix4f cameraTransformation = Matrix4f.identity();

    private VertexBufferObject render;
    int q = 0;

    public final void submit(final UniformTileMap map, Screen screen) {
        this.tileMap = map;
        this.tileMapRender = new TileMapRenderData(map, screen);
    }

    public final void renderScene(final Scene scene, final Screen screen) {
        SpriteRenderer.DEFAULT_SHADER.bind();
        final Vector3f vector = scene.cameras.getActiveCamera().getTranslation();
        this.cameraTransformation.elements[0 + 3 * 4] = -screen.getWidth() / 2 - vector.getX() % 32;
        this.cameraTransformation.elements[1 + 3 * 4] = -screen.getHeight() / 2 - vector.getY() % 32;
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f("pr_matrix", screen.getOrthographicProjection());
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f("vw_matrix", this.cameraTransformation);
        this.drawMap(scene);
        SpriteRenderer.DEFAULT_SHADER.unbind();
    }

    private final void drawMap(final Scene scene) {
        this.tileMap.getTexture().bind();
        this.render = this.tileMapRender.getRenderData((int) scene.cameras.getActiveCamera().getEntity().getXCoord(),
                (int) scene.cameras.getActiveCamera().getEntity().getYCoord());
        this.render.bind();
        this.render.draw();
        this.render.unbind();
        this.tileMap.getTexture().unbind();
    }
}
