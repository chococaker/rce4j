package org.rce4j.util;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ConcurrentHashSet<E> implements Set<E>, Serializable {
    public ConcurrentHashSet() {
        internalMap = new ConcurrentHashMap<>();
    }
    
    public ConcurrentHashSet(int initialCapacity) {
        internalMap = new ConcurrentHashMap<>(initialCapacity);
    }
    
    public ConcurrentHashSet(int initialCapacity, float loadFactor) {
        internalMap = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }
    
    public ConcurrentHashSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        internalMap = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }
    
    public ConcurrentHashSet(Collection<? extends E> values) {
        internalMap = new ConcurrentHashMap<>(values.size());
        for (E value : values) {
            internalMap.put(value, FILLER);
        }
    }
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    private static final Object FILLER = new Object();
    
    private final Map<E, Object> internalMap;
    
    @Override
    public int size() {
        return internalMap.size();
    }
    
    @Override
    public boolean isEmpty() {
        return internalMap.isEmpty();
    }
    
    @Override
    public boolean contains(Object o) {
        //noinspection SuspiciousMethodCalls
        return internalMap.containsKey(o);
    }
    
    @Override
    public Iterator<E> iterator() {
        return internalMap.keySet().iterator();
    }
    
    @Override
    public Object[] toArray() {
        return internalMap.keySet().toArray();
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        return internalMap.keySet().toArray(a);
    }
    
    @Override
    public boolean add(E e) {
        return internalMap.put(e, FILLER) != e;
    }
    
    @Override
    public boolean remove(Object o) {
        return internalMap.remove(o) != null;
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        return internalMap.keySet().containsAll(c);
    }
    
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean wasChanged = false;
        for (E e : c) {
            if (!wasChanged) {
                wasChanged = add(e);
            } else {
                add(e);
            }
        }
        
        return wasChanged;
    }
    
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean wasChanged = false;
        
        for (E e : internalMap.keySet()) {
            if (!c.contains(e)) {
                if (!wasChanged)
                    wasChanged = true;
                remove(e);
            }
        }
        
        return wasChanged;
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean wasChanged = false;
        
        for (Object e : c) {
            if (!c.contains(e)) {
                if (!wasChanged)
                    wasChanged = true;
                remove(e);
            }
        }
        
        return wasChanged;
    }
    
    @Override
    public void clear() {
        internalMap.clear();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcurrentHashSet<?> that = (ConcurrentHashSet<?>) o;
        return Objects.equals(internalMap, that.internalMap);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(internalMap);
    }
    
    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";
        
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }
}
