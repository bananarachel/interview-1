/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

/**
 * Working item will put into queue
 */
class WorkingItem
{
    private int payload;
    private boolean isEndingItem;
    private long threadId;
    private long startTimestamp;
    private long finishTimestamp;

    WorkingItem(int payload, boolean isEndingItem, long threadId)
    {
        this.payload = payload;
        this.isEndingItem = isEndingItem;
        this.threadId = threadId;
        this.startTimestamp = System.currentTimeMillis();
    }

    /**
     * Check is ending working item, consumer will stopping processing while find ending item
     * @return true if item is ending
     */
    boolean isEndingItem()
    {
        return this.isEndingItem;
    }
    long finishTask()
    {
        this.finishTimestamp = System.currentTimeMillis();
        return this.finishTimestamp - this.startTimestamp;
    }
}
