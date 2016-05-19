package vine.game.scene;

import vine.math.VineMath;
import vine.math.geometry.Intersection2D;
import vine.math.vector.Vec2f;
import vine.math.vector.Vector2s;
import vine.util.Log;

public class SceneTracer
{
    public static final float I_CHUNK_WIDTH  = 1 / 1400f;
    public static final float I_CHUNK_HEIGHT = 1 / 800f;
    public static final int   CHUNKS         = 9;
    private final Scene       scene;

    @FunctionalInterface
    public interface EntityTraverseCheck
    {
        void traverseEntity(GameEntity entity);
    }

    public SceneTracer(final Scene scene)
    {
        this.scene = scene;
    }

    public void traverseScene(
            final int startChunkX,
            final int startChunkY,
            final int endChunkX,
            final int endChunkY,
            final EntityTraverseCheck traverser)
    {
        for (int i = endChunkX - startChunkX; i >= 0; i--)
        {
            for (int j = endChunkY - startChunkY; j >= 0; j--)
            {
                for (final GameEntity entity : this.scene.getChunks()[startChunkX + i][startChunkY + j].getEntities())
                {
                    traverser.traverseEntity(entity);
                }
            }
        }
    }

    public boolean aabbTrace(
            final GameEntity tracer,
            final boolean ignoreSelf,
            final Vec2f origin,
            final Vec2f extend,
            final TraceResult result)
    {
        final long startTime = System.nanoTime();
        final int startChunkX = (int) VineMath.clamp(origin.getX() * SceneTracer.I_CHUNK_WIDTH, 0, SceneTracer.CHUNKS);
        final int startChunkY = (int) VineMath.clamp(origin.getY() * SceneTracer.I_CHUNK_HEIGHT, 0, SceneTracer.CHUNKS);
        final int endChunkX = (int) VineMath
                .clamp((origin.getX() + extend.getX()) * SceneTracer.I_CHUNK_WIDTH, 0, SceneTracer.CHUNKS);
        final int endChunkY = (int) VineMath
                .clamp((origin.getY() + extend.getY()) * SceneTracer.I_CHUNK_HEIGHT, 0, SceneTracer.CHUNKS);

        GameEntity nearestEntity = null;
        float smallestDistance = Float.MAX_VALUE;

        for (int i = endChunkX - startChunkX; i >= 0; i--)
        {
            for (int j = endChunkY - startChunkY; j >= 0; j--)
            {
                for (final GameEntity entity : this.scene.getChunks()[startChunkX + i][startChunkY + j].getEntities())
                {
                    if ((!ignoreSelf || !tracer.equals(entity)) && Intersection2D.doesAabbIntersectAabb(
                            entity.getPosition(),
                            entity.getBoundingBoxExtends(),
                            origin,
                            extend))
                    {
                        final double length = Vector2s
                                .length(origin.getX() - entity.getXPosition(), origin.getY() - entity.getYPosition());
                        if (length < smallestDistance)
                        {
                            smallestDistance = (float) length;
                            nearestEntity = entity;
                        }
                    }
                }
            }
        }
        if (Log.isDebugEnabled())
        {
            Log.debugUnchecked("CircleTrace took " + (System.nanoTime() - startTime) * (1 / 1000000f) + "milliseconds");
        }
        return nearestEntity != null;
    }

    public boolean multiAABBTrace(
            final GameEntity tracer,
            final Vec2f origin,
            final Vec2f extend,
            final MultiTraceResult result)
    {
        result.prepare();

        final long startTime = System.nanoTime();
        final int startChunkX = (int) VineMath.clamp(origin.getX() * (1 / 1400f), 0, 9);
        final int startChunkY = (int) VineMath.clamp(origin.getY() * (1f / 800), 0, 9);
        final int endChunkX = (int) VineMath.clamp((origin.getX() + extend.getX()) * (1 / 1400f), 0, 9);
        final int endChunkY = (int) VineMath.clamp((origin.getY() + extend.getY()) * (1 / 800), 0, 9);

        final boolean ignoreSelf = tracer != null;
        traverseScene(startChunkX, startChunkY, endChunkX, endChunkY, entity ->
        {
            if (ignoreSelf && entity.compareTo(tracer) != 0 && Intersection2D
                    .doesAabbIntersectAabb(entity.getPosition(), entity.getBoundingBoxExtends(), origin, extend))
            {
                result.getEntities().add(entity);
                final double length = Vector2s
                        .length(origin.getX() - entity.getXPosition(), origin.getY() - entity.getYPosition());
                if (length < result.getNearestHitDistance())
                {
                    result.setNearestHitDistance((float) length);
                }
            }
        });
        if (Log.isDebugEnabled())
        {
            Log.debugUnchecked("multiAabbTrace took " + (System.nanoTime() - startTime) / 1000000f + "milliseconds");
        }
        return !result.getEntities().isEmpty();
    }

