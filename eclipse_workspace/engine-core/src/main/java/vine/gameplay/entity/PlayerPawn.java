package vine.gameplay.entity;

import org.lwjgl.glfw.GLFW;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;
import vine.assets.AssetManager;
import vine.event.KeyEvent;
import vine.game.scene.GameEntity;
import vine.gameplay.component.AnimatedSprite;
import vine.graphics.Texture2D;
import vine.input.InputAction;
import vine.sound.AudioPlayer;

/**
 * @author Steffen
 *
 */
public class PlayerPawn extends GameEntity {
    AnimationStateManager animation;
    AudioPlayer player = new AudioPlayer();

    @Override
    public void onUpdate(final float delta) {
        super.onUpdate(delta);
    }

    private void onMoveButtonReleased(final int button) {
        switch (button) {
        case GLFW.GLFW_KEY_W:
            this.velocity.setY(this.velocity.getY() > -64 ? this.velocity.getY() - 64 : -64);
            break;
        case GLFW.GLFW_KEY_A:
            this.velocity.setX(this.velocity.getX() < 64 ? this.velocity.getX() + 64 : 64);
            break;
        case GLFW.GLFW_KEY_D:
            this.velocity.setX(this.velocity.getX() > -64 ? this.velocity.getX() - 64 : -64);
            break;
        case GLFW.GLFW_KEY_S:
            this.velocity.setY(this.velocity.getY() < 64 ? this.velocity.getY() + 64 : 64);
            break;
        default:
            break;
        }
        this.setAnimationState();
    }

    private void setAnimationState() {
        if (this.velocity.getX() == 0 && this.velocity.getY() == 0) {
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

        } else if (this.velocity.getX() > 0) {
            this.animation.changeState("right");
        } else if (this.velocity.getX() < 0) {
            this.animation.changeState("left");
        } else if (this.velocity.getY() < 0) {
            this.animation.changeState("down");
        } else if (this.velocity.getY() > 0) {
            this.animation.changeState("up");
        }
    }

    private void onMoveButtonPressed(final int button) {
        switch (button) {
        case GLFW.GLFW_KEY_A:
            this.velocity.setX(this.velocity.getX() > 64 ? 64 : this.velocity.getX() - 64);
            break;
        case GLFW.GLFW_KEY_D:
            this.velocity.setX(this.velocity.getX() < -64 ? -64 : this.velocity.getX() + 64);
            break;
        case GLFW.GLFW_KEY_S:
            this.velocity.setY(this.velocity.getY() < -64 ? -64 : this.velocity.getY() - 64);
            break;
        case GLFW.GLFW_KEY_W:
            this.velocity.setY(this.velocity.getX() > 64 ? 64 : this.velocity.getY() + 64);
            break;
        case GLFW.GLFW_KEY_F:
            this.wait(5.f);
            break;
        case GLFW.GLFW_KEY_SPACE:
            if (this.currentCollidedEntity != null) {
                this.currentCollidedEntity.destroy();
            }
            break;
        default:
            break;
        }
        this.setAnimationState();
    }

    @Override
    public boolean onKeyEvent(final KeyEvent keyEvent) {
        if (keyEvent.getAction() == InputAction.RELEASED) {
            this.onMoveButtonReleased(keyEvent.getKey());
        } else if (keyEvent.getAction() == InputAction.PRESS) {
            this.onMoveButtonPressed(keyEvent.getKey());
        }
        return true;
    }

    /**
     * @param x
     * @param y
     */
    @Override
    public void construct(final int x, final int y) {
        super.construct(x, y);
        this.velocity.setX(0);
        this.velocity.setY(0);
        this.collisionEnabled = false;
        this.move(32, 32);
        this.collisionEnabled = true;
        final Texture2D tex = AssetManager.loadSync("herosheet", Texture2D.class);
        float[] uv1 = tex.getUVSquad(0, 0, 24, 32);
        float[] uv2 = tex.getUVSquad(24, 0, 24, 32);
        float[] uv3 = tex.getUVSquad(48, 0, 24, 32);
        final AnimationClip moveUpwards = new AnimationClip(new AnimationFrame(uv2, 200), new AnimationFrame(uv1, 400),
                new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        uv1 = tex.getUVSquad(0, 64, 24, 32);
        uv2 = tex.getUVSquad(24, 64, 24, 32);
        uv3 = tex.getUVSquad(48, 64, 24, 32);
        final AnimationClip moveDownwards = new AnimationClip(new AnimationFrame(uv2, 200),
                new AnimationFrame(uv1, 400), new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        uv1 = tex.getUVSquad(0, 32, 24, 32);
        uv2 = tex.getUVSquad(24, 32, 24, 32);
        uv3 = tex.getUVSquad(48, 32, 24, 32);
        final AnimationClip moveRight = new AnimationClip(new AnimationFrame(uv2, 200), new AnimationFrame(uv1, 400),
                new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        uv1 = tex.getUVSquad(0, 96, 24, 32);
        uv2 = tex.getUVSquad(24, 96, 24, 32);
        uv3 = tex.getUVSquad(48, 96, 24, 32);
        final AnimationClip moveLeft = new AnimationClip(new AnimationFrame(uv2, 200), new AnimationFrame(uv1, 400),
                new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        final AnimationClip idleDown = new AnimationClip(new AnimationFrame(tex.getUVSquad(24, 64, 24, 32), 100f));
        final AnimationClip idleUp = new AnimationClip(new AnimationFrame(tex.getUVSquad(24, 0, 24, 32), 100f));
        final AnimationClip idleLeft = new AnimationClip(new AnimationFrame(tex.getUVSquad(24, 96, 24, 32), 100f));
        final AnimationClip idleRight = new AnimationClip(new AnimationFrame(tex.getUVSquad(24, 32, 24, 32), 100f));
        final AnimationStateManager animation = new AnimationStateManager(new AnimationState[] {
                new AnimationState(idleUp, "idle-up", 1), new AnimationState(idleRight, "idle-right", 1),
                new AnimationState(idleDown, "idle-down", 1), new AnimationState(idleLeft, "idle-left", 1),
                new AnimationState(moveUpwards, "up", 1), new AnimationState(moveDownwards, "down", 1),
                new AnimationState(moveRight, "right", 1), new AnimationState(moveLeft, "left", 1) });
        animation.changeState("idle-down");
        this.animation = animation;
        this.addComponent(this.getWorld().instantiate(AnimatedSprite.class, animation, tex, Integer.valueOf(48),
                Integer.valueOf(64)));
    }

}
