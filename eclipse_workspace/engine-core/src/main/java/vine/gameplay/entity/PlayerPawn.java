package vine.gameplay.entity;

import org.lwjgl.glfw.GLFW;

import vine.event.KeyEvent;
import vine.input.InputAction;

/**
 * @author Steffen
 *
 */
public class PlayerPawn extends GameEntity {

    @Override
    public strictfp void update(final float delta) {
        this.position.add(velocity.getX() * delta, velocity.getY() * delta, 0);
    }

    private void onMoveButtonReleased(final int button) {
        switch (button) {
        case GLFW.GLFW_KEY_A:
            velocity.setX(velocity.getX() < 64 ? velocity.getX() + 64 : 64);
            break;
        case GLFW.GLFW_KEY_D:
            velocity.setX(velocity.getX() > -64 ? velocity.getX() - 64 : -64);
            break;
        case GLFW.GLFW_KEY_S:
            velocity.setY(velocity.getY() < 64 ? velocity.getY() + 64 : 64);
            break;
        case GLFW.GLFW_KEY_W:
            velocity.setY(velocity.getY() > -64 ? velocity.getY() - 64 : -64);
            break;
        default:
            break;
        }
    }

    private void onMoveButtonPressed(final int button) {
        switch (button) {
        case GLFW.GLFW_KEY_A:
            velocity.setX(velocity.getX() > 64 ? 64 : velocity.getX() - 64);
            break;
        case GLFW.GLFW_KEY_D:
            velocity.setX(velocity.getX() < -64 ? -64 : velocity.getX() + 64);
            break;
        case GLFW.GLFW_KEY_S:
            velocity.setY(velocity.getY() < -64 ? -64 : velocity.getY() - 64);
            break;
        case GLFW.GLFW_KEY_W:
            velocity.setY(velocity.getX() > 64 ? 64 : velocity.getY() + 64);
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

    @Override
    public void construct(final int x, final int y) {
        this.position.add(0, 0, 0.5f);
    }
}
