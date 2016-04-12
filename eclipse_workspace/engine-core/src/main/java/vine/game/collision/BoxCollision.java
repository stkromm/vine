package vine.game.collision;

import vine.math.Vector2f;

public class BoxCollision {
    public static final boolean collide(Vector2f positionA, Vector2f extendsA, Vector2f positionB, Vector2f extendsB) {
        return positionA.getX() <= positionB.getX() + extendsB.getX() && //
                positionB.getX() <= positionA.getX() + extendsA.getX() && //
                positionA.getY() <= positionB.getY() + extendsB.getY() && //
                positionB.getY() <= positionA.getY() + extendsA.getY();
    }
}
