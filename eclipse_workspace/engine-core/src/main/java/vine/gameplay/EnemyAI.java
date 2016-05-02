package vine.gameplay;

import java.lang.ref.WeakReference;

import vine.game.World;
import vine.game.scene.Component;
import vine.math.vector.Vec2f;
import vine.physics.PhysicsComponent;

public class EnemyAI extends Component
{
    WeakReference<PlayerPawn> player;
    PhysicsComponent          movement;

    @Override
    public void onAttach()
    {
        this.player = new WeakReference<>((PlayerPawn) World.getObjectByName("player"));
        this.movement = this.entity.getComponent(PhysicsComponent.class);
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
            this.movement.setAcceleration(
                    playerPosition.getX() - this.entity.getXPosition(),
                    playerPosition.getY() - this.entity.getYPosition());
            // }
        }
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
}
