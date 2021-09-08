package me.conclure.cityrp.utility;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MoreCollections {
    public static final Multimap EMPTY_MULTIMAP = new EmptyMultimap<>();
    public static final Multiset EMPTY_MULTISET = new EmptyMultiset<>();

    public static <K,V> Multimap<K,V> emptyMultimap() {
        return EMPTY_MULTIMAP;
    }

    public static <E> Multiset<E> emptyMultiset() {
        return EMPTY_MULTISET;
    }

    private static class EmptyMultimap<K,V> implements Multimap<K,V> {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean containsKey(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsValue(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsEntry(@Nullable Object o, @Nullable Object o1) {
            return false;
        }

        @Override
        public boolean put(@Nullable K k, @Nullable V v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(@Nullable Object o, @Nullable Object o1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean putAll(@Nullable K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<V> removeAll(@Nullable Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<V> get(@Nullable K k) {
            return Collections.emptySet();
        }

        @Override
        public Set<K> keySet() {
            return Collections.emptySet();
        }

        @Override
        public Multiset<K> keys() {
            return emptyMultiset();
        }

        @Override
        public Collection<V> values() {
            return Collections.emptySet();
        }

        @Override
        public Collection<Map.Entry<K, V>> entries() {
            return Collections.emptySet();
        }

        @Override
        public Map<K, Collection<V>> asMap() {
            return Collections.emptyMap();
        }
    }

    private static class EmptyMultiset<E> implements Multiset<E> {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int count(@Nullable Object o) {
            return 0;
        }

        @Override
        public int add(@Nullable E e, int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int remove(@Nullable Object o, int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int setCount(E e, int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setCount(E e, int i, int i1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<E> elementSet() {
            return Collections.emptySet();
        }

        @Override
        public Set<Entry<E>> entrySet() {
            return Collections.emptySet();
        }

        @Override
        public Iterator<E> iterator() {
            return Collections.emptyIterator();
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NotNull
        @Override
        public <T> T[] toArray(T[] a) {
            if (a.length > 0)
                a[0] = null;
            return a;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(@Nullable Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }
}
