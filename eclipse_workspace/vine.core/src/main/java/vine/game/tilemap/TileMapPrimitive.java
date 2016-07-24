package vine.game.tilemap;

import vine.math.GMath;
import vine.math.HitData;
import vine.math.geometry.shape.MutableAabb;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

import vine.game.Transform;
import vine.game.primitive.BoxPrimitive;
import vine.game.primitive.CirclePrimitive;
import vine.game.primitive.Primitive;
import vine.physics.Contact;

public class TileMapPrimitive implements Primitive
{
    UniformTileMap tileMap;
    Vec2f          extend;
    Transform      transform = new Transform();

    public TileMapPrimitive(final UniformTileMap map, final MutableAabb Aabb)
    {
        tileMap = map;
        extend = new Vec2f(Aabb.getWidth(), Aabb.getHeight());
    }

    @Override
    public Transform getTransform()
    {
        return transform;
    }

    @Override
    public boolean trace(final Primitive primitive)
    {
        final Vec2f origin = primitive.getTransform().getWorldPosition();
        final Vec2f extend = primitive.getExtends();
        final int startX = Math.round(origin.getX()) / tileMap.getTileWidth();
        final int startY = Math.round(origin.getY()) / tileMap.getTileHeight();
        final int width = Math.round(origin.getX() + extend.getX()) / tileMap.getTileWidth() - startX;
        final int height = Math.round(origin.getY() + extend.getY()) / tileMap.getTileHeight() - startY;
        for (int i = startX; i <= startX + width; i++)
        {
            for (int j = startY; j <= startY + height; j++)
            {
                if (tileMap.blocksDynamic(i, j))
                {
                    return true;
                }
            }
        }
        return false;
    }

    MutableVec2f tmp1 = new MutableVec2f();

    @Override
    public void generateContact(final Primitive qq, final Contact contact)
    {
        final Vec2f origin = qq.getTransform().getWorldPosition();
        final Vec2f extend = qq.getExtends();
        final int startX = Math.round(origin.getX()) / tileMap.getTileWidth();
        final int startY = Math.round(origin.getY()) / tileMap.getTileHeight();
        final int width = Math.round(origin.getX() + extend.getX()) / tileMap.getTileWidth() - startX;
        final int height = Math.round(origin.getY() + extend.getY()) / tileMap.getTileHeight() - startY;
        for (int i = startX; i <= startX + width; i++)
        {
            for (int j = startY; j <= startY + height; j++)
            {
                if (tileMap.blocksDynamic(i, j))
                {
                    tmp1.set(i * tileMap.getTileWidth(), j * tileMap.getTileHeight());
                    tmp1.uniformScale(-1);
                    tmp1.add(qq.getTransform().getWorldPosition());
                    final float xOverlap = qq.getExtends().getX() / 2 + tileMap.getTileWidth() / 2
                            - GMath.abs(tmp1.getX());
                    final float yOverlap = qq.getExtends().getY() / 2 + tileMap.getTileHeight() / 2
                            - GMath.abs(tmp1.getY());
                    if (yOverlap < xOverlap)
                    {
                        if (tmp1.getY() < 0)
                        {
                            contact.setContactNormal(0, 1);
                        }
                        {
                            contact.setContactNormal(0, 1);
                        }
                        contact.setPenetration(tmp1.getY() < 0 ? yOverlap : -yOverlap);

                    } else
                    {
                        if (tmp1.getX() < 0)
                        {
                            contact.setContactNormal(1, 0);
                        } else
                        {
                            contact.setContactNormal(-1, 0);
                        }
                        contact.setPenetration(-xOverlap);
                    }
                    // Log.debug(
                    // "Normal " + contact.getContactNormal() + " ,Penetration "
                    // + contact.getPenetration()
                    // + " ,x overlap" + xOverlap + " ,y overlap" + yOverlap);
                    return;
                }
            }
        }
    }

    @Override
    public void generateContactBox(final BoxPrimitive box, final Contact contact)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void generateContactCircle(final CirclePrimitive box, final Contact contact)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Vec2f getExtends()
    {
        return extend;
    }

    @Override
    public boolean lineTrace(final Vec2f origin, final Vec2f iDirection, final HitData result)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean boxTrace(final Vec2f origin, final Vec2f extend, final HitData result)
    {
        final int startX = Math.round(origin.getX()) / tileMap.getTileWidth();
        final int startY = Math.round(origin.getY()) / tileMap.getTileHeight();
        final int width = Math.round(origin.getX() + extend.getX()) / tileMap.getTileWidth() - startX;
        final int height = Math.round(origin.getY() + extend.getY()) / tileMap.getTileHeight() - startY;
        for (int i = startX; i <= startX + width; i++)
        {
            for (int j = startY; j <= startY + height; j++)
            {
                if (tileMap.blocksDynamic(i, j))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean circleTrace(final Vec2f origin, final float radius, final HitData result)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean trace(final Primitive primitive, final HitData result)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
