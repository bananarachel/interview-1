/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.repository;

import com.vmware.interview.matthew.domain.Customer;
import com.vmware.interview.matthew.domain.Service;
import com.vmware.interview.matthew.form.SubscribeServiceForm;

import java.util.Optional;

/**
 * Helper class to save subscribe relationship
 */
public class SubscribeRepository
{
    public static boolean subscribe(CustomerRepository customerRepository, ServiceRepository serviceRepository, SubscribeServiceForm form)
    {
        try
        {
            Optional<Customer> optionalCustomer = customerRepository.findById(form.getCustomerId());
            Optional<Service> optionalService = serviceRepository.findById(form.getSubscribeServiceId());
            if (!optionalCustomer.isPresent() || !optionalService.isPresent())
            {
                return false;
            }

            Customer customer = optionalCustomer.get();
            Service service = optionalService.get();
            customer.getSubscribedService().add(service);
            service.getCustomers().add(customer);
            customerRepository.save(customer);
            serviceRepository.save(service);
        } catch (Exception e)
        {
            //TODO: write exception to log
            return false;
        }
        return true;
    }
}
