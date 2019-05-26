/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.form;

/**
 * Form used for subscribe service
 */
public class SubscribeServiceForm
{
    private Long subscribeServiceId;
    private Long customerId;

    public Long getSubscribeServiceId()
    {
        return subscribeServiceId;
    }

    public void setSubscribeServiceId(Long subscribeServiceId)
    {
        this.subscribeServiceId = subscribeServiceId;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }


}