    /**
     * Trace for GameEntities in the Scene in the area defined by the circle
     * defined by the given center and radius.
     * 
     * @throws NullPointerException
     *             if result is null
     * @param tracer
     *            A GameEntity that is ignored when tracing.
     * @param center
     *            The center of the circle that is used to trace.
     * @param radius
     *            The radius of the circle that is used to trace.
     * @param result
     *            Used to store detailed results of the trace
     * @return True, if there was at least one hit.
     * 
     */
    public boolean circleTrace(
            final GameEntity tracer,
            final Vec2f center,
            final float radius,
            final TraceResult result)
    {
        final long startTime = System.nanoTime();

        assert result != null : "Passed null reference to circleTrace. Traces need a valid result object passed.";
        result.prepare();

        final int startChunkX = (int) VineMath
                .clamp((center.getX() - radius) * SceneTracer.I_CHUNK_WIDTH, 0, SceneTracer.CHUNKS);
        final int startChunkY = (int) VineMath
                .clamp((center.getY() - radius) * SceneTracer.I_CHUNK_HEIGHT, 0, SceneTracer.CHUNKS);
        final int endChunkX = (int) VineMath
                .clamp((center.getX() + radius) * SceneTracer.I_CHUNK_WIDTH, 0, SceneTracer.CHUNKS);
        final int endChunkY = (int) VineMath
                .clamp((center.getY() + radius) * SceneTracer.I_CHUNK_HEIGHT, 0, SceneTracer.CHUNKS);

        final boolean ignoreSelf = tracer != null;
        traverseScene(startChunkX, startChunkY, endChunkX, endChunkY, entity ->
        {
            if (ignoreSelf && entity.compareTo(tracer) != 0 && Intersection2D
                    .doesAabbIntersectCircle(entity.getPosition(), entity.getBoundingBoxExtends(), center, radius))
            {
                final double length = Vector2s
                        .length(center.getX() - entity.getXPosition(), center.getY() - entity.getYPosition());
                if (length < result.getDistance())
                {
                    result.setDistance((float) length);
                    result.setEntity(entity);
                }
            }
        });

        if (Log.isDebugEnabled())
        {
            Log.debugUnchecked("CircleTrace took " + (System.nanoTime() - startTime) * (1 / 1000000f) + "milliseconds");
        }
        return result.getDistance() < Float.MAX_VALUE;
    }

    /**
     * Trace for GameEntities in the Scene in the area defined by the circle
     * defined by the given center and radius.
     * 
     * @throws NullPointerException
     *             if result is null
     * @param tracer
     *            A GameEntity that is ignored when tracing.
     * @param center
     *            The center of the circle that is used to trace.
     * @param radius
     *            The radius of the circle that is used to trace.
     * @param result
     *            Used to store detailed results of the trace
     * @return True, if there was at least one hit.
     * 
     */
    public boolean multiCircleTrace(
            final GameEntity tracer,
            final Vec2f center,
            final float radius,
            final MultiTraceResult result) throws NullPointerException
    {
        assert result != null;

        final long startTime = System.nanoTime();
        result.prepare();
        final int startChunkX = (int) VineMath
                .clamp((center.getX() - radius) * SceneTracer.I_CHUNK_WIDTH, 0, SceneTracer.CHUNKS);
        final int startChunkY = (int) VineMath
                .clamp((center.getY() - radius) * SceneTracer.I_CHUNK_HEIGHT, 0, SceneTracer.CHUNKS);
        final int endChunkX = (int) VineMath
                .clamp((center.getX() + radius) * SceneTracer.I_CHUNK_WIDTH, 0, SceneTracer.CHUNKS);
        final int endChunkY = (int) VineMath
                .clamp((center.getY() + radius) * SceneTracer.I_CHUNK_HEIGHT, 0, SceneTracer.CHUNKS);

        final boolean ignoreSelf = tracer != null;
        traverseScene(startChunkX, startChunkY, endChunkX, endChunkY, entity ->
        {
            if (ignoreSelf && entity.compareTo(tracer) != 0 && Intersection2D
                    .doesAabbIntersectCircle(entity.getPosition(), entity.getBoundingBoxExtends(), center, radius))
            {
                result.getEntities().add(entity);
                final double length = Vector2s
                        .length(center.getX() - entity.getXPosition(), center.getY() - entity.getYPosition());
                if (length < result.getNearestHitDistance())
                {
                    result.setNearestHitDistance((float) length);
                }
            }
        });
        if (Log.isDebugEnabled())
        {
            Log.debugUnchecked("CircleTrace took " + (System.nanoTime() - startTime) / 1000000f + "milliseconds");
        }
        return result.nearestHitDistance < Float.MAX_VALUE;
    }

