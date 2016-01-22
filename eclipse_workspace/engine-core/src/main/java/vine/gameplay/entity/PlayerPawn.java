package vine.gameplay.entity;

import org.lwjgl.glfw.GLFW;

import vine.event.KeyEvent;
import vine.event.MouseButtonEvent;
import vine.math.Vector2f;

public class PlayerPawn extends GameEntity {

    @Override
    public strictfp void update(float delta) {
        this.position.add(velocity.getX() * delta, velocity.getY() * delta, 0);
    }

    @Override
    public boolean onKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            if (keyEvent.getKey() == GLFW.GLFW_KEY_A) {
                velocity.setX(velocity.getX() < 64 ? velocity.getX() + 64 : 64);
            }
            if (keyEvent.getKey() == GLFW.GLFW_KEY_D) {
                velocity.setX(velocity.getX() > -64 ? velocity.getX() - 64 : -64);
            }
            if (keyEvent.getKey() == GLFW.GLFW_KEY_S) {
                velocity.setY(velocity.getY() < 64 ? velocity.getY() + 64 : 64);
            }
            if (keyEvent.getKey() == GLFW.GLFW_KEY_W) {
                velocity.setY(velocity.getY() > -64 ? velocity.getY() - 64 : -64);
            }
        } else if (keyEvent.getAction() == 1) {
            if (keyEvent.getKey() == GLFW.GLFW_KEY_A) {
                velocity.setX(velocity.getX() > 64 ? 64 : velocity.getX() - 64);
            }
            if (keyEvent.getKey() == GLFW.GLFW_KEY_D) {
                velocity.setX(velocity.getX() < -64 ? -64 : velocity.getX() + 64);
            }
            if (keyEvent.getKey() == GLFW.GLFW_KEY_S) {
                velocity.setY(velocity.getY() < -64 ? -64 : velocity.getY() - 64);
            }
            if (keyEvent.getKey() == GLFW.GLFW_KEY_W) {
                velocity.setY(velocity.getX() > 64 ? 64 : velocity.getY() + 64);
            }
        }
        return true;
    }

    public void construct(int x, int y) {
        this.position.add(0, 0, 0.5f);
    }
}