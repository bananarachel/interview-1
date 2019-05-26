/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

/**
 * Consumer task to take working item from queue
 */
public class ConsumeTask implements Runnable
{
    /**
     * Queue for poll working item
     */
    private Queue<WorkingItem> queue;
    private String taskName;

    private AtomicInteger totalConsume;
    private AtomicLong totalTimestamp;
    private boolean verbose;

    /**
     * Latch to indicate consumer thread finished
     */
    private CountDownLatch finishLatch;

    /**
     * Sleeping interval
     */
    private final int consumeTime = 1; // ns

    public ConsumeTask(Queue<WorkingItem> queue, String name, CountDownLatch latch, boolean verbose)
    {
        this.queue = queue;
        this.taskName = name;
        this.totalConsume = new AtomicInteger(0);
        this.totalTimestamp = new AtomicLong(0);
        this.finishLatch = latch;
        this.verbose = verbose;
    }

    private void printMessage(String message)
    {
        if (this.verbose)
        {
            System.out.println(System.currentTimeMillis() + ": " + Thread.currentThread().getId() + ": Consumer " + taskName + ": " + message);
        }
    }
    @Override
    public void run()
    {
        printMessage("start consume");

        int total = 0;
        double totalTime = 0;
        while (true)
        {
            try
            {
                WorkingItem item = this.queue.poll();
                if (item != null)
                {
                    // If item indicate ending, break while loop
                    if (item.isEndingItem())
                    {
                        break;
                    }
                    totalTime += item.finishTask();
                    total++;
                }

                LockSupport.parkNanos(consumeTime);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (total % 10000 == 0)
            {
                printMessage("consume " + total + " messages" );
            }
        }

        this.totalConsume.compareAndSet(0,total);
        this.totalTimestamp.compareAndSet(0, (long)totalTime);
        printMessage("total consume= " + this.totalConsume + ": total time: " + this.totalTimestamp);
        this.finishLatch.countDown();
    }

    long getTotalTimestamp()
    {
        return totalTimestamp.get();
    }

    int getTotalConsume()
    {
        return this.totalConsume.get();
    }
}
