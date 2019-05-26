/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import org.junit.Test;


import java.util.Queue;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

/**
 * Test queue
 */
public class ConcurrentQueueTest
{

    private final int PRODUCER_THREAD_NUMBER = 3;
    private final int CONSUME_THREAD_NUMBER = 3;
    private final int TOTAL_PRODUCE_NUMBER = 10000;

    private void runTest(String queueName, Queue<WorkingItem> queue, int totalProduce,
                         int producerThreadNumber, int consumerThreadNumber, boolean verbose)
    {
        long startTime = System.currentTimeMillis();
        ExecutorService producer = Executors.newFixedThreadPool(producerThreadNumber);
        ExecutorService consumer = Executors.newFixedThreadPool(consumerThreadNumber);


        CountDownLatch produceLatch = new CountDownLatch(producerThreadNumber);
        CountDownLatch consumeLatch = new CountDownLatch(consumerThreadNumber);
        for (int i = 0; i < totalProduce; i++)
        {
            ProduceTask task = new ProduceTask(queue, i + "", totalProduce, produceLatch, verbose);
            producer.execute(task);
        }

        ConsumeTask[] tasks = new ConsumeTask[consumerThreadNumber];
        for (int i = 0; i < consumerThreadNumber; i++)
        {
            ConsumeTask task = new ConsumeTask(queue, i + "", consumeLatch, verbose);
            tasks[i] = task;
            consumer.execute(task);
        }

        try
        {
            produceLatch.await();
            consumeLatch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        producer.shutdown();
        consumer.shutdown();

        double total = 0;
        int totalConsume = 0;
        for (ConsumeTask task : tasks)
        {
            total += task.getTotalTimestamp();
            totalConsume += task.getTotalConsume();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(queueName
                + ": total test time (s) =  " + String.format("%.2f", (endTime - startTime) / 1000.0)
                + ": total produce messages = " + totalProduce * producerThreadNumber
                + ": total consume messages = " + totalConsume
                + ": total queue time (s) = " + String.format("%.2f",total / 1000.0) + ": "
                + ": speed  = " + String.format("%.2f",  totalProduce * producerThreadNumber  / (total / 1000.) ) + " m / s"  );
    }

    @Test
    public void testArrayBlockingQueue()
    {
        Queue<WorkingItem> queue = new ArrayBlockingQueue<WorkingItem>(1024 * 1024);

        runTest("ArrayBlockingQueue", queue, TOTAL_PRODUCE_NUMBER * 1,
                PRODUCER_THREAD_NUMBER,CONSUME_THREAD_NUMBER, false);
    }

    @Test
    public void testLinkedBlockingQueue()
    {
        Queue<WorkingItem> queue = new LinkedBlockingQueue<>();

        runTest("LinkedBlockingQueue", queue, TOTAL_PRODUCE_NUMBER * 1,
                PRODUCER_THREAD_NUMBER, CONSUME_THREAD_NUMBER, false);
    }

    @Test
    public void testConcurrentLinkedQueue()
    {
        Queue<WorkingItem> queue = new ConcurrentLinkedQueue<>();

        runTest("ConcurrentLinkedQueue", queue, TOTAL_PRODUCE_NUMBER * 1,
                PRODUCER_THREAD_NUMBER, CONSUME_THREAD_NUMBER, false);
    }

    @Test
    public void testConcurrentQueue()
    {
        Queue<WorkingItem> queue = new ConcurrentQueue<>(1024 * 1024);

        runTest("ConcurrentQueue", queue, TOTAL_PRODUCE_NUMBER * 1,
                PRODUCER_THREAD_NUMBER, CONSUME_THREAD_NUMBER, false);
    }

    @Test
    public void testBasicConcurrentQueue()
    {
        Queue<WorkingItem> queue = new ConcurrentQueue<>(1024 * 1024);
        for (int i = 0; i < 1024 * 1024; i++)
        {
            WorkingItem item = new WorkingItem(1, false, 1);
            queue.offer(item);
            WorkingItem result = queue.poll();

            assertEquals(item, result);
        }
    }

}