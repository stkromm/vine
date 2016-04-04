package vine.gameplay.entity;

import org.lwjgl.glfw.GLFW;

import vine.application.GamePlayer;
import vine.event.KeyEvent;
import vine.game.Game;
import vine.game.scene.GameEntity;
import vine.gameplay.component.AnimatedSprite;
import vine.gameplay.component.StaticSprite;
import vine.graphics.SpriteRenderer;
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
        super.update(delta * 10);
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
        case GLFW.GLFW_KEY_F:
            this.getScene().cameras.getActiveCamera().shake(4.2f, 1f, Game.getObjectsByType(GameEntity.class).size(),
                    true);
            final GameEntity entity = Game.instantiate(GameEntity.class, Integer.valueOf((int) (Math.random() * 1000)),
                    Integer.valueOf((int) (Math.random() * 1000)));
            final StaticSprite sprite = Game.instantiate(StaticSprite.class, SpriteRenderer.DEFAULT_TEXTURE,
                    Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(16), Integer.valueOf(32));
            entity.addComponent(sprite);
            scene.add(entity);
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
        this.collisionEnabled = false;
        this.move(32, 32);
        this.collisionEnabled = true;
        // player.setClip(new SoundLoader().loadSync(null,
        // "E:\\Sounds\\music.wav", null, null, null));
        // player.playLooped();
    }

}
