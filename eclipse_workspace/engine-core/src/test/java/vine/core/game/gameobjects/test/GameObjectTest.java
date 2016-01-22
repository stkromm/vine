package vine.core.game.gameobjects.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.game.Game;
import vine.game.GameObject;
import vine.gameplay.entity.GameEntity;

/**
 * @author Steffen
 *
 */
public class GameObjectTest {

    /**
     * 
     */
    @Test
    public void testEquals() {
        GameObject object = Game.instantiate(MyTestObject.class);
        assertTrue(!object.equals(null));
        assertTrue(object.equals(object));
        assertTrue(!object.equals(Game.instantiate(MyTestObject.class)));
        assertTrue(!object.equals(""));
        object.destroy();
    }

    /**
     * 
     */
    @Test
    public void testHideFlag() {
        GameObject object = Game.instantiate(MyTestObject.class);
        assertTrue(!object.hideFlag());
        object.enableFlags(GameObject.HIDE_FLAG);
        assertTrue(object.hideFlag());
        object.destroy();
    }

    /**
     * 
     */
    @Test
    public void testHashCode() {
        GameObject object = Game.instantiate(MyTestObject.class);
        assertTrue(object.hashCode() == object.getName().hashCode());
        object.destroy();
    }

    /**
     * 
     */
    @Test
    public void testActiveFlag() {
        GameObject object = Game.instantiate(MyTestObject.class);
        object.update(0);
        object.disableFlags(GameObject.ACTIVE_FLAG);
        object.update(0);
        object.destroy();
    }

    /**
     * 
     */
    @Test
    public void testObjectInstantiation() {
        Game.instantiate(MyTestObject.class, "test");
        GameObject object = Game.instantiate(MyTestObject.class);
        MyTestObject test = (MyTestObject) Game.getObjectByName("test");
        assertTrue(test.getName().equals("test"));
        Game.instantiate(GameEntity.class);
        test = (MyTestObject) Game.getObjectByName("test?");
        test = Game.instantiate(MyTestObject.class, "test?");
        assertTrue(test == null);
        assertTrue(Game.getObjectsByType(MyTestObject.class).size() == 2);
        Game.instantiate(MyTestObject.class);
        assertTrue(Game.getObjectsByType(MyTestObject.class).size() == 3);
        object.update(0);
        assertTrue(Game.getObjectsByType(MyTestObject.class).size() == 3);
        object.destroy();
        assertTrue(Game.getObjectsByType(MyTestObject.class).size() == 2);

        object = Game.instantiate(GameObject.class);
        assertTrue(object == null);
        object = Game.instantiate(MyWrongTestObject.class);
        assertTrue(object == null);
        object = Game.instantiate(MyTestObject.class, "test");
        assertTrue(object == null);
    }

}