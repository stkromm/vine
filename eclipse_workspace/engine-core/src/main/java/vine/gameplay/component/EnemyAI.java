package vine.gameplay.component;

import vine.game.World;
import vine.game.scene.Component;
import vine.gameplay.entity.PlayerPawn;
import vine.math.Vector2f;

public class EnemyAI extends Component
{
    PlayerPawn player;

    Vector2f   cache = new Vector2f(0, 0);

    @Override
    public void onAttach()
    {
        this.player = (PlayerPawn) World.getObjectByName("player");
    }

    @Override
    public void onUpdate(final float delta)
    {
        if (this.player != null)
        {
            final Vector2f playerPosition = this.player.getPosition();
            this.cache.setX(playerPosition.getX() - this.entity.getXPosition());
            this.cache.setY(playerPosition.getY() - this.entity.getYPosition());
            if (this.cache.length() > 200)
            {
                this.entity.setAcceleration(playerPosition.getX() - this.entity.getXPosition(),
                        playerPosition.getY() - this.entity.getYPosition());
            }
            if (this.player.isDestroyed())
            {
                this.player = null;
            }
        }
    }
}
