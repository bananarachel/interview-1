/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.form;

/**
 * Form used for create service
 */
public class ServiceForm
{
    private String serviceName;
    private String serviceInfo;

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
}
