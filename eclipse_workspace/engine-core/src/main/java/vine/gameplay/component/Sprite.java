package vine.gameplay.component;

import vine.game.Game;

/**
 * @author Steffen
 *
 */
public class Sprite extends Component { // NOSONAR

    private float tileWidth = 32;
    private float tileHeight = 64;
    private float[] vertices = new float[] { 0, 0, 0, 0, tileHeight, 0, tileWidth, tileHeight, 0, tileWidth, 0, 0, };

    public float[] getVertices() {
        return vertices;
    }
}