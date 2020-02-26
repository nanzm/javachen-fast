package com.javachen.common.utils;

import java.util.*;
import java.util.Map.Entry;

/**
 * Convenience methods for interacting with maps
 */
public class BLCMapUtils {

    /**
     * Given a collection of values and a TypedClosure that maps an appropriate key for a given value,
     * returns a HashMap of the key to the value.
     *
     * <b>Note: If two values share the same key, the later one will override the previous one in the returned map</b>
     *
     * @param values
     * @param closure
     * @return the map
     * @see #keyedListMap(Iterable, TypedClosure)
     * <p>
     * List<V> --> Map<K, V>
     */
    public static <K, CV extends Iterable<V>, V> Map<K, V> keyedMap(CV values, TypedClosure<K, V> closure) {
        Map<K, V> map = new HashMap<K, V>();

        for (V value : values) {
            K key = closure.getKey(value);
            map.put(key, value);
        }

        return map;
    }

    /**
     * Given an array of values and a TypedClosure that maps an appropriate key for a given value,
     * returns a HashMap of the key to the value.
     *
     * <b>Note: If two values share the same key, the later one will override the previous one in the returned map</b>
     *
     * @param values
     * @param closure
     * @return the map
     * @see #keyedListMap(Iterable, TypedClosure)
     * <p>
     * V[] --> Map<K, V>
     */
    public static <K, V> Map<K, V> keyedMap(V[] values, TypedClosure<K, V> closure) {
        Map<K, V> map = new HashMap<K, V>();

        if (values != null) {
            for (V value : values) {
                K key = closure.getKey(value);
                map.put(key, value);
            }
        }

        return map;
    }

    /**
     * Given a collection of values and a TypedClosure that maps an appropriate key for a given value,
     * returns a HashMap of the key to a list of values that map to that key.
     *
     * @param values
     * @param closure
     * @return the map
     * @see #keyedMap(Iterable, TypedClosure)
     * <p>
     * List<V> --> Map<K, List<V>>
     */
    public static <K, CV extends Iterable<V>, V> Map<K, List<V>> keyedListMap(CV values, TypedClosure<K, V> closure) {
        Map<K, List<V>> map = new HashMap<K, List<V>>();

        for (V value : values) {
            K key = closure.getKey(value);
            List<V> list = map.get(key);
            if (list == null) {
                list = new ArrayList<V>();
                map.put(key, list);
            }
            list.add(value);
        }

        return map;
    }

    public static <K, V> Map<K, V> valueSortedMap(Map<K, V> map, Comparator<Entry<K, V>> comparator) {
        Set<Entry<K, V>> valueSortedEntries = new TreeSet<Entry<K, V>>(comparator);

        for (Entry<K, V> entry : map.entrySet()) {
            valueSortedEntries.add(entry);
        }

        Map<K, V> sortedMap = new LinkedHashMap<K, V>(map.size());
        for (Entry<K, V> entry : valueSortedEntries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
