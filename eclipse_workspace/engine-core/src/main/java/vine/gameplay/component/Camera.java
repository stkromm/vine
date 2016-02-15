package vine.gameplay.component;

import vine.game.Game;

/**
 * @author Steffen
 *
 */
public class Camera extends Component {

    @Override
    public void onDestroy() {
        Game.getGame().getScene().cameras.removeCamera(this);
    }
}