    public boolean rayTrace(
            final GameEntity tracer,
            final boolean ignoreSelf,
            final Vec2f origin,
            final Vec2f direction,
            final float distance,
            final TraceResult result)
    {
        return this
                .rayTrace(tracer, origin.getX(), origin.getY(), direction.getX(), direction.getY(), distance, result);
    }

    /**
     * Trace for GameEntities in the scene that are intersected by a line
     * segment that is defined by the given ray defined by the
     * origin(originX,originY) and the direction(directionX, directionY) and
     * extends the given distance (length of the distance form the origin to the
     * other point of the line).
     * 
     * @param tracer
     *            A GameEntity that is ignored when tracing.
     * @param originX
     *            The x coordinate of the ray origin.
     * @param originY
     *            The y coordinate of the ray origin.
     * @param directionX
     *            The x coordinate of the ray direction.
     * @param directionY
     *            The y coordinate of the ray direction.
     * @param distance
     *            Defines the length of the ray in which entities are hit.
     * @param result
     *            Used to store detailed results of the trace
     * @return True, if there was at least one hit.
     * @throws NullPointerException
     *             if result == null
     */
    public boolean rayTrace(
            final GameEntity tracer,
            final float originX,
            final float originY,
            final float directionX,
            final float directionY,
            final float distance,
            final TraceResult result) throws NullPointerException
    {
        final long startTime = System.nanoTime();

        assert result != null : "Passed null reference to rayTrace. Traces need a valid result object passed.";
        result.prepare();

        final double directionLength = Vector2s.length(directionX, directionY);
        assert directionLength != .0 : "Passed a direction vector with zero length to rayTrace.";
        final float iLength = 1f / (float) directionLength;
        final float direcX = directionX * iLength;
        final float iDirecX = 1 / direcX;
        final float direcY = directionY * iLength;
        final float iDirecY = 1 / direcY;

        final int startChunkX = (int) VineMath.clamp(originX * SceneTracer.I_CHUNK_WIDTH, 0, SceneTracer.CHUNKS);
        final int startChunkY = (int) VineMath.clamp(originY * SceneTracer.I_CHUNK_HEIGHT, 0, SceneTracer.CHUNKS);
        final int endChunkX = (int) VineMath
                .clamp((originX + distance * directionX * iLength) * SceneTracer.I_CHUNK_WIDTH, 0, SceneTracer.CHUNKS);
        final int endChunkY = (int) VineMath
                .clamp((originY + distance * directionY * iLength) * SceneTracer.I_CHUNK_HEIGHT, 0, SceneTracer.CHUNKS);

        final boolean ignoreSelf = tracer != null;
        traverseScene(startChunkX, startChunkY, endChunkX, endChunkY, entity ->
        {
            if (!ignoreSelf || entity.compareTo(tracer) != 0)
            {
                final float length = Intersection2D.whereDoesRayIntersectAabb(
                        originX,
                        originY,
                        iDirecX,
                        iDirecY,
                        entity.getPosition().getX(),
                        entity.getPosition().getY(),
                        entity.getBoundingBoxExtends().getX(),
                        entity.getBoundingBoxExtends().getY());
                if (length >= distance || length >= result.getDistance())
                {
                    result.setDistance(length);
                    result.setEntity(entity);
                }
            }
        });
        if (Log.isDebugEnabled())
        {
            Log.debugUnchecked("LineTrace took " + (System.nanoTime() - startTime) / 1000000f + "milliseconds");
        }
        return result.getEntity() != null;
    }

