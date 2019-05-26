/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import com.vmware.interview.matthew.domain.Customer;
import com.vmware.interview.matthew.domain.Service;
import com.vmware.interview.matthew.repository.CustomerRepository;
import com.vmware.interview.matthew.repository.ServiceRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subscribe")
@Api(value = "subscribe", description = "subscribe service", tags = ("subscribe"))
public class SubscribeController
{
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceRepository serviceRepository;


    @RequestMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "Get All Services", notes = "Get all services in the system", nickname = "getAllServices")
    public List<Service> getAllServices()
    {
        return (List<Service>) this.serviceRepository.findAll();
    }

    @RequestMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "Create new service", notes = "Create new service", nickname = "createService")
    public Service createService(@ApiParam(value = "Service input form") @RequestBody Service input)
    {
        if (StringUtils.isEmpty(input.getServiceName()) || StringUtils.isEmpty(input.getServiceInfo()))
        {
            throw new ResponseStatusException(HttpStatus.valueOf(550), "Service name or info cannot be empty.");
        }
        this.serviceRepository.save(input);
        return input;
    }

    @RequestMapping(value = "/services/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "Get Service by ID", notes = "Get service by specified ID in the system", nickname = "getService")
    public Service getServiceById(@ApiParam(value = "Service ID") @PathVariable("id")Long serviceId)
    {
        return checkService(serviceId);
    }

    private Service checkService(Long serviceId)
    {
        Optional<Service> optionalService = this.serviceRepository.findById(serviceId);
        if (!optionalService.isPresent())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find service by id");
        }
        return optionalService.get();
    }

    @RequestMapping(value = "/services/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Service by ID", notes = "Delete service by specified ID in the system", nickname = "deleteService")
    public boolean deleteServiceById(@ApiParam(value = "Service ID") @PathVariable("id")Long serviceId)
    {
        Service service = checkService(serviceId);
        this.serviceRepository.delete(service);
        return true;
    }


    @RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "Get All Customers", notes = "Get all customers in the system", nickname = "getAllCustomers")
    public List<Customer> getAllCustomers()
    {
        return (List<Customer>) this.customerRepository.findAll();
    }

    @RequestMapping(value = "/customers/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "Get customer by ID", notes = "Get customer by specified ID in the system", nickname = "getCustomerById")
    public Customer getCustomerById(@ApiParam(value = "Customer ID") @PathVariable("id")Long customerId)
    {
        return checkCustomer(customerId);
    }

    @RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "Create customer", notes = "Create customer in the system", nickname = "createCustomer")
    public Customer createCustomer(@ApiParam(value = "Customer input form") @RequestBody Customer input)
    {
        if (StringUtils.isEmpty(input.getCustomerName()) || StringUtils.isEmpty(input.getCustomerInfo()) )
        {
            throw new ResponseStatusException(HttpStatus.valueOf(550), "Customer name or info cannot be empty");
        }

        this.customerRepository.save(input);
        return input;
    }

    private Customer checkCustomer(Long customerId)
    {
        Optional<Customer> optionalCustomer = this.customerRepository.findById(customerId);
        if (!optionalCustomer.isPresent())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find customer base on ID");
        }

        return optionalCustomer.get();
    }
    @RequestMapping(value = "/customers/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete customer by ID", notes = "Delete customer by specified ID in the system", nickname = "deleteCustomerById")
    public boolean deleteCustomerById(@ApiParam(value = "Customer ID") @PathVariable("id")Long customerId)
    {
        Customer customer = checkCustomer(customerId);
        this.customerRepository.delete(customer);
        return true;
    }

    @RequestMapping(value = "/customers/{id}/available", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "Get available service", notes = "Get all available service for subscribe ", nickname = "getAvailableService")
    public List<Service> getAvailableService(@ApiParam(value = "Customer ID") @PathVariable("id")Long customerId)
    {
        Customer customer = checkCustomer(customerId);

        List<Service> allServices = (List<Service>) this.serviceRepository.findAll();
        allServices.removeAll(customer.getSubscribedService());
        return allServices;
    }

    @RequestMapping(value = "/{serviceID}/{customerID}",produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST )
    @ApiOperation(value = "Subscribe service", notes = "Customer subscribe service ", nickname = "subscribeService")
    public Customer subscribeService(
            @ApiParam(value = "Service ID")
            @PathVariable("serviceID")Long serviceID,
            @ApiParam(value = "Customer ID")
            @PathVariable("customerID")Long customerID)
    {
        Customer customer = checkCustomer(customerID);
        Service service = checkService(serviceID);

        // Already subscribed
        if (customer.getSubscribedService().contains(service))
        {
            throw new ResponseStatusException(HttpStatus.valueOf(500), "service already subscribed.");
        }
        customer.getSubscribedService().add(service);
        service.getCustomers().add(customer);
        customerRepository.save(customer);
        serviceRepository.save(service);
        return customer;
    }

    @RequestMapping(value = "/{serviceID}/{customerID}",produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE )
    @ApiOperation(value = "unsubscribe service", notes = "Customer unsubscribe service ", nickname = "unsubscribeService")
    public Customer unsubscribeService(
            @ApiParam(value = "Service ID")
            @PathVariable("serviceID")Long serviceID,
            @ApiParam(value = "Customer ID")
            @PathVariable("customerID")Long customerID)
    {
        Customer customer = checkCustomer(customerID);
        Service service = checkService(serviceID);

        if (!customer.getSubscribedService().contains(service))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not subscribe service.");
        }

        customer.getSubscribedService().remove(service);
        service.getCustomers().remove(customer);
        customerRepository.save(customer);
        serviceRepository.save(service);

        return customer;
    }

}
