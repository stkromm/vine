package vine.game.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vine.game.scene.GameEntity;

public class GameEntityTest
{

    GameEntity entity;

    @Before
    public void init()
    {
        this.entity = new GameEntity();
    }

    @Test
    public void testVelocityAccess()
    {
        Assert.assertTrue(this.entity.getXSpeed() == 0);
        this.entity.setSpeed(1, 1);
        Assert.assertTrue(this.entity.getXSpeed() == 1);
        Assert.assertTrue(this.entity.getYSpeed() == 1);
        this.entity.setSpeedX(5);
        Assert.assertTrue(this.entity.getXSpeed() == 5);
        Assert.assertTrue(this.entity.getYSpeed() == 1);
        this.entity.setSpeedY(5);
        Assert.assertTrue(this.entity.getXSpeed() == 5);
        Assert.assertTrue(this.entity.getYSpeed() == 5);
    }
}
