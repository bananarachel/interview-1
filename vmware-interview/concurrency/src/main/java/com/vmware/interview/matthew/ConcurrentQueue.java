/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A fully blocked queue by using array
 * @param <E> element type
 */
public class ConcurrentQueue<E> extends AbstractQueue<E>
{
    /**
     * Array to store elements
     */
    private final Object[] elements;

    /**
     * Total count at queue
     */
    private int count;

    /**
     * head index for poll
     */
    private int head;

    /**
     * tail index for offer
     */
    private int tail;

    private final ReentrantLock fullLock;

    private final Condition notEmpty;

    private final Condition notFull;

    public ConcurrentQueue(int capacity)
    {
        if (capacity <= 0)
        {
            throw new IllegalArgumentException("capacity cannot less then 0");
        }

        this.elements = new Object[capacity];
        fullLock = new ReentrantLock();
        notEmpty = fullLock.newCondition();
        notFull = fullLock.newCondition();
    }


    @Override
    public Iterator<E> iterator()
    {
        throw new NotImplementedException();
    }

    @Override
    public int size()
    {
        final ReentrantLock lock = this.fullLock;
        lock.lock();
        try
        {
            return count;
        } finally
        {
            lock.unlock();
        }
    }

    /**
     * put item into queue, if queue full, will wait infinity until queue have available slot
     * @param e item will put into queue
     * @return true if succeed, otherwise, return false if wait action interrupted
     */
    @Override
    public boolean offer(E e)
    {
        e = Objects.requireNonNull(e);

        try
        {
            final ReentrantLock lock = this.fullLock;
            lock.lockInterruptibly();
            try
            {
                // queue is full, wait for slot
                if (count == elements.length)
                {
                    notFull.await();
                }

                final Object[] items = this.elements;
                items[tail] = e;
                // If tail arrived array's end, reset it
                tail++;
                if (tail == items.length)
                {
                    tail = 0;
                }
                count++;
                notEmpty.signal();
                return true;
            }
            finally
            {
                lock.unlock();
            }
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }


    /**
     * Get item from queue's head. If queue is empty, wait infinity until queue have available items
     * @return item or null if wait action interrupted
     */
    @Override
    public E poll()
    {
        final ReentrantLock lock = this.fullLock;
        try
        {
            lock.lockInterruptibly();
            try
            {
                // queue is empty, wait for items
                if (count == 0)
                {
                    notEmpty.await();
                }

                // get item from head index
                final Object[] items = this.elements;
                @SuppressWarnings("unchecked")
                E x = (E) items[head];
                items[head] = null;
                head++;
                // if index arrived array's end, reset it
                if (head == items.length)
                {
                    head = 0;
                }
                count--;
                notFull.signal();
                return x;

            }
            finally
            {
                lock.unlock();
            }
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Get item from queue without pop it. This method will not be blocked if queue is empty.
     * @return item or null if queue is empty
     */
    @SuppressWarnings("unchecked")
    @Override
    public E peek()
    {
       final ReentrantLock lock = this.fullLock;
       lock.lock();
       try
       {
           return (E) this.elements[head];
       }
       finally
       {
           lock.unlock();
       }
    }
}
