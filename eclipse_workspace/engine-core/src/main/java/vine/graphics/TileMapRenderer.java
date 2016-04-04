package vine.graphics;

import vine.assets.TextureLoader;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.math.Matrix4f;
import vine.math.Vector3f;
import vine.tilemap.TileMap;
import vine.tilemap.TileMapRenderData;

public class TileMapRenderer {
    private TileMap tileMap;
    private TileMapRenderData tileMapRender;
    private final Matrix4f cameraTransformation = Matrix4f.identity();

    private VertexBufferObject render;
    int q = 0;

    public final void submit(final TileMap map, Screen screen) {
        tileMap = map;
        tileMapRender = new TileMapRenderData(map, screen);
    }

    public final void renderScene(final Scene scene, final Screen screen) {
        SpriteRenderer.DEFAULT_SHADER.bind();
        scene.calculateVisibleEntities();
        Vector3f vector = scene.cameras.getActiveCamera().getTranslation();
        cameraTransformation.elements[0 + 3 * 4] = -vector.getX();
        cameraTransformation.elements[1 + 3 * 4] = -vector.getY();
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f("pr_matrix", screen.getOrthographicProjection());
        SpriteRenderer.DEFAULT_SHADER.setUniformMat4f("vw_matrix", cameraTransformation);
        drawMap(scene);
        SpriteRenderer.DEFAULT_SHADER.unbind();
    }

    private final void drawMap(final Scene scene) {
        tileMap.getTexture().bind();
        render = tileMapRender.getRenderData((int) scene.cameras.getActiveCamera().getEntity().getXCoord(),
                (int) scene.cameras.getActiveCamera().getEntity().getYCoord());
        render.render();
        render.unbind();
        tileMap.getTexture().unbind();
    }
}
