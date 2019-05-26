/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import com.vmware.interview.matthew.domain.Customer;
import com.vmware.interview.matthew.domain.Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DBProjectApplication.class)
public class SubscribeControllerTests
{

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetServices()
    {
        // Test get all services
        ResponseEntity<Service[]> response = this.restTemplate.getForEntity("/subscribe/services", Service[].class);
        Service[] services = response.getBody();
        assertNotNull(services);
        assertTrue(services.length > 0);
        for (Service service : services)
        {
            assertNotNull(service.getServiceInfo());
            assertNotNull(service.getServiceName());
            assertTrue(service.getId() > 0);
        }

        // Test get service by ID
        Service expectedService = services[0];
        ResponseEntity<Service> serviceResponseEntity = this.restTemplate.getForEntity("/subscribe/services/" + expectedService.getId(), Service.class);
        Service actualService = serviceResponseEntity.getBody();
        assertEquals(expectedService.getId(), actualService.getId());
    }

    @Test
    public void testGetCustomers()
    {
        // Test get all customers
        ResponseEntity<Customer[]> response = this.restTemplate.getForEntity("/subscribe/customers", Customer[].class);
        Customer[] customers = response.getBody();
        assertNotNull(customers);
        assertTrue(customers.length > 0);
        for (Customer customer : customers)
        {
            assertNotNull(customer.getCustomerInfo());
            assertNotNull(customer.getCustomerName());
            assertTrue(customer.getId() > 0);

            Set<Service> services = customer.getSubscribedService();
            assertNotNull(services);
            assertTrue(services.size() > 0);
            for (Service service : services)
            {
                assertNotNull(service.getServiceInfo());
                assertNotNull(service.getServiceName());
                assertTrue(service.getId() > 0);
            }
        }

        // Test get customers by ID
        Customer expectedCustomer = customers[0];
        ResponseEntity<Customer> customerResponseEntity = this.restTemplate.getForEntity("/subscribe/customers/" + expectedCustomer.getId(), Customer.class);
        Customer actualCustomer = customerResponseEntity.getBody();
        assertEquals(expectedCustomer.getId(), actualCustomer.getId());
    }

    @Test
    public void testGetNotExistsCustomer()
    {
        ResponseEntity<Customer> customerResponseEntity = this.restTemplate.getForEntity("/subscribe/customers/" + Integer.MAX_VALUE, Customer.class);
        assertEquals(HttpStatus.NOT_FOUND, customerResponseEntity.getStatusCode());
    }

    @Test
    public void testGetNotExistsService()
    {
        ResponseEntity<Service> serviceResponseEntity = this.restTemplate.getForEntity("/subscribe/services/" + Integer.MAX_VALUE, Service.class);
        assertEquals(HttpStatus.NOT_FOUND, serviceResponseEntity.getStatusCode());
    }

    @Test
    public void testAvailableService()
    {
        ResponseEntity<Customer[]> response = this.restTemplate.getForEntity("/subscribe/customers", Customer[].class);
        Customer[] customers = response.getBody();
        assertNotNull(customers);
        assertTrue(customers.length > 0);

        Customer customer = customers[0];
        Set<Service> subscribedService = customer.getSubscribedService();

        ResponseEntity<Service[]> serviceResponse = this.restTemplate.getForEntity("/subscribe/customers/" + customer.getId() + "/available", Service[].class);
        Service[] services = serviceResponse.getBody();
        assertNotNull(services);

        // Returned services should not include any subscribed service
        for (Service service : services)
        {
            for (Service sub : subscribedService)
            {
                if (sub.getId() == service.getId())
                {
                    fail("Available service should not contains subscribed service.");
                }
            }
        }
    }

    @Test
    public void testSubscribeService()
    {
        ResponseEntity<Customer[]> response = this.restTemplate.getForEntity("/subscribe/customers", Customer[].class);
        Customer[] customers = response.getBody();
        assertNotNull(customers);
        assertTrue(customers.length > 0);

        Customer customer = customers[0];
        Set<Service> subscribedService = customer.getSubscribedService();
        assertNotNull(subscribedService);

        // Test get all services
        ResponseEntity<Service[]> sResponse = this.restTemplate.getForEntity("/subscribe/services", Service[].class);
        Service[] services = sResponse.getBody();
        assertNotNull(services);
        assertTrue(services.length > 0);

        // Try to find candidate service which not belong subscribed service
        Service candidateService = null;
        for (Service service : services)
        {
            if (!subscribedService.contains(service))
            {
                candidateService = service;
            }
        }

        if (candidateService != null)
        {
            // Test subscribe service
            ResponseEntity<Customer> cResponse = this.restTemplate.postForEntity("/subscribe/" + candidateService.getId() + "/" + customer.getId(),
                    null, Customer.class);
            Customer actualCustomer = cResponse.getBody();
            assertNotNull(actualCustomer);
            Set<Service> actualService = actualCustomer.getSubscribedService();
            assertNotNull(actualService);
            // Make sure returned customer's subscribed services contains candidate one
            boolean contains = false;
            for (Service service : actualService)
            {
                if (service.getId() == candidateService.getId())
                {
                    contains = true;
                    break;
                }
            }

            assertTrue(contains);

            // Try to subscribe again, expect 500 error
            ResponseEntity<Customer> errorResponse = this.restTemplate.postForEntity("/subscribe/" + candidateService.getId() + "/" + customer.getId(),
                    null, Customer.class);
            assertEquals(HttpStatus.valueOf(500), errorResponse.getStatusCode());

            // Test unsubscribe service
            ResponseEntity<Customer> unsubResponse = this.restTemplate.exchange("/subscribe/" + candidateService.getId() + "/" + customer.getId(),
                    HttpMethod.DELETE, null, Customer.class);
            Customer unCustomer = unsubResponse.getBody();
            assertNotNull(unCustomer);

            Set<Service> unService = unCustomer.getSubscribedService();
            assertNotNull(unService);
            for (Service service : unService)
            {
                if (service.getId() == candidateService.getId())
                {
                    fail("unsubscribe service returned result should not include candidate one");
                }
            }

        }
        else
        {
            fail("cannot find candidate service for subscribe");
        }
    }


}
