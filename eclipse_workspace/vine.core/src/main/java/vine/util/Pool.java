package vine.util;

import java.util.ArrayList;
import java.util.List;

public class Pool<T>
{
    Class<T>      instanceType;
    final List<T> pool;

    public Pool(final Class<T> type, final int size)
    {
        pool = new ArrayList<>(size);
        instanceType = type;
    }

    public T take()
    {
        if (pool.isEmpty())
        {
            try
            {
                return instanceType.newInstance();
            } catch (InstantiationException | IllegalAccessException exception)
            {
                Log.exception("Failed to create object in object pool", exception);
                return null;
            }
        } else
        {
            final T t = pool.get(0);
            pool.remove(0);
            return t;
        }
    }

    public void give(final T object)
    {
        pool.add(object);
    }
}