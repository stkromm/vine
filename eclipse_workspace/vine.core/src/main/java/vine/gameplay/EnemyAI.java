package vine.gameplay;

import java.lang.ref.WeakReference;

import vine.game.World;
import vine.game.scene.Component;
import vine.math.vector.VectorUtils;
import vine.math.vector.Vec2f;
import vine.physics.RigidBody;

public class EnemyAI extends Component
{
    WeakReference<PlayerPawn> player;
    RigidBody                 movement;

    @Override
    public void onAttach()
    {
        player = new WeakReference<>((PlayerPawn) World.getObjectByName("player"));
        movement = entity.getComponent(RigidBody.class);
    }

    @Override
    public void onUpdate(final float delta)
    {
        if (player.get() != null)
        {
            final Vec2f playerPosition = player.get().getPosition();
            if (VectorUtils.squaredLength(
                    playerPosition.getX() - entity.getXPosition(),
                    playerPosition.getY() - entity.getYPosition()) > 150 * 150)
            {
                movement.addForce(
                        (playerPosition.getX() - entity.getXPosition()) / 3,
                        (playerPosition.getY() - entity.getYPosition()) / 3);
            }
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
