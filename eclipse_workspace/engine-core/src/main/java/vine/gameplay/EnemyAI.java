package vine.gameplay;

import java.lang.ref.WeakReference;

import vine.game.World;
import vine.game.scene.Component;
import vine.graphics.renderer.SpriteBatch;
import vine.math.Vec2f;

public class EnemyAI extends Component
{
    WeakReference<PlayerPawn> player;

    @Override
    public void onAttach()
    {
        this.player = new WeakReference<>((PlayerPawn) World.getObjectByName("player"));
    }

    @Override
    public void onUpdate(final float delta)
    {
        if (this.player.get() != null)
        {
            final Vec2f playerPosition = this.player.get().getPosition();
            // if (Vector2f.length(playerPosition.getX() -
            // this.entity.getXPosition(),
            // playerPosition.getY() - this.entity.getYPosition()) > 200)
            // {
            this.entity.setAcceleration(
                    playerPosition.getX() - this.entity.getXPosition(),
                    playerPosition.getY() - this.entity.getYPosition());
            // }
        }
    }

    @Override
    public void onUpdatePhysics(float delta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDetach()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeactivation()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivation()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRender(SpriteBatch batcher)
    {
        // TODO Auto-generated method stub

    }
}
