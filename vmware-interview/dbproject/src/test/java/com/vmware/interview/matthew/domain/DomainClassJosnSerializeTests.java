/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class DomainClassJosnSerializeTests
{
    @Test
    public void testCustomer() throws IOException
    {
        Customer customer = new Customer();
        customer.setId(0);
        customer.setCustomerName("test 1");
        customer.setCustomerInfo("test info 1");

        Service service = new Service();
        service.setId(1);
        service.setServiceName("service 1");
        service.setServiceInfo("service info 1");

        Set<Service> services = new HashSet<>();
        services.add(service);
        customer.setSubscribedService(services);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(customer);

        assertThat(result, containsString("customerName"));
        assertThat(result, containsString("customerInfo"));
        assertThat(result, containsString("subscribedService"));
        assertThat(result, containsString("serviceName"));
        assertThat(result, containsString("serviceInfo"));
        assertThat(result, containsString("id"));

        Customer newCustomer = mapper.readValue(result, Customer.class);

        assertEquals(customer.getId(), newCustomer.getId());
        assertEquals(customer.getCustomerName(), newCustomer.getCustomerName());
        assertEquals(customer.getCustomerInfo(), newCustomer.getCustomerInfo());
        Set<Service> newServices = customer.getSubscribedService();
        assertEquals(services.size(), newServices.size());

        assertTrue(newServices.contains(service));

    }
}
