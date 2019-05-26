/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * Domain class for table service
 */
@Entity
@Table(name = "SERVICE")
public class Service
{
    @Id
    @Column(name = "SERVICE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "SERVICE_NAME")
    private String serviceName;

    @Column(name = "SERVICE_INFO")
    private String serviceInfo;

    /**
     * Model Many to Many relationship
     */
    @ManyToMany
    @JoinTable(
            name = "SERVICE_CUSTOMER",
            joinColumns = @JoinColumn(name = "SERVICE_ID"),
            inverseJoinColumns = @JoinColumn(name = "CUSTOMER_ID"))
    @JsonIgnore
    private Set<Customer> customers;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getServiceInfo()
    {
        return serviceInfo;
    }

    public void setServiceInfo(String serviceInfo)
    {
        this.serviceInfo = serviceInfo;
    }

    public Set<Customer> getCustomers()
    {
        return customers;
    }

    public void setCustomers(Set<Customer> customers)
    {
        this.customers = customers;
    }
}
