package vine.gameplay.component;

import org.lwjgl.opengl.GL11;

import vine.game.Game;
import vine.graphics.Graphics;

/**
 * @author Steffen
 *
 */
public class Camera extends Component {

    @Override
    public void onDestroy() {
        this.entity.getScene().cameras.removeCamera(this);
    }
}