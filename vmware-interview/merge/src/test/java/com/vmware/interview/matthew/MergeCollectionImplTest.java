/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MergeCollectionImplTest
{

    @Test
    public void testMergeCollectionWithList()
    {
        // Merge two sorted lists
        List<Integer> left = Arrays.asList(4, 6, 7, 10, 12);
        List<Integer> right = Arrays.asList(2, 5, 11, 15, 16);

        MergeCollection<Integer> merger = new MergeCollectionImpl<>();
        List<Integer> result = new ArrayList<>();
        merger.merge(left, right, result, Integer::compare);

        List<Integer> expected = Arrays.asList(2, 4, 5, 6, 7, 10, 11, 12, 15, 16);
        checkMergeResult(left, right, result, expected);

        // Merge reserved lists
        left = Arrays.asList(12, 10, 7, 6, 4);
        right = Arrays.asList(16, 15, 11, 5, 2);
        expected = Arrays.asList(16, 15, 12, 11, 10, 7, 6, 5, 4, 2);
        merger.merge(left, right, result, (a, b) -> b - a);
        checkMergeResult(left, right, result, expected);


        // empty left
        left = new ArrayList<>();
        right = Arrays.asList(1, 2, 4, 6, 7);
        expected = right;
        merger.merge(left, right, result, Integer::compare);
        checkMergeResult(left, right, result, expected);

        // empty right
        left = Arrays.asList(2, 4, 5, 8, 10);
        right = new ArrayList<>();
        expected = left;
        merger.merge(left, right, result, Integer::compare);
        checkMergeResult(left, right, result, expected);

        // left have one element
        left = Arrays.asList(10);
        right = Arrays.asList(1, 3, 4, 5, 9);
        expected = Arrays.asList(1, 3, 4, 5, 9, 10);
        merger.merge(left, right, result, Integer::compare);
        checkMergeResult(left, right, result, expected);

        left = Arrays.asList(8);
        right = Arrays.asList(1, 3, 4, 5, 9);
        expected = Arrays.asList(1, 3, 4, 5, 8, 9);
        merger.merge(left, right, result, Integer::compare);
        checkMergeResult(left, right, result, expected);

        // right have one element
        left = Arrays.asList(1, 3, 4, 5, 9);
        right = Arrays.asList(10);
        expected = Arrays.asList(1, 3, 4, 5, 9, 10);
        merger.merge(left, right, result, Integer::compare);
        checkMergeResult(left, right, result, expected);

        left = Arrays.asList(1, 3, 4, 5, 9);
        right = Arrays.asList(8);
        expected = Arrays.asList(1, 3, 4, 5, 8, 9);
        merger.merge(left, right, result, Integer::compare);
        checkMergeResult(left, right, result, expected);
    }

    private void checkMergeResult(List<Integer> left, List<Integer> right, List<Integer> result, List<Integer> expected)
    {
        assertEquals(left.size() + right.size(), result.size());
        for (int i = 0; i < expected.size(); i++)
        {
            assertEquals(expected.get(i), result.get(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMergeCollectionWithException()
    {
        List<Integer> left = Arrays.asList(4, 6, 20, 10, 12);
        List<Integer> right = Arrays.asList(2, 5, 11, 15, 16);
        List<Integer> result = new ArrayList<>();

        MergeCollection<Integer> merger = new MergeCollectionImpl<>();
        merger.merge(left, right, result, Integer::compare);
    }

    private List<Integer> createLinkedList(int[] array)
    {
        List<Integer> list = new LinkedList<>();
        for (int value : array)
        {
            list.add(value);
        }
        return list;
    }

    @Test
    public void testMergeCollectionWithLinkedList()
    {
        int[] leftArray = {-10, -5, 0, 5, 8, 10, 20};
        int[] rightArray = {-100, 2, 5, 9, 10, 13};
        int[] resultArray = {-100, -10, -5, 0, 2, 5, 5, 8, 9, 10, 10, 13, 20};
        List<Integer> left = createLinkedList(leftArray);
        List<Integer> right = createLinkedList(rightArray);
        List<Integer> expected = createLinkedList(resultArray);

        List<Integer> result = new LinkedList<>();
        MergeCollection<Integer> merger = new MergeCollectionImpl<>();
        merger.merge(left, right, result, Integer::compare);
        checkMergeResult(left, right, result, expected);
    }
}