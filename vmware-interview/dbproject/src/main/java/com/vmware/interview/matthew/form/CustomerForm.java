/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.form;

/**
 * Form used for create new customer
 */
public class CustomerForm
{
    private String customerName;
    private String customerInfo;

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCustomerInfo()
    {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo)
    {
        this.customerInfo = customerInfo;
    }
}
