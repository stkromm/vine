package vine.gameplay;

import org.lwjgl.glfw.GLFW;

import vine.animation.AnimationStateManager;
import vine.assets.AssetManager;
import vine.event.Event.EventType;
import vine.event.KeyEvent;
import vine.game.scene.GameEntity;
import vine.game.scene.MultiTraceResult;
import vine.graphics.Image;
import vine.input.InputAction;
import vine.physics.CollisionBox;
import vine.physics.PhysicsComponent;
import vine.sound.AudioPlayer;
import vine.sound.SoundClip;

/**
 * @author Steffen
 *
 */
public class PlayerPawn extends GameEntity
{
    AnimationStateManager          animation;
    AudioPlayer                    player         = new AudioPlayer();
    PhysicsComponent               movement;
    private final MultiTraceResult tmpTraceResult = new MultiTraceResult();

    @Override
    public void onUpdate(final float delta)
    {
        super.onUpdate(delta * 1.5f);
    }

    @Override
    public void begin()
    {
        final AnimatedSprite sprite = this.getComponent(AnimatedSprite.class);
        this.animation = sprite.getAnimationManager();
        getScene().getListener().addEventHandler(EventType.KEY, event -> onKeyEvent((KeyEvent) event));
        this.movement = this.getComponent(PhysicsComponent.class);
    }

    private void onMoveButtonReleased(final int button)
    {
        this.movement.setAcceleration(0, 0);
        switch (button) {
        case GLFW.GLFW_KEY_W:
            this.movement.addSpeed(0, -64);
        break;
        case GLFW.GLFW_KEY_A:
            this.movement.addSpeed(64, 0);
        break;
        case GLFW.GLFW_KEY_D:
            this.movement.addSpeed(-64, 0);
        break;
        case GLFW.GLFW_KEY_S:
            this.movement.addSpeed(0, 64);
        break;
        case GLFW.GLFW_KEY_F:
            if (getScene().getTracer()
                    .multiRayTrace(this, getPosition().getX(), getPosition().getY(), 1, 0, 500, this.tmpTraceResult))
            {
                for (final GameEntity entity : this.tmpTraceResult.getEntities())
                {
                    entity.destroy();
                }
            }
        break;
        case GLFW.GLFW_KEY_G:
            final StaticSprite sprite1 = new StaticSprite(AssetManager.loadSync("hero", Image.class), 0, 0, 16, 32, 32,
                    64);
            attachComponent(sprite1);
            final float x = (float) Math.random() * 200;
            final float y = (float) Math.random() * 200;
            sprite1.addWorldOffset(x, y);
            final CollisionBox box = new CollisionBox();
            attachComponent(box);
            box.addWorldOffset(x, y);
        break;
        default:
        break;
        }
        setAnimationState();
    }

    private void setAnimationState()
    {
        if (this.movement.getSpeedX() == 0 && this.movement.getSpeedY() == 0)
        {
            switch (this.animation.getCurrentState().getName()) {
            case "down":
                this.animation.changeState("idle-down");
            break;
            case "up":
                this.animation.changeState("idle-up");
            break;
            case "left":
                this.animation.changeState("idle-left");
            break;
            case "right":
                this.animation.changeState("idle-right");
            break;
            default:
            }

        } else if (this.movement.getSpeedX() > 0)
        {
            this.animation.changeState("right");
        } else if (this.movement.getSpeedX() < 0)
        {
            this.animation.changeState("left");
        } else if (this.movement.getSpeedY() < 0)
        {
            this.animation.changeState("down");
        } else if (this.movement.getSpeedY() > 0)
        {
            this.animation.changeState("up");
        }
    }

    private void onMoveButtonPressed(final int button)
    {
        switch (button) {
        case GLFW.GLFW_KEY_W:
            this.movement.addSpeed(0, 64);
        break;
        case GLFW.GLFW_KEY_A:
            this.movement.addSpeed(-64, 0);
        break;
        case GLFW.GLFW_KEY_D:
            this.movement.addSpeed(64, 0);
        break;
        case GLFW.GLFW_KEY_S:
            this.movement.addSpeed(0, -64);
        break;
        default:
        break;
        }
        setAnimationState();
    }

    public boolean onKeyEvent(final KeyEvent keyEvent)
    {
        if (keyEvent.getAction() == InputAction.RELEASED)
        {
            onMoveButtonReleased(keyEvent.getKey());
        } else if (keyEvent.getAction() == InputAction.PRESS)
        {
            onMoveButtonPressed(keyEvent.getKey());
        }
        return true;
    }

    @Override
    public void construct()
    {
        this.player.setClip(AssetManager.loadSync("music", SoundClip.class));
        // this.player.playLooped();
    }
}
