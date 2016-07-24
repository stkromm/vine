package vine.gameplay;

import org.lwjgl.glfw.GLFW;

import vine.animation.AnimationStateManager;
import vine.device.input.InputAction;
import vine.event.Event.EventType;
import vine.event.KeyEvent;
import vine.game.primitive.BoxPrimitive;
import vine.game.scene.GameEntity;
import vine.game.scene.MultiTraceResult;
import vine.graphics.RgbaTexture;
import vine.io.assets.AssetManager;
import vine.physics.RigidBody;
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
    RigidBody                      movement;
    private final MultiTraceResult tmpTraceResult = new MultiTraceResult();

    @Override
    public void onUpdate(final float delta)
    {
        setAnimationState();
        super.onUpdate(delta * 1.5f);
    }

    @Override
    public void begin()
    {
        final AnimatedSprite sprite = this.getComponent(AnimatedSprite.class);
        animation = sprite.getAnimationManager();
        getScene().getListener().addEventHandler(EventType.KEY, event -> onKeyEvent((KeyEvent) event));
        movement = this.getComponent(RigidBody.class);
    }

    private void onMoveButtonReleased(final int button)
    {
        switch (button) {
        case GLFW.GLFW_KEY_W:
            movement.addImpuls(0, -32);
        break;
        case GLFW.GLFW_KEY_A:
            movement.addImpuls(32, 0);
        break;
        case GLFW.GLFW_KEY_D:
            movement.addImpuls(-32, 0);
        break;
        case GLFW.GLFW_KEY_S:
            movement.addImpuls(0, 32);
        break;
        case GLFW.GLFW_KEY_F:
            getScene().getWorld().getScreen().getWindow().getClipboard().setContent("TEST");
            if (getScene().getTracer()
                    .multiRayTrace(this, getPosition().getX(), getPosition().getY(), 1, 0, 500, tmpTraceResult))
            {
                for (final GameEntity entity : tmpTraceResult.getEntities())
                {
                    entity.destroy();
                }
            }
        break;
        case GLFW.GLFW_KEY_G:
            final StaticSprite sprite1 = new StaticSprite(AssetManager.loadSync("hero", RgbaTexture.class), 0, 0, 16,
                    32, 32, 64);
            attachComponent(sprite1);
            final BoxPrimitive box = new BoxPrimitive();
            attachComponent(box);
        break;
        default:
        break;
        }

    }

    private void setAnimationState()
    {

        if (movement.getVelocity().getX() == 0 && movement.getVelocity().getY() == 0)
        {
            switch (animation.getCurrentState().getName()) {
            case "down":
                animation.changeState("idle-down");
            break;
            case "up":
                animation.changeState("idle-up");
            break;
            case "left":
                animation.changeState("idle-left");
            break;
            case "right":
                animation.changeState("idle-right");
            break;
            default:
            }

        } else if (movement.getVelocity().getX() > 0)
        {
            animation.changeState("right");
        } else if (movement.getVelocity().getX() < 0)
        {
            animation.changeState("left");
        } else if (movement.getVelocity().getY() < 0)
        {
            animation.changeState("down");
        } else if (movement.getVelocity().getY() > 0)
        {
            animation.changeState("up");
        }
    }

    private void onMoveButtonPressed(final int button)
    {
        switch (button) {
        case GLFW.GLFW_KEY_W:
            movement.addImpuls(0, 32);
        break;
        case GLFW.GLFW_KEY_A:
            movement.addImpuls(-32, 0);
        break;
        case GLFW.GLFW_KEY_D:
            movement.addImpuls(32, 0);
        break;
        case GLFW.GLFW_KEY_S:
            movement.addImpuls(0, -32);
        break;
        default:
        break;
        }
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
        player.setClip(AssetManager.loadSync("music", SoundClip.class));
        // this.player.playLooped();
    }
}
