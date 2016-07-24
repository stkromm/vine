package vine.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentManagedSet<T> extends AbstractSet<T> implements Serializable
{
    transient FastIterator<T>                   iterator;
    T[]                                         elements;
    private static final long                   serialVersionUID = -2365638121421284770L;
    private transient final Map<T, T>           addSet           = new ConcurrentHashMap<>();
    private transient final Map<Object, Object> removeSet        = new ConcurrentHashMap<>();
    private transient final Set<T>              set;

    public ConcurrentManagedSet(final Set<T> set)
    {
        this.set = set;
    }

    public Iterable<T> getIterable()
    {

        return this.set;
    }

    @Override
    public boolean isEmpty()
    {
        getIterable();
        return this.set.isEmpty();
    }

    @Override
    public int size()
    {
        return elements.length;
    }

    @Override
    public boolean add(final T e)
    {
        return this.addSet.put(e, e) != null;
    }

    @Override
    public boolean remove(final Object o)
    {
        return this.removeSet.put(o, o) != null;
    }

    @Override
    public Iterator<T> iterator()
    {
        int i = 0;
        if (!this.removeSet.isEmpty())
        {
            i += removeSet.size();
            this.set.removeAll(this.removeSet.values());
            this.removeSet.clear();
        }
        if (!this.addSet.isEmpty())
        {
            i += addSet.size();
            this.set.addAll(this.addSet.values());
            this.addSet.clear();
        }

        FastIterator<T> it = iterator;
        if (it == null || i != 0)
        {
            elements = (T[]) set.toArray();
            it = new FastIterator<T>(elements);
            iterator = it;
        } else
        {
            it.index = 0;
        }

        return it;
    }

    private static final class FastIterator<T> implements Iterator<T>
    {
        private final T[] elements;
        int               index;

        public FastIterator(final T[] elements)
        {
            this.elements = elements;
        }

        @Override
        public boolean hasNext()
        {
            return index != elements.length;
        }

        @Override
        public T next()
        {
            return elements[index++];
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}
