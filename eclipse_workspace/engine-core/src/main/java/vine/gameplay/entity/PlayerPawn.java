package vine.gameplay.entity;

import org.lwjgl.glfw.GLFW;

import vine.assets.SoundLoader;
import vine.event.KeyEvent;
import vine.game.scene.GameEntity;
import vine.input.InputAction;
import vine.sound.AudioPlayer;

/**
 * @author Steffen
 *
 */
public class PlayerPawn extends GameEntity {

    AudioPlayer player = new AudioPlayer();

    @Override
    public strictfp void update(final float delta) {
        super.update(delta);
    }

    private void onMoveButtonReleased(final int button) {
        switch (button) {
        case GLFW.GLFW_KEY_W:
            velocity.setY(velocity.getY() > -64 ? velocity.getY() - 64 : -64);
            break;
        case GLFW.GLFW_KEY_A:
            velocity.setX(velocity.getX() < 64 ? velocity.getX() + 64 : 64);
            break;
        case GLFW.GLFW_KEY_D:
            velocity.setX(velocity.getX() > -64 ? velocity.getX() - 64 : -64);
            break;
        case GLFW.GLFW_KEY_S:
            velocity.setY(velocity.getY() < 64 ? velocity.getY() + 64 : 64);
            break;
        default:
            break;
        }
    }

    @Override
    public void updatePhysics(final float delta) {
        super.updatePhysics(delta * 2);
    }

    private void onMoveButtonPressed(final int button) {
        switch (button) {
        case GLFW.GLFW_KEY_A:
            velocity.setX(velocity.getX() > 64 ? 64 : velocity.getX() - 64);
            break;
        case GLFW.GLFW_KEY_D:
            velocity.setX(velocity.getX() < -64 ? -64 : velocity.getX() + 64);
            player.resume();
            break;
        case GLFW.GLFW_KEY_S:
            velocity.setY(velocity.getY() < -64 ? -64 : velocity.getY() - 64);
            break;
        case GLFW.GLFW_KEY_W:
            velocity.setY(velocity.getX() > 64 ? 64 : velocity.getY() + 64);
            player.pause();
            break;
        default:
            break;
        }
    }

    @Override
    public boolean onKeyEvent(final KeyEvent keyEvent) {
        if (keyEvent.getAction() == InputAction.RELEASED) {
            onMoveButtonReleased(keyEvent.getKey());
        } else if (keyEvent.getAction() == InputAction.PRESS) {
            onMoveButtonPressed(keyEvent.getKey());
        }
        return true;
    }

    /**
     * @param x
     * @param y
     */
    public void construct(final int x, final int y) {
        super.construct(x, y);
        this.velocity.setX(0);
        this.velocity.setY(0);
        this.position.add(0, 0);
        player.setClip(new SoundLoader().loadSync(null, "E:\\Sounds\\music.wav", null, null, null));
        player.playLooped();
    }

}
