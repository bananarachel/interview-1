/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 * Producer task to produce working item and put it into queue
 */
public class ProduceTask implements Runnable
{
    /**
     * Queue for putting working item
     */
    private Queue<WorkingItem> queue;
    private String taskName;
    private final int totalProduce;
    /**
     * latch to indicate producer thread finished
     */
    private CountDownLatch finishLatch;
    private boolean verbose;

    /**
     * Sleep interval
     */
    private final int interval = 1; //ns


    public ProduceTask(Queue<WorkingItem> queue, String name, int totalProduce, CountDownLatch latch, boolean verbose)
    {
        this.queue = queue;
        this.taskName = name;
        this.totalProduce = totalProduce;
        this.finishLatch = latch;
        this.verbose = verbose;
    }

    private void printMessage(String message)
    {
        if (this.verbose)
        {
            System.out.println(System.currentTimeMillis() + ": " + Thread.currentThread().getId() + ": Producer " + taskName + ": " + message);
        }
    }

    @Override
    public void run()
    {
        printMessage("start produce");

        int index = 0;
        while (index < totalProduce)
        {
            try
            {

                WorkingItem item = new WorkingItem(index, false, Thread.currentThread().getId());
                if(this.queue.offer(item))
                {
                    index++;
                }
                LockSupport.parkNanos(interval);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if (index % 10000 == 0)
            {
                printMessage("produced " + index + " messages");
            }
        }

        // add ending working item to queue
        for (int i = 0; i < 100; ++i)
        {
            WorkingItem endingItem = new WorkingItem(i, true, Thread.currentThread().getId());
            this.queue.offer(endingItem);
        }

        printMessage(" finished.");
        this.finishLatch.countDown();
    }
}
