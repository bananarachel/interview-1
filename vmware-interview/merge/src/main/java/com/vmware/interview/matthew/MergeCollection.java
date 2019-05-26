
/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;


import java.util.Collection;
import java.util.Comparator;

/**
 * Merge two sorted collections of the same kind into a single sorted collection.
 *
 * @param <E> the type of elements in this list
 */
public interface MergeCollection<E>
{

    /**
     * Merge left and right collection into one, this function will use {@param comparator} for comparision
     *
     * @param left       collection containing elements will be merged
     * @param right      collection containing elements will be merged
     * @param result     collection which will be stored merge result
     * @param comparator comparator which will be use to compare element of type E
     * @throws NullPointerException     if {@param left}, {@param right}, {@param result} or {@param comparator} is null
     * @throws IllegalArgumentException if {@param left} or {@param right} not sorted
     */
    void merge(Collection<E> left, Collection<E> right, Collection<E> result, Comparator<E> comparator);
}
