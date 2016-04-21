package vine.physics;

import vine.math.Vector2f;

public final class BoxCollision
{
    private BoxCollision()
    {

    }

    public static boolean collide(
            final Vector2f positionA,
            final Vector2f extendsA,
            final Vector2f positionB,
            final Vector2f extendsB)
    {
        return positionA.getX() <= positionB.getX() + extendsB.getX() && //
                positionB.getX() <= positionA.getX() + extendsA.getX() && //
                positionA.getY() <= positionB.getY() + extendsB.getY() && //
                positionB.getY() <= positionA.getY() + extendsA.getY();
    }
}