    public boolean multiRayTrace(
            final GameEntity tracer,
            final boolean ignoreSelf,
            final Vec2f origin,
            final Vec2f direction,
            final float distance,
            final TraceResult result)
    {
        return this
                .rayTrace(tracer, origin.getX(), origin.getY(), direction.getX(), direction.getY(), distance, result);
    }

    /**
     * Trace for GameEntities in the scene that are intersected by a line
     * segment that is defined by the given ray defined by the
     * origin(originX,originY) and the direction(directionX, directionY) and
     * extends the given distance (length of the distance form the origin to the
     * other point of the line).
     * 
     * @param tracer
     *            A GameEntity that is ignored when tracing.
     * @param originX
     *            The x coordinate of the ray origin.
     * @param originY
     *            The y coordinate of the ray origin.
     * @param directionX
     *            The x coordinate of the ray direction.
     * @param directionY
     *            The y coordinate of the ray direction.
     * @param distance
     *            Defines the length of the ray in which entities are hit.
     * @param result
     *            Used to store detailed results of the trace
     * @return True, if there was at least one hit.
     * @throws NullPointerException
     *             if result == null
     */
    public boolean multiRayTrace(
            final GameEntity tracer,
            final float originX,
            final float originY,
            final float directionX,
            final float directionY,
            final float distance,
            final MultiTraceResult result) throws NullPointerException
    {
        assert result != null : "Passed null reference to multiRayTrace. MultiTraces need a valid result object passed.";

        final long startTime = System.nanoTime();
        result.prepare();

        final double directionLength = Vector2s.length(directionX, directionY);
        assert directionLength != .0 : "Passed a direction vector with zero length to multiRayTrace.";
        final float inversedDirectionLength = 1f / (float) directionLength;
        final float iDirectionX = 1 / (directionX * inversedDirectionLength);
        final float iDirectionY = 1 / (directionY * inversedDirectionLength);

        final int startChunkX = (int) VineMath.clamp(originX * SceneTracer.I_CHUNK_WIDTH, 0, SceneTracer.CHUNKS);
        final int startChunkY = (int) VineMath.clamp(originY * SceneTracer.I_CHUNK_HEIGHT, 0, SceneTracer.CHUNKS);
        final int endChunkX = (int) VineMath.clamp(
                (originX + distance * directionX * inversedDirectionLength) * SceneTracer.I_CHUNK_WIDTH,
                0,
                SceneTracer.CHUNKS);
        final int endChunkY = (int) VineMath.clamp(
                (originY + distance * directionY * inversedDirectionLength) * SceneTracer.I_CHUNK_HEIGHT,
                0,
                SceneTracer.CHUNKS);

        final boolean ignoreSelf = tracer != null;
        traverseScene(startChunkX, startChunkY, endChunkX, endChunkY, (entity) ->
        {
            if (ignoreSelf && entity.compareTo(tracer) == 0)
            {
                return;
            }
            final float tracedDistance = Intersection2D.whereDoesRayIntersectAabb(
                    originX,
                    originY,
                    iDirectionX,
                    iDirectionY,
                    entity.getXPosition(),
                    entity.getYPosition(),
                    entity.getBoundingBoxExtends().getX(),
                    entity.getBoundingBoxExtends().getY());
            if (tracedDistance >= distance)
            {
                return;
            }
            result.getEntities().add(entity);
            if (tracedDistance < result.getNearestHitDistance())
            {
                result.setNearestHitDistance(tracedDistance);
            }
        });
        if (Log.isDebugEnabled())
        {
            Log.debugUnchecked("MultiLineTrace took " + (System.nanoTime() - startTime) / 1000000f + "milliseconds");
            Log.debugUnchecked("Chunks x:" + startChunkX + "-" + endChunkX + " y:" + startChunkY + "-" + endChunkY);
        }
        return result.getNearestHitDistance() < Float.MAX_VALUE;
    }
}
