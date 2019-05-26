/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */


package com.vmware.interview.matthew;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

/**
 * Merge two sorted collections into one
 *
 * @param <E> the type of elements in this list
 */
public class MergeCollectionImpl<E> implements MergeCollection<E>
{
    /**
     * Merge two sorted collections
     *
     * @param left       collection containing elements will be merged
     * @param right      collection containing elements will be merged
     * @param result     collection which will be stored with merge result
     * @param comparator comparator which will be use to compare element of type E
     * @throws NullPointerException     if {@param left}, {@param right}, {@param result} or {@param comparator} is null
     * @throws IllegalArgumentException if {@param left} or {@param right} not sorted
     */
    @Override
    public void merge(Collection<E> left, Collection<E> right, Collection<E> result, Comparator<E> comparator)
    {
        // pre-check
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(result);
        Objects.requireNonNull(comparator);

        if (notSorted(left, comparator))
        {
            throw new IllegalArgumentException("left collection not sorted.");
        }
        if (notSorted(right, comparator))
        {
            throw new IllegalArgumentException("right collection not sorted.");
        }

        // Some corner cases
        result.clear();
        if (left.isEmpty())
        {
            result.addAll(right);
            return;
        }

        if (right.isEmpty())
        {
            result.addAll(left);
            return;
        }


        // Start merge two collections
        Iterator<E> leftIt = left.iterator();
        Iterator<E> rightIt = right.iterator();
        E leftValue = leftIt.next();
        E rightValue = rightIt.next();


        while (leftIt.hasNext() && rightIt.hasNext())
        {
            if (comparator.compare(leftValue, rightValue) <= 0)
            {
                result.add(leftValue);
                leftValue = leftIt.next();
            } else
            {
                result.add(rightValue);
                rightValue = rightIt.next();
            }
        }

        // Case 1: left collection still have more than one element
        //         right collection have last element
        // [rightItem]
        // [leftItem, leftItem ... leftItem]
        if (leftIt.hasNext())
        {
            while (leftIt.hasNext())
            {
                if (comparator.compare(leftValue, rightValue) <= 0)
                {
                    result.add(leftValue);
                    leftValue = leftIt.next();
                } else
                {
                    // last element merged
                    result.add(rightValue);
                    rightValue = null;
                    break;
                }
            }

            // merge last elements
            merge(rightValue, leftValue, leftIt, result, comparator);
        }

        // Case 2: right collection still have more then one element
        //         left collection have last element
        // [leftItem]
        // [rightItem rightItem ... rightItem]
        if (rightIt.hasNext())
        {
            while (rightIt.hasNext())
            {
                if (comparator.compare(leftValue, rightValue) <= 0)
                {
                    // last element merged
                    result.add(leftValue);
                    leftValue = null;
                    break;
                } else
                {
                    result.add(rightValue);
                    rightValue = rightIt.next();
                }
            }
            // merge last elements
            merge(leftValue, rightValue, rightIt, result, comparator);
        }
    }

    /**
     * Check collection sorted or not base on {@param comparator}
     *
     * @param collection collection will be checked
     * @param comparator compare element
     * @return true if collection not sorted, otherwise false
     */
    private boolean notSorted(Collection<E> collection, Comparator<E> comparator)
    {
        if (collection.isEmpty())
        {
            return false;
        }

        Iterator<E> it = collection.iterator();
        E prev = it.next();
        while (it.hasNext())
        {
            E current = it.next();
            if (comparator.compare(prev, current) > 0)
            {
                return true;
            }
            prev = current;
        }
        return false;
    }

    /**
     * Merge last value with collection
     *
     * @param lastValue       last element
     * @param collectionValue collection current element
     * @param collectionIt    collection iterator
     * @param result          store results
     * @param comparator      compare elements
     */
    private void merge(E lastValue, E collectionValue, Iterator<E> collectionIt, Collection<E> result, Comparator<E> comparator)
    {
        if (lastValue != null)
        {
            if (comparator.compare(collectionValue, lastValue) <= 0)
            {
                result.add(collectionValue);
                result.add(lastValue);
            } else
            {
                result.add(lastValue);
                result.add(collectionValue);
            }
        } else
        {
            result.add(collectionValue);
        }
        while (collectionIt.hasNext())
        {
            result.add(collectionIt.next());
        }
    }
}
