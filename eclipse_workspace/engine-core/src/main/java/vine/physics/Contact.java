package vine.physics;

import vine.math.vector.MutableVec2f;

public class Contact
{
    MutableVec2f contactPoint;
    MutableVec2f contactNormal;
    float        penetration;
    float        friction;
    float        restituition;
}
