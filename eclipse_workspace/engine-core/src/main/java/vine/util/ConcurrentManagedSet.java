package vine.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentManagedSet<T>
{
    private final Map<T, T> addSet    = new ConcurrentHashMap<>();
    private final Map<T, T> removeSet = new ConcurrentHashMap<>();
    private final Set<T>    set;

    public ConcurrentManagedSet(final Set<T> set)
    {
        this.set = set;
    }

    public void add(final T object)
    {
        this.addSet.put(object, object);
    }

    public void remove(final T object)
    {
        this.removeSet.put(object, object);
    }

    public Iterable<T> getIterable()
    {
        if (!this.removeSet.isEmpty())
        {
            this.set.removeAll(this.removeSet.values());
            this.removeSet.clear();
        }
        if (!this.addSet.isEmpty())
        {
            this.set.addAll(this.addSet.values());
            this.addSet.clear();
        }
        return this.set;
    }

    public boolean isEmpty()
    {
        getIterable();
        return this.set.isEmpty();
    }

    public int size()
    {
        getIterable();
        return this.set.size();
    }
}
