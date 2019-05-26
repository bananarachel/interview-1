/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

/**
 * Domain class for table customer
 */
@Entity
@Table(name = "Customer")
public class Customer
{
    @Id
    @Column(name = "CUSTOMER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_INFO")
    private String customerInfo;

    @ManyToMany(mappedBy = "customers")
    private Set<Service> subscribedService;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

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

    public Set<Service> getSubscribedService()
    {
        return subscribedService;
    }

    public void setSubscribedService(Set<Service> subscribedService)
    {
        this.subscribedService = subscribedService;
    }
}
